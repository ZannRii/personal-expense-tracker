package org.example.personalexpensetracker.entity;

import lombok.ToString;
import org.example.personalexpensetracker.entity.User;
@ToString
public class Session {
    private static User loggedInUser;

    public static void setUser(User user) {
        loggedInUser = user;
    }

    public static User getUser() {
        return loggedInUser;
    }

    public static int getUserId() {
        return loggedInUser != null ? loggedInUser.getUser_id() : -1;
    }

    public static void clear() {
        loggedInUser = null;
    }

}
