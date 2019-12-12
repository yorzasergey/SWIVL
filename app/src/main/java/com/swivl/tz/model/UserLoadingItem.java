package com.swivl.tz.model;

public class UserLoadingItem extends UserListItem{

    public UserLoadingItem(){

    }

    @Override
    public UserListItemTypes getListItemType() {
        return UserListItemTypes.TYPE_USER_LOADING;
    }
}
