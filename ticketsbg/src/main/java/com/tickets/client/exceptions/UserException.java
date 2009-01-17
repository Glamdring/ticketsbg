package com.tickets.client.exceptions;


public class UserException extends Exception {

    public static final int EMAIL_PROBLEM_ID = 1;

    public static final UserException EMAIL_PROBLEM
        = new UserException(UserException.EMAIL_PROBLEM_ID) ;


    private static final int INCORRECT_LOGIN_DATA_ID = 2;

    public static final UserException INCORRECT_LOGIN_DATA
        = new UserException(UserException.INCORRECT_LOGIN_DATA_ID);


    private static final int USER_INACTIVE_ID = 3;

    public static final UserException USER_INACTIVE
        = new UserException(UserException.USER_INACTIVE_ID);


    private static final int USER_ALREADY_ACTIVE_ID = 4;

    public static final UserException USER_ALREADY_ACTIVE
        = new UserException(USER_ALREADY_ACTIVE_ID);


    private static final int INVALID_ACTIVATION_CODE_ID = 5;

    public static final UserException INVALID_ACTIVATION_CODE
        = new UserException(INVALID_ACTIVATION_CODE_ID);



    private static final int UNEXPECTED_PROBLEM_ID = 6;

    public static final UserException UNEXPECTED_PROBLEM
        = new UserException(UNEXPECTED_PROBLEM_ID);


    private static final int INVALID_EMAIL_USER_COMBINATION_ID = 7;

    public static final UserException INVALID_EMAIL_USER_COMBINATION
        = new UserException(INVALID_EMAIL_USER_COMBINATION_ID);


    private int id;

    public UserException() {

    }

    public UserException(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        final UserException other = (UserException) obj;
        if (id != other.id)
            return false;
        return true;
    }



}
