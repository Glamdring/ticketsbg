package com.tickets.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

import javax.crypto.Cipher;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tickets.constants.Constants;
import com.tickets.constants.Messages;
import com.tickets.constants.Settings;
import com.tickets.dao.ValidationBypassingEventListener;
import com.tickets.exceptions.UserException;
import com.tickets.model.Firm;
import com.tickets.model.User;
import com.tickets.utils.CertificateManager;
import com.tickets.utils.GeneralUtils;
import com.tickets.utils.base64.Base64Decoder;
import com.tickets.utils.base64.Base64Encoder;

@Service("userService")
public class UserServiceImpl extends BaseService<User> implements UserService {

    private static Logger log = Logger.getLogger(UserServiceImpl.class);
    public static final String ENCODING = "ISO-8859-1";

    @SuppressWarnings("unchecked")
    public User login(String username, char[] password,
            boolean passwordAlreadyHashed) throws UserException {

        String passParam = "";
        if (passwordAlreadyHashed)
            passParam = new String(password);
        else
            passParam = saltAndHashPassword(new String(password));

        List<User> result = getDao().findByNamedQuery("User.login",
                new String[] { "username", "password" },
                new Object[] { username, passParam });

        if (result == null || result.size() != 1) {
            result = getDao().findByNamedQuery("User.tempLogin",
                    new String[] { "username", "password" },
                    new Object[] { username, saltAndHashPassword(new String(password)) });

            if (result == null || result.size() != 1) {
                throw UserException.INCORRECT_LOGIN_DATA;
            } else if (!result.get(0).isActive()) {
                throw UserException.USER_INACTIVE;
            } else {
                User user = result.get(0);
                user.setChangePasswordAfterLogin(true);
                return user;
            }
        } else if (!result.get(0).isActive()) {
            throw UserException.USER_INACTIVE;
        } else {
            User user = result.get(0);
            user.setChangePasswordAfterLogin(false);
            return user;
        }
    }

