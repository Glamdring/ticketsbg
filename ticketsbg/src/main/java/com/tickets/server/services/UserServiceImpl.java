package com.tickets.server.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tickets.client.constants.Constants;
import com.tickets.client.exceptions.UserException;
import com.tickets.client.model.User;
import com.tickets.client.services.UserService;
import com.tickets.server.constants.Msgs;
import com.tickets.server.constants.Settings;

@Service("userService")
public class UserServiceImpl extends BaseService<User> implements UserService {

    private static Logger log = Logger.getLogger(UserServiceImpl.class);

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
                throw new UserException(Constants.INCORRECT_LOGIN_DATA);
            } else if (!result.get(0).isActive()) {
                throw new UserException(Constants.USER_INACTIVE);
            } else {
                User user = result.get(0);
                user.setChangePasswordAfterLogin(true);
                getDao().save(user);
                return user.getUserId();
            }
        } else if (!result.get(0).isActive()) {
            throw new UserException(Constants.USER_INACTIVE);
        } else {
            return result.get(0).getUserId();
        }
    }

    public void register(User user) throws UserException {
        if (user == null)
            return;

        user.setPassword(hash(user.getPassword()));
        user.setActivationCode(getHexString(salt(user.getUsername())
                        .getBytes()));

        user.setRegisteredTimestamp(System.currentTimeMillis());

        HtmlEmail email = getPreconfiguredMail();
        try {

            email.addTo(user.getEmail());
            email.setFrom(Settings.getValue("confirmation.email.sender"));
            email.setSubject(Msgs.getString("confirmation.email.subject"));
            email.setHtmlMsg(Msgs.getString("confirmation.email.content",
                    user.getName(), user.getUsername(), user.getRepeatPassword(),
                    Settings.getValue("base.url")
                            + "/users.do?method=activate&code="
                            + user.getActivationCode()));

            email.send();

            getDao().save(user);
        } catch (EmailException eex) {
            log.error("Mail problem", eex);
            throw new UserException("email.problem");
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
            throw new UserException(Constants.USER_ALREADY_ACTIVE);
        }
        throw new UserException(Constants.INVALID_ACTIVATION_CODE);
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

    @SuppressWarnings("unchecked")
    public void remindPassword(String username, String email)
            throws UserException {
        List list = getDao().findByNamedQuery("User.getByUsernameAndEmail",
                new String[] {"username", "email"}, new Object[] {username,
                        email});

        if (list.size() > 0) {
            User user = (User) list.get(0);
            String tempPassword = generateTemporaryPassword();
            user.setTemporaryPassword(hash(tempPassword));

            try {
                HtmlEmail mail = getPreconfiguredMail();
                mail.setFrom(Settings.getValue("temp.password.email.sender"));
                mail.addTo(email);
                mail.setSubject(Msgs.getString("temp.password.email.subject"));
                mail.setHtmlMsg(Msgs.getString("temp.password.email.content",
                        user.getName(), tempPassword));
                mail.send();
                getDao().save(user);
            } catch (EmailException eex) {
                log.error("Mail server not configured", eex);
                throw new UserException("unexpected.problem");
            }
        } else {
            throw new UserException("invalid.username.email.combination");
        }
    }

    public void changePassword(String password, User user) {
        user.setPassword(hash(password));
        user.setTemporaryPassword("");
        getDao().update(user);
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

}