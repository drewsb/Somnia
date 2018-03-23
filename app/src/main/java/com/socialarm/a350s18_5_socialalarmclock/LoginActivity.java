package com.socialarm.a350s18_5_socialalarmclock;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

/**
 * A login screen that offers login via facebook email/password.
 */
public class LoginActivity extends AppCompatActivity {

    public final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = "LoginActivity";

    // UI references.
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private static final String[] permissions = new String[]{"public_profile", "email", "user_friends"};
    private final UserInfo userInfo = new UserInfo(LoginActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(permissions);

        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
        if (loggedIn) { //If already logged in
            sendRequest(AccessToken.getCurrentAccessToken());
        }

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                sendRequest(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.v("LoginActivity", "cancel");
            }

            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
                Log.d(TAG, "Login attempt failed.");
                deleteAccessToken();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    //Retrieve user data from JSON object
    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();

        try {
            String id = object.getString("id");
            URL profile_pic;
            try {
                profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
                bundle.putString("profile_pic", profile_pic.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
            bundle.putString("idFacebook", id);
            if (object.has("friends")) {
                bundle.putString("friends", object.getString("friends"));
            }
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));


            userInfo.saveFacebookUserInfo(object.getString("first_name"),
                    object.getString("last_name"),object.getString("email"),
                    object.getString("gender"), profile_pic.toString());

        } catch (Exception e) {
            Log.d(TAG, "BUNDLE Exception : "+e.toString());
        }
        return bundle;
    }

    private void deleteAccessToken() {
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                if (currentAccessToken == null){
                    //User logged out
                    userInfo.clearToken();
                    LoginManager.getInstance().logOut();
                }
            }
        };
    }

    /*
        Submit read permissions and retrieve user data. Send data as a bundle and
        transition to the MainActivity
     */
    private void sendRequest(AccessToken token){
        GraphRequest request = GraphRequest.newMeRequest(token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Bundle facebookData = getFacebookData(object);
                        final User user = new User(facebookData);
                        UserDatabase.checkUserExists(user);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtras(facebookData);
                        startActivity(intent);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email,picture,gender,birthday,friendlists,friends");
        request.setParameters(parameters);
        request.executeAsync();
    }
}

