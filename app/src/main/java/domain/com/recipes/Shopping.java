package domain.com.recipes;

/*-----------------------------------

    - Recipes -

    created by cubycode ©2017
    All Rights reserved

-----------------------------------*/

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.R.id.list;

public class Shopping extends AppCompatActivity {

    /* Views */
    ListView shoppingListView;

    /* Variables */
    List<String> ingredientsArray;
    SharedPreferences prefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Hide ActionBar
        getSupportActionBar().hide();


        // Get shopping List
        prefs = PreferenceManager.getDefaultSharedPreferences(Shopping.this);
        Configs.shoppingString = prefs.getString("shoppingString", "");


        // Make an array of ingredients (to be shown in the shoppingListView)
        String[] one = Configs.shoppingString.split("\\r?\\n");;
        ingredientsArray = new ArrayList<String>();
        ingredientsArray.addAll(Arrays.asList(one));
        ingredientsArray.remove(0);
        // Log.i("log-", "INGREDIENTS ARRAY: " + ingredientsArray);
        setupShoppingListView();


        // Init views
        TextView titleTxt = findViewById(R.id.shopTitleTxt);
        titleTxt.setTypeface(Configs.typeWriter);


        // Init TabBar buttons
        Button tab_one = findViewById(R.id.tab_one);
        Button tab_two = findViewById(R.id.tab_three);

        tab_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Shopping.this, Home.class));
        }});

        tab_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Shopping.this, Account.class));
        }});





        // MARK: - CLEAR LIST BUTTON ------------------------------------
        Button clButt = findViewById(R.id.shopClearListButt);
        clButt.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              AlertDialog.Builder alert = new AlertDialog.Builder(Shopping.this);
              alert.setMessage("Are you sure you want to clear this List?")
                  .setTitle(R.string.app_name)
                  .setPositiveButton("Clear", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          if (ingredientsArray != null) {
                              ingredientsArray.clear();
                              shoppingListView.invalidateViews();
                              shoppingListView.refreshDrawableState();

                              // Save the shopping string
                              prefs.edit().putString("shoppingString", "").apply();
                          }
                  }})
                  .setNegativeButton("Cancel", null)
                  .setIcon(R.drawable.logo);
              alert.create().show();

        }});





        // MARK: - SHARE SHOPPING LIST BUTTON ------------------------------------
        Button shareButt = findViewById(R.id.shopShare);
        shareButt.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent = new Intent(Intent.ACTION_SEND);
              intent.setType("image/jpeg");
              intent.putExtra(Intent.EXTRA_TEXT, "List of ingredients I need: \n\n" + Configs.shoppingString);
              startActivity(Intent.createChooser(intent, "Share on..."));
         }});



        // Init AdMob banner
        AdView mAdView =  findViewById(R.id.admobBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }// end onCreate()





    // MARK: - SETUP SHOPPING LISTVIEW -------------------------------------------------------
    void setupShoppingListView() {
        class ListAdapter extends BaseAdapter {
            private Context context;
            private ListAdapter(Context context, List<String> objects) {
                super();
                this.context = context;
            }

            // CONFIGURE CELL
            @Override
            public View getView(final int position, View cell, ViewGroup parent) {
                if (cell == null) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    assert inflater != null;
                    cell = inflater.inflate(R.layout.cell_shopping_list, null);
                }

                // Get ingredient
                TextView ingrTxt = cell.findViewById(R.id.cslIngredientTxt);
                ingrTxt.setText(ingredientsArray.get(position));

                return cell;
            }
            @Override public int getCount() { return ingredientsArray.size(); }
            @Override public Object getItem(int position) { return ingredientsArray.get(position); }
            @Override public long getItemId(int position) { return position; }
        }


        // Init ListView and set its adapter
        shoppingListView =  findViewById(R.id.shopShoppingListView);
        shoppingListView.setAdapter(new ListAdapter(Shopping.this, ingredientsArray));


        // LONG PRESS -> DELETE AN ITEM -----------------------------------------
        shoppingListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                ingredientsArray.remove(position);
                shoppingListView.invalidateViews();
                shoppingListView.refreshDrawableState();

           return true;
        }});
    }






}// @end
