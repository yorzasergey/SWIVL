package com.swivl.tz.model;

public class UserErrorItem extends UserListItem{

    public UserErrorItem(){

    }

    @Override
    public UserListItemTypes getListItemType() {
        return UserListItemTypes.TYPE_USER_ERROR;
    }
}
