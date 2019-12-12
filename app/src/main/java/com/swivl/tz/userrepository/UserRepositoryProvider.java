package com.swivl.tz.userrepository;

import androidx.annotation.NonNull;

public final class UserRepositoryProvider {

    private static volatile UserRepository userRepository;

    @NonNull
    public static UserRepository provideDataRepository(){

        UserRepository repository = userRepository;

        if(repository == null){
            synchronized (UserRepositoryProvider.class){
                repository = userRepository;
                if(repository == null){
                    repository = userRepository = new UserRepositoryHandler();
                }
            }
        }

        return repository;
    }


    public static void destroy(){
        userRepository = null;
    }
}
