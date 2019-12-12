package com.swivl.tz.ui.detail;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.swivl.tz.model.User;
import com.swivl.tz.model.UserDetail;
import com.swivl.tz.userrepository.UserRepository;
import com.swivl.tz.userrepository.UserRepositoryProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class UserDetailViewModel extends ViewModel {

    private UserRepository userRepository;
    private CompositeDisposable disposable;

    private MutableLiveData<UserDetail> userDetail = new MutableLiveData<>();
    private MutableLiveData<Boolean> loadError = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public UserDetailViewModel(String urlProfile){

        userRepository = UserRepositoryProvider.provideDataRepository();
        disposable = new CompositeDisposable();

        fetchUserProfile(urlProfile);
    }

    private void fetchUserProfile(String url){

        loadError.setValue(false);
        loading.setValue(true);

        disposable.add(userRepository.getUserFrofile(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<UserDetail>() {
                    @Override
                    public void onSuccess(UserDetail value) {
                        userDetail.setValue(value);
                        loading.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadError.setValue(true);
                        loading.setValue(false);
                    }
                }));
    }

    public LiveData<UserDetail> getUserDetail(){
        return userDetail;
    }

    public LiveData<Boolean> getLoadError(){
        return loadError;
    }

    public LiveData<Boolean> getLoading(){
        return loading;
    }

    public void onClickRetryLoadingProfile(String urlProfile){

        fetchUserProfile(urlProfile);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null) {
            disposable.clear();
            disposable = null;
        }
    }
}