    public String encryptPassword(char[] password) {
        try {
            byte[] bytes = new String(password).getBytes(ENCODING);

            PublicKey key = CertificateManager.getPublicKey();
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            String base64String = Base64Encoder.toBase64String(cipher
                    .doFinal(bytes));

            return base64String;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public char[] decryptPassword(String password) {
        try {
            byte[] bytes = Base64Decoder.toByteArray(password);
            PrivateKey key = CertificateManager.getPrivateKey();
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(bytes), ENCODING).toCharArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public User register(User user) throws UserException {
        if (user == null)
            return null;

        List existingUser = getDao().findByNamedQuery("User.getByUsername",
                new String[] { "username" },
                new Object[] { user.getUsername() });

        if (existingUser.size() > 0) {
            throw UserException.USER_ALREADY_EXISTS;
        }

        List existingEmail = getDao().findByNamedQuery("User.getByEmail",
                new String[] { "email" }, new Object[] { user.getEmail() });

        if (existingEmail.size() > 0) {
            throw UserException.EMAIL_ALREADY_EXISTS;
        }

        user.setPassword(saltAndHashPassword(user.getPassword()));

        try {
            MessageDigest digester = MessageDigest.getInstance("SHA-1");
            digester.update(user.getPassword().getBytes());
            user.setActivationCode(getHexString(digester.digest()));
        } catch (NoSuchAlgorithmException e) {
            user.setActivationCode(getHexString(user.getUsername().getBytes()));
        }

        user.setRegisteredTimestamp(System.currentTimeMillis());

        try {
            Email email = GeneralUtils.getPreconfiguredMail(false);
            email.addTo(user.getEmail(), user.getName());
            email.setFrom(Settings.getValue("confirmation.email.sender"));
            email.setSubject(Messages.getString("confirmation.email.subject"));
            email.setMsg(Messages.getString("confirmation.email.content",
                    user.getName(), user.getUsername(), user
                            .getRepeatPassword(), Settings.getValue("base.url")
                            + "/activate.jsp?code="
                            + user.getActivationCode()));

            email.send();
        } catch (EmailException eex) {
            log.error("Mail problem", eex);
            throw UserException.EMAIL_PROBLEM;
        }
        //Save after a successful email
        user = (User) getDao().persist(user);

        return user;
    }

    @SuppressWarnings("unchecked")
    public User activateUserWithCode(String code) throws UserException {
        List<User> list = getDao().findByQuery(
                "select u from User u " + "where u.activationCode=:code",
                new String[] { "code" }, new Object[] { code });

        if (list.size() == 1) {
            User user = list.get(0);
            if (!user.isActive()) {
                user.setActive(true);
                getDao().persist(user);
                return user;
            }
            throw UserException.USER_ALREADY_ACTIVE;
        }
        throw UserException.INVALID_ACTIVATION_CODE;
    }

    public User getUserById(String id) {
        try {
            int intId = Integer.parseInt(id);
            return getDao().getById(User.class, intId);
        } catch (Exception e) {
            return null;
        }
    }

    public void clearNonActivatedUsers() {
        int result = getDao()
                .executeQuery(
                        "delete from User u "
                                + "WHERE u.registeredTimestamp < :treshold AND active=false",
                        new String[] { "treshold" },
                        new Object[] { System.currentTimeMillis()
                                - Constants.ONE_DAY });

        log.info("Cleaning inactive users : " + result);
    }

    public void sendTemporaryPassword(String email) throws UserException {
        List list = getDao().findByNamedQuery("User.getByEmail",
                new String[] { "email" }, new Object[] { email });

        if (list.size() > 0) {
            User user = (User) list.get(0);
            String tempPassword = generateTemporaryPassword();

            try {
                Email mail = GeneralUtils.getPreconfiguredMail(false);
                mail.setFrom(Settings.getValue("temp.password.email.sender"));
                mail.addTo(email, user.getName());
                mail.setSubject(Messages
                        .getString("temp.password.email.subject"));
                mail.setMsg(Messages.getString(
                        "temp.password.email.content", user.getName(),
                        tempPassword, user.getUsername()));
                mail.send();

                //Set after the mail, so that no update is triggered
                user.setTemporaryPassword(saltAndHashPassword(tempPassword));
                getDao().persist(user);
            } catch (EmailException eex) {
                log.error("Mail server not configured", eex);
                throw UserException.EMAIL_PROBLEM;
            }
        } else {
            throw UserException.INVALID_EMAIL;
        }
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(saltAndHashPassword(newPassword));
        user.setTemporaryPassword("");
        getDao().persist(user);
    }

    private String generateTemporaryPassword() {
        int length = 8 + (int) (Math.random() * 4);
        byte[] chars = new byte[length];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (byte) (48 + (Math.random() * 89));
        }
        try {
            MessageDigest digester = MessageDigest.getInstance("SHA-1");
            digester.update(new String(chars).getBytes());
            return getHexString(digester.digest()).substring(0, length);
        } catch (NoSuchAlgorithmException ex) {
            return new String(chars).trim();
        }
    }

    private char[] saltChars = new char[] { '!', 'b', '0', 'z', 'h', 'o' };

    private String salt(String password) {
        StringBuffer sb = new StringBuffer();
        char[] chars = password.toCharArray();
        int crystal = 0;
        for (int i = 0; i < chars.length; i++) {
            sb.append(chars[i]);
            if (i % 2 == 0 && crystal < saltChars.length) {
                sb.append(saltChars[crystal++]);
            }
        }
        return sb.toString();
    }

    public String saltAndHashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA");
            password = salt(password);
            digest.update(password.getBytes());
            String result = getHexString(digest.digest());
            return result;
        } catch (NoSuchAlgorithmException e) {
            log.error("Hashing algorithm not found");
            return password;
        }
    }

    private String getHexString(byte[] array) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            hexString.append(Integer.toHexString(0xFF & array[i]));
        }

        return hexString.toString();
    }

    public List list() {
        return list(User.class);
    }

    public List listOrdered(String orderColumn) {
        return listOrdered(User.class, orderColumn);
    }

    @SuppressWarnings("unchecked")
    @Override
    public User findUser(String userName) {
        List<User> result = getDao().findByNamedQuery("User.getByUsername", new String[] {
                "username"}, new Object[] {userName});

        if(result != null && result.size() > 0) {
            return result.get(0);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> fetchUsers(Firm firm) {
        List result = getDao().findByNamedQuery("User.getByFirm",
                new String[] { "firm" },
                new Object[] { firm });

        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> fetchAgentsUsers(Firm firm) {
        List result = getDao().findByNamedQuery("User.getAgentsUsersByFirm",
                new String[] { "firm" },
                new Object[] { firm });

        return result;
    }

    @Override
    public boolean isHash(String password) {
        return password.length() == 37 && password.matches("[0-9abcdef]+");
    }



    @Override
    public User save(User user) {
        ValidationBypassingEventListener.turnValidationOff();
        try {
            return super.save(user);
        } finally {
            ValidationBypassingEventListener.turnValidationOn();
        }
    }
}