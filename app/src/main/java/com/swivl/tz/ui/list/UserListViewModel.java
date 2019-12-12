package com.swivl.tz.ui.list;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.swivl.tz.model.User;
import com.swivl.tz.model.UserErrorItem;
import com.swivl.tz.model.UserListItem;
import com.swivl.tz.model.UserLoadingItem;
import com.swivl.tz.userrepository.UserRepository;
import com.swivl.tz.userrepository.UserRepositoryProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class UserListViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private CompositeDisposable disposable;
    private int since;
    private List<UserListItem> usersList = new ArrayList<>();

    private MutableLiveData<List<UserListItem>> users = new MutableLiveData<>();
    private MutableLiveData<Boolean> loadError = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public UserListViewModel(Application application){
        super(application);

        userRepository = UserRepositoryProvider.provideDataRepository();
        disposable = new CompositeDisposable();

        //usersList.add(new UserLoadingItem());
        //users.setValue(usersList);

        fetchUsers();
    }

    private void fetchUsers(){

        loadError.setValue(false);
        loading.setValue(true);

        disposable.add(userRepository.getUsers(since)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<User>>() {
                    @Override
                    public void onSuccess(List<User> value) {

                        since = value.get(value.size()-1).getId();

                        if(usersList.size() > 0){
                            usersList.addAll(usersList.size() - 1, value);
                            usersList.set(usersList.size() - 1, new UserLoadingItem());
                        } else {
                            usersList.addAll(value);
                            usersList.add(new UserLoadingItem());
                        }

                        users.setValue(usersList);

                        loading.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        /*if(usersList.size() > 0){
                            usersList.set(usersList.size() - 1, new UserErrorItem());
                        } else {
                            usersList.add(new UserErrorItem());
                        }*/

                        loadError.setValue(true);
                        loading.setValue(false);
                    }
                }));
    }

    public LiveData<Boolean> getLoadError(){
        return loadError;
    }

    public LiveData<Boolean> getLoading(){
        return loading;
    }

    public void onLoadMore(){

        fetchUsers();
    }

    public void onReload(){

        since = 0;
        usersList.clear();
        fetchUsers();
    }

    public LiveData<List<UserListItem>> getUsers(){
        return users;
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
