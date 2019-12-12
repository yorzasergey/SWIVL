package com.swivl.tz.ui.detail;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class UserDetailModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final String urlProfile;

    public UserDetailModelFactory(String urlProfile) {
        super();
        this.urlProfile = urlProfile;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == UserDetailViewModel.class) {
            return (T) new UserDetailViewModel(urlProfile);
        }
        return null;
    }
}
