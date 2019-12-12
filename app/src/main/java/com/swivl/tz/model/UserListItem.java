package com.swivl.tz.model;

public abstract class UserListItem {

    abstract public UserListItemTypes getListItemType();

    public enum UserListItemTypes {

        TYPE_USER_DONE(0),
        TYPE_USER_ERROR(1),
        TYPE_USER_LOADING(2);

        private int value;

        UserListItemTypes(int value){
            this.value = value;
        }

        public int getValue(){
            return value;
        }
    }
}
