package domain.com.recipes;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Orderdetails extends AppCompatActivity {
String Key;
    ArrayList<String> ArrayDetails ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails);
        ArrayDetails = new ArrayList<String>();
        final ListView List_view = (ListView) findViewById(R.id.List_viewDetails);




        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            Key = bundle.getString("Key");

        }

        DatabaseReference myref = FirebaseDatabase.getInstance().getReference("Kitchen").child(Key);
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        ArrayDetails.add(data.getValue(String.class));

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Orderdetails.this, "error"+databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                ArrayDetails ){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){

                View view = super.getView(position, convertView, parent);

                TextView textview = (TextView) view.findViewById(android.R.id.text1);
                if ((getResources().getConfiguration().screenLayout &
                        Configuration.SCREENLAYOUT_SIZE_MASK) ==
                        Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                    // on a large screen device ...
                    textview.setTextSize(30);
                }
                //Set your Font Size Here.


                return view;
            }
        };


        List_view.setAdapter(arrayAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
      if(item.getItemId() == R.id.delete){

          AlertDialog.Builder builder1 = new AlertDialog.Builder(Orderdetails.this);
          builder1.setMessage("Are you sure to delete this order ? ");
          builder1.setCancelable(true);

          builder1.setPositiveButton(
                  "Yes",
                  new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int id) {

                          // Do your Code
                          DatabaseReference myref = FirebaseDatabase.getInstance().getReference("Kitchen").child(Key);
                          myref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                              @Override
                              public void onSuccess(Void aVoid) {
                                  finish();
                              }
                          });



                      }
                  });

          builder1.setNegativeButton(
                  "No",
                  new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int id) {

                      }
                  });

          AlertDialog alert11 = builder1.create();
          alert11.show();

          return true;
         }
        return super.onOptionsItemSelected(item);
        }

    }

