package com.tickets.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

import javax.crypto.Cipher;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tickets.constants.Constants;
import com.tickets.constants.Messages;
import com.tickets.constants.Settings;
import com.tickets.exceptions.UserException;
import com.tickets.model.User;
import com.tickets.utils.CertificateManager;
import com.tickets.utils.base64.Base64Decoder;
import com.tickets.utils.base64.Base64Encoder;

@Service("userService")
public class UserServiceImpl extends BaseService<User> implements UserService {

    private static Logger log = Logger.getLogger(UserServiceImpl.class);
    public static final String ENCODING = "ISO-8859-1";

    @SuppressWarnings("unchecked")
    public int login(String username, char[] password,
            boolean isStaff, boolean passwordAlreadyHashed) throws UserException {

        String passParam = "";
        if (passwordAlreadyHashed)
            passParam = new String(password);
        else
            passParam = hash(new String(password));

        List<User> result = getDao().findByNamedQuery("User.login", new String[] {
                "username", "password"}, new Object[] {username, passParam});

        if (result == null || result.size() != 1) {
            result = getDao().findByNamedQuery("User.tempLogin", new String[] {
                    "username", "password"}, new Object[] {username,
                    hash(new String(password))});

            if (result == null || result.size() != 1) {
                throw UserException.INCORRECT_LOGIN_DATA;
            } else if (!result.get(0).isActive()) {
                throw UserException.USER_INACTIVE;
            } else {
                User user = result.get(0);
                user.setChangePasswordAfterLogin(true);
                //getDao().save(user);
                return user.getUserId();
            }
        } else if (!result.get(0).isActive()) {
            throw UserException.USER_INACTIVE;
        } else {
            return result.get(0).getUserId();
        }
    }

    public String encryptPassword(char[] password) {
        try {
            byte[] bytes = new String(password).getBytes(ENCODING);

            PublicKey key = CertificateManager.getPublicKey();
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            String base64String = Base64Encoder.toBase64String(cipher.doFinal(bytes));

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

    public void register(User user) throws UserException {
        if (user == null)
            return;

        user.setPassword(hash(user.getPassword()));
        user.setActivationCode(getHexString(salt(user.getUsername())
                        .getBytes()));

        user.setRegisteredTimestamp(System.currentTimeMillis());

        getDao().save(user);
        try {
            HtmlEmail email = getPreconfiguredMail();
            email.addTo(user.getEmail());
            email.setFrom(Settings.getValue("confirmation.email.sender"));
            email.setSubject(Messages.getString("confirmation.email.subject"));
            email.setHtmlMsg(Messages.getString("confirmation.email.content",
                    user.getName(), user.getUsername(), user.getRepeatPassword(),
                    Settings.getValue("base.url")
                            + "/users.do?method=activate&code="
                            + user.getActivationCode()));

            email.send();

        } catch (EmailException eex) {
            log.error("Mail problem", eex);
            throw UserException.EMAIL_PROBLEM;
        }
    }

    @SuppressWarnings("unchecked")
    public User activateUserWithCode(String code) throws UserException {
        List<User> list = getDao().findByQuery("select u from User u "
                + "where u.activationCode=:code", new String[] {"code"},
                new Object[] {code});

        if (list.size() == 1) {
            User user = list.get(0);
            if (!user.isActive()) {
                user.setActive(true);
                return getDao().save(user);
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
        int result = getDao().executeQuery("delete from User u "
                + "WHERE u.registeredTimestamp < :treshold AND active=false",
                new String[] {"treshold"}, new Object[] {System
                        .currentTimeMillis()
                        - Constants.ONE_DAY});

        log.info("Cleaning inactive users : " + result);
    }

    public void remindPassword(String email)
            throws UserException {
        List list = getDao().findByNamedQuery("User.getByEmail",
                new String[] {"email"}, new Object[] {email});

        if (list.size() > 0) {
            User user = (User) list.get(0);
            String tempPassword = generateTemporaryPassword();
            user.setTemporaryPassword(hash(tempPassword));

            try {
                HtmlEmail mail = getPreconfiguredMail();
                mail.setFrom(Settings.getValue("temp.password.email.sender"));
                mail.addTo(email);
                mail.setSubject(Messages.getString("temp.password.email.subject"));
                mail.setHtmlMsg(Messages.getString("temp.password.email.content",
                        user.getName(), tempPassword));
                mail.send();
                getDao().save(user);
            } catch (EmailException eex) {
                log.error("Mail server not configured", eex);
                throw UserException.UNEXPECTED_PROBLEM;
            }
        } else {
            throw UserException.INVALID_EMAIL;
        }
    }

    public void changePassword(String password, User user) {
        user.setPassword(hash(password));
        user.setTemporaryPassword("");
        getDao().saveOrUpdate(user);
    }

    private HtmlEmail getPreconfiguredMail() {
        HtmlEmail se = new HtmlEmail();
        se.setHostName(Settings.getValue("smtp.host"));
        String username = Settings.getValue("smtp.user");
        if (username.length() > 0)
            se.setAuthentication(username, Settings.getValue("smtp.password"));

        se.setCharset("utf-8");
        return se;

    }

    private String generateTemporaryPassword() {
        int length = 6 + (int) (Math.random() * 6);
        byte[] chars = new byte[length];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (byte) (33 + (Math.random() * 93));
        }
        return new String(chars).trim();
    }

    private char[] saltChars = new char[] {'!', 'b', '0', 'z', 'h', 'o'};

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

    private String hash(String password) {
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

}