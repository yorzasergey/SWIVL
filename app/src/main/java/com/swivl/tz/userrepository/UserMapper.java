package com.swivl.tz.userrepository;

import com.swivl.tz.BaseMapper;
import com.swivl.tz.model.User;
import com.swivl.tz.networkmodel.UserResponse;

import java.util.ArrayList;
import java.util.List;

public class UserMapper implements BaseMapper<List<UserResponse>, List<User>> {

    @Override
    public List<User> mapFrom(List<UserResponse> userResponseList) {

        List<User> userList = new ArrayList<>();

        for (UserResponse item : userResponseList) {

            User user = new User.Builder()
                    .setLogin(item.getLogin())
                    .setId(item.getId())
                    .setUrlProfile(item.getUrl())
                    .setAvatarUrl(item.getAvatarUrl())
                    .setFollowersUrl(item.getFollowersUrl())
                    .setGistsUrl(item.getGistsUrl())
                    .setReposUrl(item.getReposUrl())
                    .build();

            userList.add(user);
        }

        return userList;
    }
}
