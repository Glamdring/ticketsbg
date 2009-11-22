package com.tickets.services;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.tickets.exceptions.UserException;
import com.tickets.mocks.EmailServiceMock;
import com.tickets.model.User;
import com.tickets.test.BaseTest;

import static com.tickets.test.TestUtils.*;

public class UserSerivceTest extends BaseTest {

    @Autowired
    private UserService userService;

    @Test
    public void createInitialUserTest() {
        userService.createInitialUser();
        List<User> users = userService.list(User.class);
        if (users.size() == 0) {
            Assert.fail("User not created");
        }
    }

    @Test
    public void registerAndLoginTest() {

        getUserService().setEmailService(new EmailServiceMock());

        User user = createUser();

        try {
            user = getUserService().register(user);
        } catch (UserException ex) {
            Assert.fail(ex.getMessageKey());
        }

        try {
            attemptLogin(user);
            Assert.fail("Login should fail before validation");
        } catch (UserException ex) {
            if (ex != UserException.USER_INACTIVE) {
                Assert.fail(ex.getMessageKey());
            }
        }

        try {
            getUserService().activateUserWithCode(user.getActivationCode());
        } catch (UserException ex) {
            Assert.fail(ex.getMessageKey());
        }

        try {
            getUserService().activateUserWithCode(user.getActivationCode());
            Assert.fail("Second attempt for activation should fail");
        } catch (UserException ex) {
            if (ex != UserException.USER_ALREADY_ACTIVE) {
                Assert.fail(ex.getMessageKey());
            }
        }

        try {
            attemptLogin(user);
        } catch (UserException ex) {
            Assert.fail(ex.getMessageKey());
        }
    }

    @Test
    public void forgottenPasswordTest() {
        getUserService().setEmailService(new EmailServiceMock());
        User user = createUser();
        try {
            user = getUserService().register(user);
        } catch (UserException ex) {
            Assert.fail(ex.getMessageKey());
        }
        user.setActive(true);

        try {
            getUserService().sendTemporaryPassword(user.getEmail());
        } catch (UserException ex) {
            Assert.fail(ex.getMessageKey());
        }
    }

    private void attemptLogin(User user) throws UserException {
        getUserService().login(user.getUsername(), user.getPassword().toCharArray(), true);
    }

    private UserServiceImpl getUserService() {
        return (UserServiceImpl) userService;
    }

    private User createUser() {
        User user = new User();
        user.setEmail(getRandomEmail());
        String pass = getRandomString(5);
        user.setUsername(getRandomString(5));
        user.setPassword(pass);
        user.setRepeatPassword(pass);
        user.setName(getRandomString(10));
        user.setContactPhone(getRandomString(10));

        return user;
    }
}
