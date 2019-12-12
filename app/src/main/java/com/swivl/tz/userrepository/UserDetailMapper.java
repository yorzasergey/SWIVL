package com.swivl.tz.userrepository;

import com.swivl.tz.BaseMapper;
import com.swivl.tz.model.User;
import com.swivl.tz.model.UserDetail;
import com.swivl.tz.networkmodel.UserDetailResponse;
import com.swivl.tz.networkmodel.UserResponse;

import java.util.ArrayList;
import java.util.List;

public class UserDetailMapper implements BaseMapper<UserDetailResponse, UserDetail> {

    @Override
    public UserDetail mapFrom(UserDetailResponse item) {

        UserDetail userDetail = new UserDetail.Builder()
                    .setName(item.getName())
                    .setFollowers(item.getFollowers())
                    .setGists(item.getPublicGists())
                    .setRepos(item.getPublicRepos())
                    .setUrlProfile(item.getHtmlUrl())
                    .build();

        return userDetail;
    }
}
