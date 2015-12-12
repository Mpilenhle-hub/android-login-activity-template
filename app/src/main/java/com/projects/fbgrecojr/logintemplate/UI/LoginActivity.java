package com.projects.fbgrecojr.logintemplate.UI;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.projects.fbgrecojr.logintemplate.HTTPManager.HttpManager;
import com.projects.fbgrecojr.logintemplate.HTTPManager.RequestPackage;
import com.projects.fbgrecojr.logintemplate.Parser.JSONParser;
import com.projects.fbgrecojr.logintemplate.R;
import com.projects.fbgrecojr.logintemplate.Session.Session;
import com.projects.fbgrecojr.logintemplate.Structures.User;
import com.projects.fbgrecojr.logintemplate.Utility.UTILITY;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText userName;
    private EditText password;
    private Button login;
    private RelativeLayout image;
    private LinearLayout input, button, belowPic;
    private Animation fadeInImage, fadeInButton, bottomUp, fadeOut;
    private TextInputLayout inputLayoutName,inputLayoutPassword;
    private ViewGroup hiddenPanel;
    private ShimmerFrameLayout container, loggingIn;
    private static final int SECOND = 1000;
    private static final int HALF_SECOND = 500;
    private static final int QUARTER_SECOND = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //INITIALIZE ANIMATION ITEMS
        fadeInImage = new AlphaAnimation(0, 1);
        fadeInButton = new AlphaAnimation(0, 1);
        fadeOut = new AlphaAnimation(1.0f,0.0f);
        bottomUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up_animation);
        fadeInImage.setInterpolator(new AccelerateInterpolator()); //and this
        bottomUp.setInterpolator(new DecelerateInterpolator());
        hiddenPanel = (ViewGroup)findViewById(R.id.input);
        inputLayoutName = (TextInputLayout) findViewById(R.id.text_input_username);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.text_input_password);

        //GET UI ELEMENTS
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        image = (RelativeLayout) findViewById(R.id.image);
        input = (LinearLayout) findViewById(R.id.input);
        button = (LinearLayout) findViewById(R.id.button);
        container = (ShimmerFrameLayout) findViewById(R.id.shimmer);
        belowPic = (LinearLayout) findViewById(R.id.below_picture);
        loggingIn = (com.facebook.shimmer.ShimmerFrameLayout) findViewById(R.id.login_shimmer);

        //SET UI PROPERTIES
        loggingIn.setVisibility(View.INVISIBLE);
        userName.setCursorVisible(false);
        password.setCursorVisible(false);
        password.setHint("Password");
        userName.setHint("Username");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                userName.setCursorVisible(true);
                password.setCursorVisible(true);
                userName.requestFocus();
            }
        }, LoginActivity.SECOND * 3);

        //ANIMATIONS
        fadeInImage.setDuration(SECOND * 3);
        fadeOut.setStartOffset(SECOND);
        fadeOut.setDuration(SECOND);
        image.setAnimation(fadeInImage);
        fadeInButton.setStartOffset(SECOND + HALF_SECOND + QUARTER_SECOND);
        fadeInButton.setDuration(SECOND * 2);
        button.setAnimation(fadeInButton);
        hiddenPanel.startAnimation(bottomUp);
        hiddenPanel.setVisibility(View.VISIBLE);
        container.setDuration(SECOND * 2 + QUARTER_SECOND);
        container.setRepeatDelay(QUARTER_SECOND);
        container.setIntensity((float) 0.15);
        container.setBaseAlpha((float) 0.75);
        container.setFadingEdgeLength(3);
        container.setDropoff((float) 0.40);
        container.startShimmerAnimation();

        //ON CLICK LISTENERS
        login.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:

                if(getUserName().getText().toString().equals("") || getUserName().getText().toString().equals(" ")){
                    inputLayoutName.setError("enter username");
                }else if(getPassword().getText().toString().equals("") || getPassword().getText().toString().equals(" ")){
                    inputLayoutPassword.setError("enter password");
                }else{
                    //webservice
                    if (isOnline()) {
                        RequestPackage p = new RequestPackage();
                        p.setMethod("GET");
                        p.setUri(UTILITY.UBUNTU_SERVER_URL);
                        p.setParam("query", "user");
                        p.setParam("username", getUserName().getText().toString());
                        new WebserviceCallOne().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, p);
                    } else {
                        Toast.makeText(getApplicationContext(), "you are not connected to the internet", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private void animateExit() {
        //fade out annimation
        belowPic.startAnimation(fadeOut);
        belowPic.setVisibility(View.INVISIBLE);
        fadeInImage.setStartOffset(SECOND * 2);
        fadeInImage.setDuration(HALF_SECOND);
        loggingIn.startAnimation(fadeInImage);
        loggingIn.setVisibility(View.VISIBLE);
        loggingIn.setDuration(SECOND);
        loggingIn.startShimmerAnimation();
    }

    /**
     * Check to see whether there is an internet connection or not.
     * @return whether there is an internet connection
     */
    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public EditText getPassword() {
        return password;
    }

    public EditText getUserName() {
        return userName;
    }

    private class WebserviceCallOne extends AsyncTask<RequestPackage, String, User> {
        @Override
        protected User doInBackground(RequestPackage... params) {
            String content = HttpManager.getData(params[0]);
            return JSONParser.parseUserFeed(content);
        }

        @Override
        protected void onPostExecute(User s) {
            Session.setCurrentUser(s);
            //if null, error stacktrace will print to the log. This is expected!!
            if(Session.getCurrentUser() == null){ //username was incorrect
                inputLayoutName.setError("username does not exist");
            }else{ //check password
                if(getPassword().getText().toString().equals(s.getPassword())){ //passwords match
                    animateExit();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //do nothing
                            //just wait while we enjoy the annimation
                            //start intent
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    },
                            LoginActivity.SECOND * 4);
                }else{
                    inputLayoutPassword.setError("password incorrect");
                }
            }
        }
    }


}
