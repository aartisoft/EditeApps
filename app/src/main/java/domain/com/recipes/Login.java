package domain.com.recipes;

/*-----------------------------------

    - Recipes -

    created by cubycode ©2017
    All Rights reserved

-----------------------------------*/

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Login extends AppCompatActivity {

        /* Views */
        EditText usernameTxt;
        EditText passwordTxt;




        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.login);
            super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


            // Hide ActionBar
            getSupportActionBar().hide();



            // Init views
            usernameTxt = findViewById(R.id.usernameTxt);
            passwordTxt = findViewById(R.id.passwordTxt);




            // MARK: - FACEBOOK LOGIN BUTTON ------------------------------------------------------------------
            Button fbButt = findViewById(R.id.facebookButt);
            fbButt.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                List<String> permissions = Arrays.asList("public_profile", "email");
                     Configs.showPD("Please wait...", Login.this);

                ParseFacebookUtils.logInWithReadPermissionsInBackground(Login.this, permissions, new LogInCallback() {
                     @Override
                    public void done(ParseUser user, ParseException e) {
                         if (user == null) {
                             Log.i("log-", "Uh oh. The user cancelled the Facebook login.");
                             Configs.hidePD();

                         } else if (user.isNew()) {
                            getUserDetailsFromFB();

                         } else {
                            Log.i("log-", "RETURNING User logged in through Facebook!");
                             Configs.hidePD();
                            startActivity(new Intent(Login.this, Home.class));
                }}});
            }});




            // This code generates a KeyHash that you'll have to copy from your Logcat console and paste it into Key Hashes field in the 'Settings' section of your Facebook Android App
            try {
                PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.i("log-", "keyhash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
            } catch (PackageManager.NameNotFoundException e) {
            } catch (NoSuchAlgorithmException e) {}







            // MARK: - LOGIN BUTTON ------------------------------------------------------------------
            Button loginButt = findViewById(R.id.loginButt);
            loginButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Configs.showPD("Please wait", Login.this);

                    ParseUser.logInInBackground(usernameTxt.getText().toString(), passwordTxt.getText().toString(),
                            new LogInCallback() {
                                public void done(ParseUser user, ParseException error) {
                                    if (user != null) {
                                        Configs.hidePD();
                                        startActivity(new Intent(Login.this, Home.class));
                                    } else {
                                        Configs.hidePD();
                                        Configs.simpleAlert(error.getMessage(), Login.this);
                    }}});
            }});





            // MARK: - SIGN UP BUTTON ------------------------------------------------------------------
            Button signupButt = findViewById(R.id.signUpButt);
            signupButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Login.this, SignUp.class));
            }});





            // MARK: - FORGOT PASSWORD BUTTON ------------------------------------------------------------------
            Button fpButt = findViewById(R.id.forgotPassButt);
            fpButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(Login.this);
                    alert.setTitle(R.string.app_name);
                    alert.setMessage("Type the valid email address you've used to register on this app");

                    // Add an EditTxt
                    final EditText editTxt = new EditText (Login.this);
                    editTxt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    alert.setView(editTxt)
                            .setNegativeButton("Cancel", null)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    // Reset password
                                    ParseUser.requestPasswordResetInBackground(editTxt.getText().toString(), new RequestPasswordResetCallback() {
                                        public void done(ParseException error) {
                                            if (error == null) {
                                                Configs.simpleAlert("We've sent you an email to reset your password!", Login.this);
                                            } else {
                                                Configs.simpleAlert(error.getMessage(), Login.this);
                                    }}});

                            }});
                    alert.show();

            }});





            // MARK: - DISMISS BUTTON ---------------------------------------------------------------
            Button dismissButt = findViewById(R.id.loginDismissButt);
            dismissButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
            }});



        }// end onCreate()





    // MARK: - FACEBOOK GRAPH REQUEST --------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }


    void getUserDetailsFromFB() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),new GraphRequest.GraphJSONObjectCallback(){
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                String facebookID = "";
                String name = "";
                String email = "";
                String username = "";

                try{
                    name = object.getString("name");
                    email = object.getString("email");
                    facebookID = object.getString("id");

                    String[] one = name.toLowerCase().split(" ");
                    for (String word : one) { username += word; }
                    Log.i("log-", "USERNAME: " + username + "\n");
                    Log.i("log-", "email: " + email + "\n");
                    Log.i("log-", "name: " + name + "\n");

                } catch(JSONException e){ e.printStackTrace(); }


                // SAVE NEW USER IN YOUR PARSE DASHBOARD -> USER CLASS
                final String finalFacebookID = facebookID;
                final String finalUsername = username;
                final String finalEmail = email;
                final String finalName = name;

                final ParseUser currUser = ParseUser.getCurrentUser();
                currUser.put(Configs.USER_USERNAME, finalUsername);
                if (finalEmail != null) { currUser.put(Configs.USER_EMAIL, finalEmail);
                } else { currUser.put(Configs.USER_EMAIL, facebookID + "@facebook.com"); }
                currUser.put(Configs.USER_FULLNAME, finalName);
                currUser.put(Configs.USER_IS_REPORTED, false);

                currUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.i("log-", "NEW USER signed up and logged in through Facebook...");


                        // Get and Save avatar from Facebook
                        new Timer().schedule(new TimerTask() {
                            @Override public void run() {
                                try {
                                    URL imageURL = new URL("https://graph.facebook.com/" + finalFacebookID + "/picture?type=large");
                                    Bitmap avatarBm = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    avatarBm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                    byte[] byteArray = stream.toByteArray();
                                    ParseFile imageFile = new ParseFile("image.jpg", byteArray);
                                    currUser.put(Configs.USER_AVATAR, imageFile);
                                    currUser.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException error) {
                                            Log.i("log-", "... AND AVATAR SAVED!");
                                            Configs.hidePD();

                                            startActivity(new Intent(Login.this, Home.class));
                                        }});
                                } catch (IOException error) { error.printStackTrace(); }

                            }}, 1000); // 1 second


                    }}); // end saveInBackground

            }}); // end graphRequest


        Bundle parameters = new Bundle();
        parameters.putString("fields", "email, name, picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }
    // END FACEBOOK GRAPH REQUEST --------------------------------------------------------------------




}//@end
