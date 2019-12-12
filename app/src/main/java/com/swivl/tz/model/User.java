package com.swivl.tz.model;

public class User extends UserListItem{

    private final String login;
    private final int id;
    private final String urlProfile;
    private final String avatarUrl;
    private final String followersUrl;
    private final String gistsUrl;
    private final String reposUrl;

    private User(Builder builder) {

        login = builder.login;
        urlProfile = builder.urlProfile;
        id = builder.id;
        avatarUrl = builder.avatarUrl;
        followersUrl = builder.followersUrl;
        gistsUrl = builder.gistsUrl;
        reposUrl = builder.reposUrl;
    }

    @Override
    public UserListItemTypes getListItemType() {
        return UserListItemTypes.TYPE_USER_DONE;
    }

    public String getLogin() {
        return login;
    }

    public int getId() {
        return id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getFollowersUrl() {
        return followersUrl;
    }

    public String getGistsUrl() {
        return gistsUrl;
    }

    public String getReposUrl() {
        return reposUrl;
    }

    public String getUrlProfile() {
        return urlProfile;
    }

    public static class Builder {

        private String login;
        private int id;
        private String urlProfile;
        private String avatarUrl;
        private String followersUrl;
        private String gistsUrl;
        private String reposUrl;

        public Builder() {

        }

        public Builder setLogin(String login) {
            this.login = login;
            return this;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setUrlProfile(String urlProfile) {
            this.urlProfile = urlProfile;
            return this;
        }

        public Builder setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }

        public Builder setFollowersUrl(String followersUrl) {
            this.followersUrl = followersUrl;
            return this;
        }

        public Builder setGistsUrl(String gistsUrl) {
            this.gistsUrl = gistsUrl;
            return this;
        }

        public Builder setReposUrl(String reposUrl) {
            this.reposUrl = reposUrl;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
