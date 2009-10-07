package com.tickets.exceptions;

/**
 * Thrown when the creation of a ticket fails
 *
 * @author Bozhidar Bozhanov
 *
 */
public class TicketCreationException extends Exception {

    private String messageKey;
    private boolean redoSearch;
    private Object[] arguments;

    public TicketCreationException(String messageKey) {
        super();
        this.messageKey = messageKey;
    }

    public TicketCreationException(String messageKey, boolean redoSearch) {
        super();
        this.messageKey = messageKey;
        this.redoSearch = redoSearch;
    }

    public TicketCreationException(String messageKey, Object[] arguments) {
        super();
        this.messageKey = messageKey;
        this.arguments = arguments;
    }

    public TicketCreationException(String messageKey, boolean redoSearch,
            Object[] arguments) {
        super();
        this.messageKey = messageKey;
        this.redoSearch = redoSearch;
        this.arguments = arguments;
    }

    public String getMessageKey() {
        return messageKey;
    }
    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }
    public boolean isRedoSearch() {
        return redoSearch;
    }
    public void setRedoSearch(boolean redoSearch) {
        this.redoSearch = redoSearch;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

}
