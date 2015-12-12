package com.projects.fbgrecojr.logintemplate.Session;


import com.projects.fbgrecojr.logintemplate.Structures.User;

/**
 * Created by fbgrecojr on 11/14/15.
 */
public class Session {

    //holds the current User Objects. It can be accessed through the entire project by its static getter/setter
    private static User currentUser = new User();

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        Session.currentUser = currentUser;
    }
}
