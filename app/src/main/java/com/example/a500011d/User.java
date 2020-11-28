package com.example.a500011d;

import android.content.Context;

import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;


public class User {
    private String username;
    private String password;
    @Nullable
    public String userImagePath;

    public User() {
        // necessary to pull data from firebase
    }

    private User(UserBuilder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.userImagePath = builder.userImagePath;
    }

    public User(String username, String password, @Nullable String userImagePath) {
        this.username = username;
        this.password = password;
        this.userImagePath = userImagePath;
    }

    public String getUsername() { return this.username; }

    public String getPassword() { return this.password; }

    public boolean validate(String input) { return input.equals(this.password); }

    public void updatePassword(String password) {
        this.password = password;
    }

    static class UserBuilder {
        private String username;
        private String password;
        @Nullable
        public String userImagePath;

        UserBuilder() {
        }

        public UserBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder setUserImagePath(String userImagePath) {
            this.userImagePath = userImagePath;
            return this;
        }

        public User build() {
            return new User(this);
        }

    }

}
