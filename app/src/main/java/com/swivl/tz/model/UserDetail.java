package com.swivl.tz.model;

public class UserDetail {

    private final String name;
    private final int followers;
    private final int gists;
    private final int repos;
    private final String urlProfile;

    private UserDetail(Builder builder) {

        name = builder.name;
        followers = builder.followers;
        gists = builder.gists;
        repos = builder.repos;
        urlProfile = builder.urlProfile;
    }

    public String getName() {
        return name;
    }

    public int getFollowers() {
        return followers;
    }

    public int getGists() {
        return gists;
    }

    public int getRepos() {
        return repos;
    }

    public String getUrlProfile() {
        return urlProfile;
    }

    public static class Builder {

        private String name;
        private int followers;
        private int gists;
        private int repos;
        private String urlProfile;

        public Builder() {

        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setFollowers(int followers) {
            this.followers = followers;
            return this;
        }

        public Builder setGists(int gists) {
            this.gists = gists;
            return this;
        }

        public Builder setRepos(int repos) {
            this.repos = repos;
            return this;
        }

        public Builder setUrlProfile(String urlProfile) {
            this.urlProfile = urlProfile;
            return this;
        }

        public UserDetail build() {
            return new UserDetail(this);
        }
    }
}
