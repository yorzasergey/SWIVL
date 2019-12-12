package com.swivl.tz.userrepository;

import com.google.gson.JsonArray;
import com.swivl.tz.BaseMapper;
import com.swivl.tz.BuildConfig;
import com.swivl.tz.model.User;
import com.swivl.tz.model.UserDetail;
import com.swivl.tz.networkapi.ApiServiceFactory;
import com.swivl.tz.networkmodel.UserDetailResponse;
import com.swivl.tz.networkmodel.UserResponse;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class UserRepositoryHandler implements UserRepository {

    private BaseMapper<List<UserResponse>, List<User>> userMapper;
    private BaseMapper<UserDetailResponse, UserDetail> userDetailMapper;

    public UserRepositoryHandler() {

        userMapper = new UserMapper();
        userDetailMapper = new UserDetailMapper();
    }

    @Override
    public Single<List<User>> getUsers() {
        return getUsers(0);
    }

    @Override
    public Single<List<User>> getUsers(int pageNumber) {

        return ApiServiceFactory.getRetrofitApiService().getUsers(String.valueOf(pageNumber))
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<List<UserResponse>, SingleSource<? extends List<User>>>() {
                    @Override
                    public SingleSource<? extends List<User>> apply(List<UserResponse> userResponses) throws Exception {
                        return Single.just(userMapper.mapFrom(userResponses));
                    }
                })
                .onErrorResumeNext(new Function<Throwable, SingleSource<? extends List<User>>>() {
                    @Override
                    public SingleSource<? extends List<User>> apply(Throwable throwable) throws Exception {
                        return Single.error(throwable);
                    }
                });
    }

    @Override
    public Single<UserDetail> getUserFrofile(String urlPath) {

        return ApiServiceFactory.getRetrofitApiService().getUserProfile(urlPath)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<UserDetailResponse, SingleSource<? extends UserDetail>>() {
                    @Override
                    public SingleSource<? extends UserDetail> apply(UserDetailResponse userDetailResponse) throws Exception {
                        return Single.just(userDetailMapper.mapFrom(userDetailResponse));
                    }
                })
                .onErrorResumeNext(new Function<Throwable, SingleSource<? extends UserDetail>>() {
                    @Override
                    public SingleSource<? extends UserDetail> apply(Throwable throwable) throws Exception {
                        return Single.error(throwable);
                    }
                });
    }
}