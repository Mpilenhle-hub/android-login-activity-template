package com.projects.fbgrecojr.logintemplate.Utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by fbgrecojr on 11/21/15.
 */
public class UTILITY {

    public static final String UBUNTU_SERVER_URL ="http://ec2-54-88-199-143.compute-1.amazonaws.com/panther_park/scripts.php";

    /**
     * Check to see whether there is an internet connection or not.
     * @return whether there is an internet connection
     */
    public static boolean isOnline(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
