package com.tickets.controllers.security;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import javax.faces.context.FacesContext;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.tickets.annotations.Action;
import com.tickets.controllers.Screen;
import com.tickets.controllers.users.LoggedUserHolder;
import com.tickets.model.User;

@Aspect
@Component("securityAspect")
public class SecurityAspect implements Serializable {

    public SecurityAspect() {
    }

    @Around("execution(* com.tickets.controllers..*.*(..))")
    public Object checkAccessToOperation(ProceedingJoinPoint jp)
            throws Throwable {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Method method = null;
        try {
            method = jp.getTarget().getClass().getDeclaredMethod(
                    jp.getSignature().getName());
        } catch (Exception ex) {
            // No method found. I.e. the method is a setter, or
            // event handler, which are ignored by the
            // security checks

            return jp.proceed();
        }

        Action action = method.getAnnotation(Action.class);
        if (action == null) {
            action = method.getDeclaringClass().getAnnotation(Action.class);
        }

        AccessLevel requiredLevel = AccessLevel.PUBLIC;

        if (action != null) {
            requiredLevel = action.accessLevel();
        }

        if (requiredLevel == AccessLevel.PUBLIC) {
            return jp.proceed();
        }

        User user = LoggedUserHolder.getUser();

        if (user == null) {
            redirectTo(Screen.LOGIN_SCREEN, facesContext);
            return getObjectToReturn(jp);
        }

        if (user.getAccessLevel().getPrivileges() < requiredLevel
                .getPrivileges()) {
            redirectTo(Screen.UNAUTHORIZED, facesContext);
            return getObjectToReturn(jp);
        }

        return jp.proceed();
    }

    /**
     * having an object to return, because in some cases
     * (getter methods) a new empty instance will be created
     * in order to avoid NPEs and IAEs
     *
     * @param jp
     * @return an object to return :)
     */

    private Object getObjectToReturn(ProceedingJoinPoint jp) {
        if (jp.getSignature().getName().startsWith("get")) {
            try {
                Object result = jp.proceed();
                Constructor c = result.getClass().getDeclaredConstructor();
                c.setAccessible(true);
                return c.newInstance();
            } catch (Throwable ex) {
                // failed to create an empty object - use null as return value
            }
        }

        return null;
    }

    private void redirectTo(Screen screen, FacesContext facesContext) {
        facesContext.getApplication().getNavigationHandler().handleNavigation(
                facesContext, null, screen.getOutcome());

        facesContext.renderResponse();

    }
}
