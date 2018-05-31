package domain.com.recipes;

/*-----------------------------------

    - Recipes -

    created by cubycode ©2017
    All Rights reserved

-----------------------------------*/

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;

public class SignUp extends AppCompatActivity {
    
    /* Views */

    EditText usernameTxt;
    EditText passwordTxt;
    EditText fullnameTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Hide ActionBar
        getSupportActionBar().hide();



        // Init views
        usernameTxt =  findViewById(R.id.usernameTxt2);
        passwordTxt =  findViewById(R.id.passwordTxt2);
        fullnameTxt =  findViewById(R.id.fullnameTxt);





        // SIGN UP BUTTON ------------------------------------------------------------------------
        Button signupButt =  findViewById(R.id.signUpButt2);
        signupButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (usernameTxt.getText().toString().matches("") ||
                        passwordTxt.getText().toString().matches("") ||
                        fullnameTxt.getText().toString().matches("")
                        ){

                    Configs.simpleAlert("You must fill all the fields to Sign Up!", SignUp.this);

                } else {
                    Configs.showPD("Please wait...", SignUp.this);
                    dismisskeyboard();

                    final ParseUser user = new ParseUser();
                    user.setUsername(usernameTxt.getText().toString());
                    user.setEmail(usernameTxt.getText().toString());
                    user.setPassword(passwordTxt.getText().toString());
                    user.put(Configs.USER_FULLNAME, fullnameTxt.getText().toString());
                    user.put(Configs.USER_IS_REPORTED, false);

                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException error) {
                            if (error == null) {

                                // Save default avatar
                                Bitmap bitmap = BitmapFactory.decodeResource(SignUp.this.getResources(), R.drawable.logo);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                byte[] byteArray = stream.toByteArray();
                                ParseFile imageFile = new ParseFile("avatar.jpg", byteArray);
                                user.put(Configs.USER_AVATAR, imageFile);
                                user.saveInBackground();

                                Configs.hidePD();
                                startActivity(new Intent(SignUp.this, Home.class));
                            } else {
                                Configs.hidePD();
                                Configs.simpleAlert(error.getMessage(), SignUp.this);
                    }}});
                }
        }});




        // MARK: - TERMS OF USE BUTTON ----------------------------------------------------------
        Button touButt =  findViewById(R.id.touButt);
        touButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, TermsOfuse.class));
        }});



        // MARK: - DISMISS BUTTON ---------------------------------------------------------------
        Button dismissButt =  findViewById(R.id.signupDismissButt);
        dismissButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
        }});



    }// end onCreate()





    // DISMISS KEYBOARD
    public void dismisskeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(usernameTxt.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(passwordTxt.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(fullnameTxt.getWindowToken(), 0);
    }




}// @end

