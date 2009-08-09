package com.tickets.controllers.security;

import java.io.Serializable;
import java.lang.reflect.Method;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.tickets.annotations.Action;
import com.tickets.controllers.Screen;
import com.tickets.controllers.users.LoggedUserHolder;
import com.tickets.model.User;

@Aspect
@Component("securityAspect")
public class SecurityAspect implements Serializable {

    private static final String LOGGED_UESR_HOLDER_KEY = "loggedUserHolder";

    public SecurityAspect() {
    }

    @Before("execution(* com.tickets.controllers..*.*(..))")
    public void checkAccessToOperation(JoinPoint jp) throws Throwable {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        Method method = null;
        try {
            method = jp.getTarget().getClass().getDeclaredMethod(
                    jp.getSignature().getName());
        } catch (Exception ex) {
            // No method found. I.e. the method is a setter, or
            // event handler, which are ignored by the
            // security checks

            return;
        }

        Action action = method.getAnnotation(Action.class);
        if (action == null) {
            action = method.getDeclaringClass().getAnnotation(
                    Action.class);
        }

        AccessLevel requiredLevel = AccessLevel.PUBLIC;

        if (action != null) {
            requiredLevel = action.accessLevel();
        }

        if (requiredLevel == AccessLevel.PUBLIC) {
            return;
        }

        HttpSession session = (HttpSession) facesContext.getExternalContext()
                .getSession(true);

        LoggedUserHolder loggedUserHolder = (LoggedUserHolder) session
                .getAttribute(LOGGED_UESR_HOLDER_KEY);
        if (loggedUserHolder == null) {
            redirectTo(Screen.LOGIN_SCREEN, facesContext);
            return;
        }

        User user = loggedUserHolder.getLoggedUser();

        if (user == null) {
            redirectTo(Screen.LOGIN_SCREEN, facesContext);
            return;
        }

        if (user.getAccessLevel().getValue() < requiredLevel.getValue()) {
            redirectTo(Screen.UNAUTHORIZED, facesContext);
        }

    }

    private void redirectTo(Screen screen, FacesContext facesContext) {
        facesContext.getApplication().getNavigationHandler().handleNavigation(
                facesContext, null, screen.getOutcome());

        facesContext.renderResponse();

    }
}
