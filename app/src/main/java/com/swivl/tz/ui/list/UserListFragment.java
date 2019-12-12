package com.swivl.tz.ui.list;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.transition.Fade;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swivl.tz.R;
import com.swivl.tz.model.User;
import com.swivl.tz.model.UserListItem;
import com.swivl.tz.ui.DetailsTransition;
import com.swivl.tz.ui.OnChageActionBarTitle;
import com.swivl.tz.ui.detail.UserDetailFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UserListFragment extends Fragment {

    private UserListViewModel viewModel;
    private UsersAdapter usersAdapter;
    private Unbinder unbinder;
    private boolean isLoading, isErrorLoading;

    @BindView(R.id.usersSwipeContainer)
    SwipeRefreshLayout usersSwipeContainer;

    @BindView(R.id.usersRecyclerView)
    RecyclerView usersRecyclerView;

    @BindString(R.string.app_name)
    String appName;

    private OnChageActionBarTitle interactionListener;

    UsersAdapter.OnClickListener userAdapterOnClickListener = new UsersAdapter.OnClickListener() {
        @Override
        public void onItemClick(View view, int position, User user) {

            UserDetailFragment details = UserDetailFragment.newInstance(user.getUrlProfile(), user.getAvatarUrl());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                details.setSharedElementEnterTransition(new DetailsTransition());
                details.setEnterTransition(new Fade());
                setExitTransition(new Fade());
                details.setSharedElementReturnTransition(new DetailsTransition());
            }

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .addSharedElement(view.findViewById(R.id.userImageView), "avatarImage")
                    .replace(R.id.container, details)
                    .addToBackStack(null)
                    .commit();
        }

        @Override
        public void onItemClickReload() {

            usersAdapter.setUserLoading();
            viewModel.onLoadMore();
        }
    };

    public static UserListFragment newInstance() {
        return new UserListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_list_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        usersAdapter = new UsersAdapter(getContext());
        usersAdapter.setOnClickListener(userAdapterOnClickListener);

        usersRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        usersRecyclerView.setLayoutManager(layoutManager);
        usersRecyclerView.setItemAnimator(new DefaultItemAnimator());

        usersRecyclerView.setAdapter(usersAdapter);

        usersRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private int visibleThreshold = 5;
            private int lastVisibleItem, totalItemCount;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (!isErrorLoading && !isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    viewModel.onLoadMore();
                }
            }
        });

        usersSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                usersAdapter.setData(new ArrayList<>());
                viewModel.onReload();
                usersSwipeContainer.setRefreshing(true);
            }
        });

        usersSwipeContainer.setRefreshing(true);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(interactionListener != null) interactionListener.onChangeActionBarTitle(appName);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(UserListViewModel.class);

        viewModel.getUsers().observe(this, new Observer<List<UserListItem>>() {
            @Override
            public void onChanged(List<UserListItem> users) {
                usersAdapter.setData(users);
            }
        });

        viewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loading) {

                isLoading = loading;

                if(!isLoading) usersSwipeContainer.setRefreshing(false);
            }
        });

        viewModel.getLoadError().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isError) {

                isErrorLoading = isError;

                if(isErrorLoading) usersAdapter.setUserError();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        usersAdapter.removeOnClickListener();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChageActionBarTitle) {
            interactionListener = (OnChageActionBarTitle) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChageActionBarTitle");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
    }
}
