package com.tickets.services;

import static com.tickets.test.TestUtils.getRandomEmail;
import static com.tickets.test.TestUtils.getRandomString;

import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.tickets.exceptions.UserException;
import com.tickets.mocks.EmailServiceMock;
import com.tickets.model.Agent;
import com.tickets.model.Firm;
import com.tickets.model.User;
import com.tickets.test.BaseTest;
import com.tickets.utils.GeneralUtils;

@Ignore
public class UserSerivceTest extends BaseTest {

    @Autowired
    private UserService userService;

    @Before
    public void initialize() {
        getUserService().setEmailService(new EmailServiceMock());
    }

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

        User user = createUser();

        try {
            user = getUserService().register(user);
        } catch (UserException ex) {
            Assert.fail(ex.getMessageKey());
        }

        try {
            attemptLogin(user.getUsername(), user.getPassword());
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
            attemptLogin(user.getUsername(), user.getPassword());
        } catch (UserException ex) {
            Assert.fail(ex.getMessageKey());
        }
    }

    @Test
    public void forgottenPasswordTest() {

        User user = createAndRegisterUser();

        try {
            getUserService().sendTemporaryPassword(user.getEmail());
        } catch (UserException ex) {
            Assert.fail(ex.getMessageKey());
        }
        user = getUserService().get(User.class, user.getId());

        try {
            attemptLogin(user.getUsername(), user.getTemporaryPassword());
        } catch (UserException ex) {
            Assert.fail(ex.getMessageKey());
        }

        getUserService().changePassword(user, getRandomString(5));
    }

    @Test
    public void clearNonActivatedUsersTest() {
        List<User> users = getUserService().list(User.class);
        int usersCountBefore = users.size();

        User user = createAndRegisterUser(); // create, but not activate
        Calendar twoDaysAgo = GeneralUtils.createCalendar();
        twoDaysAgo.add(Calendar.DAY_OF_YEAR, -2);
        user.setRegisteredTimestamp(twoDaysAgo.getTimeInMillis());
        user.setActive(false);

        getUserService().save(user);

        getUserService().clearNonActivatedUsers();

        users = getUserService().list(User.class);

        int usersCountAfter = users.size();

        Assert.assertNotSame(usersCountBefore + 1, usersCountAfter);
    }

    @Test
    public void emailExistsTest() {
        User user = createAndRegisterUser();
        Assert.assertTrue(getUserService().emailExists(user.getEmail()));
    }

    @Test
    public void getUsersByFirmTest() {
        Firm firm = createFirm();
        User user = createAndRegisterUser();
        user.setFirm(firm);
        user = getUserService().save(user);

        List<User> users = getUserService().fetchUsers(firm);
        for (User cUser : users) {
            if (cUser.getId() == user.getId()) {
                // if found, return successfully
                return;
            }
        }
        // if not found, fail
        Assert.fail("Newly registered user not found in the firm's users list");
    }

    @Test
    public void getUsersByAgentTest() {

        Firm firm = createFirm();
        Agent agent = new Agent();
        agent.setActive(true);
        agent.setName(getRandomString(5));
        agent.setFirm(firm);
        agent = (Agent) getUserService().saveObject(agent);

        User user = createAndRegisterUser();
        user.setAgent(agent);
        user.setFirm(firm);
        user = getUserService().save(user);

        List<User> users = getUserService().fetchAgentsUsers(firm);

        for (User cUser : users) {
            if (cUser.getId() == user.getId()) {
                // if found, return successfully
                return;
            }
        }
        // if not found, fail
        Assert
                .fail("Newly registered user not found in the firm's agents' users list");
    }

    private void attemptLogin(String username, String password)
            throws UserException {
        getUserService().login(username, password.toCharArray(), true);
    }

    private UserServiceImpl getUserService() {
        return (UserServiceImpl) userService;
    }

    private User createAndRegisterUser() {
        User user = createUser();
        try {
            user = getUserService().register(user);
        } catch (UserException ex) {
            Assert.fail();
        }

        user.setActive(true);
        user = getUserService().save(user);

        return user;
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

    private Firm createFirm() {
        Firm firm = new Firm();
        firm.setActive(true);
        firm.setName(getRandomString(5));
        firm = (Firm) getUserService().saveObject(firm);
        return firm;
    }
}
