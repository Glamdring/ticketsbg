package com.tickets.exceptions;

import java.util.LinkedList;
import java.util.List;

public class GeneralException extends Exception {

    private List<String> messageKeys = new LinkedList<String>();

    public GeneralException() {

    }

    public GeneralException(String message) {
        messageKeys.add(message);
    }

    public void addMessageKey(String messageKey) {
        messageKeys.add(messageKey);
    }

    public List<String> getMessageKeys() {
        return messageKeys;
    }

    public String getMessageKey() {
        if (messageKeys.size() > 0) {
            return messageKeys.get(0);
        }

        String key = "";
        for (String msgKey : messageKeys) {
            key += msgKey + "; ";
        }

        return key;
    }
}
