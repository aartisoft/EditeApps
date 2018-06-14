package domain.com.recipes;

/*-----------------------------------

    - Recipes -

    created by cubycode ©2017
    All Rights reserved

-----------------------------------*/

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.util.Set;


public class Configs extends Application {



//    // IMPORTANT: Reaplce the red strings below with your own Application ID and Client Key of your app on back4app
//    public static String PARSE_APP_ID = "hrpYkU8Kvjqu4XaV7VAW5b62EsDRqRCLmZC9izB3";
//    public static String PARSE_CLIENT_KEY = "AkAJXQLAZDfllNSkweRL9DmJgdB8CHtPQIUHRF24";
//    //-----------------------------------------------------------------------------
// IMPORTANT: Reaplce the red strings below with your own Application ID and Client Key of your app on back4app
public static String PARSE_APP_ID = "4IZN5z01RnK3SiNMCT5QWbtlDNhPycteqq3UUME5";
    public static String PARSE_CLIENT_KEY = "4hwOdru3gAKT3HQDegKemNbsf7tGPvGoxcBkSk8F";
    //-----------------------------------------------------------------------------



    // FOOD CATEGORIES ARRAY (editable)
    public static String[] categoriesArray = new String[] {
            "Soup",
            "Salad",
            "Appetizer",
            "Pasta",
            "Main Dish",
            "Fahita",
            "Pizza",
            "Burger Jumbo",
            "Sandwich",
            "Desserts",
            "Crepe",
            "Hot Drinks",
            "Cocktails",
            "Ice cream",
            "Frappe",
            "Juices",
            "Mix Shake",
            "Milk Shake",
            "Shisha",



            // You can add categories here...
            // IMPORTANT: Also remember to add the proper images into the 'drawable'  folder.
    };


    // Custom yellow color
    public static String yellow = "#f7fc8e";



    // Set fonts
    public static Typeface typeWriter;






    /************** DO NOT EDIT THE CODE BELOW *******************/

    public static String  USER_CLASS_NAME = "_User";
    public static String  USER_FULLNAME = "fullName";
    public static String  USER_AVATAR = "avatar";
    public static String  USER_USERNAME = "username";
    public static String  USER_EMAIL = "email";
    public static String  USER_JOB = "job";
    public static String  USER_ABOUTME = "aboutMe";
    public static String  USER_IS_REPORTED = "isReported";


    public static String  LIKES_CLASS_NAME = "Likes";
    public static String  LIKES_LIKED_BY = "likedBy";
    public static String  LIKES_RECIPE_LIKED = "recipeLiked";


    public static String  RECIPES_CLASS_NAME = "Recipes";
    public static String  RECIPES_COVER = "cover";
    public static String  RECIPES_TITLE = "title";
    public static String  RECIPES_TITLE_LOWERCASE = "titleLowercase";
    public static String  RECIPES_CATEGORY = "category";
    public static String  RECIPES_LIKES = "likes";
    public static String  RECIPES_ABOUT = "aboutRecipe";
    public static String  RECIPES_DIFFICULTY = "difficulty";
    public static String  RECIPES_COOKING = "cooking";
    public static String  RECIPES_BAKING = "baking";
    public static String  RECIPES_RESTING = "resting";
    public static String  RECIPES_YOUTUBE = "youtube";
    public static String  RECIPES_VIDEO_TITLE = "videoTitle";
    public static String  RECIPES_INGREDIENTS = "ingredients";
    public static String  RECIPES_PREPARATION = "preparation";
    public static String  RECIPES_USER_POINTER = "userPointer";
    public static String  RECIPES_IS_REPORTED = "isReported";
    public static String  RECIPES_price = "price";
    public static String  RECIPES_REPORT_MESSAGE = "reportMessage";
    public static String  RECIPES_KEYWORDS = "keywords";
    public static String  RECIPES_CREATED_AT = "createdAt";
    public static String  RECIPES_COMMENTS = "comments";

    public static String  COMMENTS_CLASS_NAME = "Comments";
    public static String  COMMENTS_RECIPE_POINTER = "recipePointer";
    public static String  COMMENTS_USER_POINTER = "userPointer";
    public static String  COMMENTS_COMMENT = "comment";

    public static String  ACTIVITY_CLASS_NAME = "Activity";
    public static String  ACTIVITY_CURRENT_USER = "currentUser";
    public static String  ACTIVITY_OTHER_USER = "otherUser";
    public static String  ACTIVITY_TEXT = "text";


    public static String categoryStr = "";
    public static String shoppingString;



    boolean isParseInitialized = false;

    public void onCreate() {
        super.onCreate();

        if (!isParseInitialized) {
            Parse.initialize(new Parse.Configuration.Builder(this)
                    .applicationId(String.valueOf(PARSE_APP_ID))
                    .clientKey(String.valueOf(PARSE_CLIENT_KEY))
                    .server("https://parseapi.back4app.com")
                    .build()
            );
            Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
            ParseUser.enableAutomaticUser();
            isParseInitialized = true;
        }

        // Init Facebook Utils
        ParseFacebookUtils.initialize(this);

        // Tnit font
        // (the font files are into app/src/main/assets/font folder)
        typeWriter = Typeface.createFromAsset(getAssets(),"font/AmericanTypewriter.ttc");


    }// end onCreate()




    // MARK: - CUSTOM PROGRESS DIALOG -----------
    public static  Dialog dialog ;
    public static void showPD(String mess, Context ctx) {
        dialog = new Dialog(ctx);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pd);
        dialog.show();
    }
    public static void hidePD(){ dialog.dismiss(); }



    // SIMPLE ALERT DIALOG --------------------------------------------------
    public static void simpleAlert(String mess, Context context){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(mess)
                .setTitle(R.string.app_name)
                .setPositiveButton("OK", null)
                .setIcon(R.drawable.logooo);
        alert.create().show();
    }





    // MARK: - SCALE BITMAP TO MAX SIZE
    public static Bitmap scaleBitmapToMaxSize(int maxSize, Bitmap bm) {
            int outWidth;
            int outHeight;
            int inWidth = bm.getWidth();
            int inHeight = bm.getHeight();
            if(inWidth > inHeight){
                outWidth = maxSize;
                outHeight = (inHeight * maxSize) / inWidth;
            } else {
                outHeight = maxSize;
                outWidth = (inWidth * maxSize) / inHeight;
            }
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, outWidth, outHeight, false);
            return resizedBitmap;
    }



    // ROUND THOUSANDS NUMBERS
    public static String roundThousandsIntoK(Number number) {
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        long numValue = number.longValue();
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.0").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat("#,##0").format(numValue);
        }
    }



}//@end
