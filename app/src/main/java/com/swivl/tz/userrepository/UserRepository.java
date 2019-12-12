package com.swivl.tz.userrepository;

import com.swivl.tz.model.User;
import com.swivl.tz.model.UserDetail;

import java.util.List;

import io.reactivex.Single;

public interface UserRepository {

    Single<List<User>> getUsers();

    Single<List<User>> getUsers(int pageNumber);

    Single<UserDetail> getUserFrofile(String urlPath);
}
