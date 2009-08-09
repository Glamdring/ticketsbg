package com.tickets.controllers.security;

import java.lang.reflect.Field;

import javax.persistence.Id;

import org.springframework.util.ClassUtils;

import com.tickets.controllers.users.LoggedUserHolder;
import com.tickets.dao.Dao;
import com.tickets.model.LogEntry;
import com.tickets.model.User;

public final class ActionLogger {

    public static void logAction(Dao dao, Object entity, DatabaseOperationType dot) {
        if (entity == null) {
            return;
        }
        if (entity.getClass().equals(LogEntry.class)) {
            return;
        }
        User user = LoggedUserHolder.getUser();
        // Log actions only of logged staff users
        if (user != null && user.isStaff()) {
            LogEntry logEntry = new LogEntry();
            logEntry.setUser(user);
            logEntry.setDatabaseOperationType(dot);
            logEntry.setEntityClass(entity.getClass().getName());
            Field[] fields = entity.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Id.class)) {
                    if (ClassUtils.isPrimitiveOrWrapper(field.getType())) {
                        field.setAccessible(true);
                        try {
                            logEntry.setEntityId(((Number) field.get(entity)).longValue());
                        } catch (Exception e) {
                        }
                    }
                }
            }
            dao.persist(logEntry);
        }
    }
}
