package com.swivl.tz.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.swivl.tz.MainActivity;
import com.swivl.tz.R;
import com.swivl.tz.model.User;
import com.swivl.tz.model.UserDetail;
import com.swivl.tz.ui.OnChageActionBarTitle;
import com.swivl.tz.ui.list.UserListFragment;
import com.swivl.tz.ui.list.UserListViewModel;
import com.swivl.tz.ui.list.UsersAdapter;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UserDetailFragment extends Fragment {

    private static String URL_PROFILE_KEY = "url_profile_key";
    private static String URL_AVATAR_KEY = "url_avatar_key";

    private UserDetailViewModel viewModel;
    private Unbinder unbinder;
    private String urlProfile, urlAvatar;

    @BindView(R.id.avatarImageView)
    AppCompatImageView avatarImageView;

    @BindView(R.id.nameTextView)
    TextView nameTextView;

    @BindView(R.id.urlTextView)
    TextView urlTextView;

    @BindView(R.id.reposCountTextView)
    TextView reposCountTextView;

    @BindView(R.id.gistsCountTextView)
    TextView gistsCountTextView;

    @BindView(R.id.followersCountTextView)
    TextView followersCountTextView;

    @BindView(R.id.retryLoading)
    TextView retryLoading;

    @BindView(R.id.itemProgressBar)
    ProgressBar itemProgressBar;

    @BindString(R.string.url_open_error_message)
    String urlOpenErrorMessage;

    @OnClick(R.id.urlTextView) void onClickUrlProfile(){

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlTextView.getText().toString()));

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), urlOpenErrorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.retryLoading) void onClickRetryLoadingProfile(){

        viewModel.onClickRetryLoadingProfile(urlProfile);
    }

    private OnChageActionBarTitle interactionListener;

    public static UserDetailFragment newInstance(String urlProfile, String urlAvatar) {

        UserDetailFragment fragment = new UserDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putString(URL_PROFILE_KEY, urlProfile);
        bundle.putString(URL_AVATAR_KEY, urlAvatar);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        urlProfile = getArguments().getString(URL_PROFILE_KEY);
        urlAvatar = getArguments().getString(URL_AVATAR_KEY);

        viewModel = ViewModelProviders.of(this, new UserDetailModelFactory(urlProfile)).get(UserDetailViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_detail_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        if(interactionListener != null) interactionListener.onChangeActionBarTitle("");

        ViewCompat.setTransitionName(avatarImageView, "avatarImage");

        Glide.with(this)
                .load(urlAvatar)
                .apply(RequestOptions.circleCropTransform())
                .into(avatarImageView);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel.getUserDetail().observe(this, new Observer<UserDetail>() {
            @Override
            public void onChanged(UserDetail userDetail) {

                if(interactionListener != null) interactionListener.onChangeActionBarTitle(userDetail.getName());

                nameTextView.setText(userDetail.getName());
                urlTextView.setText(userDetail.getUrlProfile());
                reposCountTextView.setText(String.valueOf(userDetail.getRepos()));
                gistsCountTextView.setText(String.valueOf(userDetail.getGists()));
                followersCountTextView.setText(String.valueOf(userDetail.getFollowers()));
            }
        });

        viewModel.getLoadError().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean error) {

                if(error){
                    retryLoading.setVisibility(View.VISIBLE);
                } else {
                    retryLoading.setVisibility(View.GONE);
                }
            }
        });

        viewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loading) {

                if(loading){
                    itemProgressBar.setIndeterminate(true);
                    itemProgressBar.setVisibility(View.VISIBLE);
                } else {
                    itemProgressBar.setIndeterminate(false);
                    itemProgressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
