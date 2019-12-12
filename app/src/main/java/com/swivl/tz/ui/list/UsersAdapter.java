package com.swivl.tz.ui.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.swivl.tz.R;
import com.swivl.tz.model.User;
import com.swivl.tz.model.UserErrorItem;
import com.swivl.tz.model.UserListItem;
import com.swivl.tz.model.UserLoadingItem;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UserListItem> usersList = new ArrayList<>();
    private OnClickListener onClickListener;
    private Context context;

    public interface OnClickListener{

        public void onItemClick(View view, int position, User user);
        public void onItemClickReload();
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }
    public void removeOnClickListener(){
        onClickListener = null;
    }

    public UsersAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return usersList.get(position).getListItemType().getValue();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == UserListItem.UserListItemTypes.TYPE_USER_DONE.getValue()) {
            return new UserRecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false));
        } else if (viewType == UserListItem.UserListItemTypes.TYPE_USER_LOADING.getValue()) {
            return new LoadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
        } else if(viewType == UserListItem.UserListItemTypes.TYPE_USER_ERROR.getValue()){
            return new ErrorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading_error, parent, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position) == UserListItem.UserListItemTypes.TYPE_USER_DONE.getValue()){

            User user = (User) usersList.get(position);

            UserRecyclerViewHolder userViewHolder = (UserRecyclerViewHolder) holder;

            userViewHolder.userNameTextView.setText(user.getLogin());

            Glide.with(context)
                    .load(user.getAvatarUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(R.drawable.ic_avatar_def)
                    .into(userViewHolder.userImageView);

            ViewCompat.setTransitionName(userViewHolder.userImageView, String.valueOf(position) + "_image");

            userViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onItemClick(view, position, user);
                }
            });

        } else if(getItemViewType(position) == UserListItem.UserListItemTypes.TYPE_USER_LOADING.getValue()){

            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.itemProgressBar.setIndeterminate(true);

        } else if(getItemViewType(position) == UserListItem.UserListItemTypes.TYPE_USER_ERROR.getValue()){

            ErrorViewHolder errorViewHolder = (ErrorViewHolder) holder;
            errorViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onItemClickReload();
                }
            });;
        }
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public void setData(List<UserListItem> usersList) {

        this.usersList.clear();

        if(usersList != null){
            this.usersList.addAll(usersList);
        }

        notifyDataSetChanged();
    }

    public void setUserError(){

        if(usersList.size() > 0){
            usersList.set(usersList.size() - 1, new UserErrorItem());
            notifyItemChanged(usersList.size() - 1);
        } else {
            usersList.add(new UserErrorItem());
            notifyItemInserted(usersList.size() - 1);
        }
    }

    public void setUserLoading(){

        if(usersList.size() > 0){
            usersList.set(usersList.size() - 1, new UserLoadingItem());
            notifyItemChanged(usersList.size() - 1);
        } else {
            usersList.add(new UserLoadingItem());
            notifyItemInserted(usersList.size() - 1);
        }
    }

    static class UserRecyclerViewHolder extends RecyclerView.ViewHolder {

        public AppCompatImageView userImageView;
        public TextView userNameTextView;

        UserRecyclerViewHolder(View view) {
            super(view);

            userImageView = (AppCompatImageView) view.findViewById(R.id.userImageView);
            userNameTextView = (TextView) view.findViewById(R.id.userNameTextView);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar itemProgressBar;

        LoadingViewHolder(View view) {
            super(view);

            itemProgressBar = (ProgressBar) view.findViewById(R.id.itemProgressBar);
        }
    }

    static class ErrorViewHolder extends RecyclerView.ViewHolder {

        public TextView retryLoading;

        ErrorViewHolder(View view) {
            super(view);

            retryLoading = (TextView) view.findViewById(R.id.retryLoading);
        }
    }
}
