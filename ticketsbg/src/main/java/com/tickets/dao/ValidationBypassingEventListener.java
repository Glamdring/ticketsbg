package com.tickets.dao;

import org.hibernate.event.PreInsertEvent;
import org.hibernate.event.PreUpdateEvent;
import org.hibernate.validator.event.ValidateEventListener;

/**
 * Extension of Hibernate's <code>ValidateEventListener</code> that allows you
 * to bypass the normal validation performed by Hibernate for a specific thread.
 *
 */
public class ValidationBypassingEventListener extends ValidateEventListener {
    private static ThreadLocal<Boolean> shouldValidateThreadLocal = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return Boolean.TRUE;
        }
    };

    /**
     * Perform validation before insert, <code>unless</code>
     * {@link #turnValidationOff()} has been called for the currently executing
     * thread.
     *
     * @param event
     *            the PreInsertEvent
     * @return Return true if the operation should be vetoed
     */
    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        return isCurrentlyValidating() && super.onPreInsert(event);
    }

    /**
     * Perform validation before update, <code>unless</code>
     * {@link #turnValidationOff()} has been called for the currently executing
     * thread.
     *
     * @param event
     *            the PreUpdateEvent
     * @return Return true if the operation should be vetoed
     */
    @Override
    public boolean onPreUpdate(PreUpdateEvent event) {
        return isCurrentlyValidating() && super.onPreUpdate(event);
    }

    /**
     * Call this method to explicitly turn validation on for the currently
     * executing thread.
     */
    public static void turnValidationOn() {
        ValidationBypassingEventListener.shouldValidateThreadLocal
                .set(Boolean.TRUE);
    }

    /**
     * Call this method to bypass validation for the currently executing thread.
     */
    public static void turnValidationOff() {
        ValidationBypassingEventListener.shouldValidateThreadLocal
                .set(Boolean.FALSE);
    }

    /** @return <code>true</code> if we need to validate for the current thread */
    public static Boolean isCurrentlyValidating() {
        return ValidationBypassingEventListener.shouldValidateThreadLocal.get();
    }
}
