package domain.com.recipes;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Order extends AppCompatActivity {
    private ArrayList<String> Array ;
    private boolean After10second = false;
    private boolean ViewColor = false ;
    private  AsyncTask <Void , Void , Void > a ;
    private String LastPosition;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Array = new ArrayList<String>();
        final ListView List_view = (ListView) findViewById(R.id.List_view);
        DatabaseReference myref = FirebaseDatabase.getInstance().getReference("Kitchen");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                Array ){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){

                View view = super.getView(position, convertView, parent);

                TextView textview = (TextView) view.findViewById(android.R.id.text1);
                if ((getResources().getConfiguration().screenLayout &
                        Configuration.SCREENLAYOUT_SIZE_MASK) ==
                        Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                    // on a large screen device ...


                    //Set your Font Size Here.
                    textview.setTextSize(30);
                }
             if(Array.get(position).equals(LastPosition)){

                    view.setBackgroundColor(Color.parseColor("#91DC5A"));

             }
             else {

                 view.setBackgroundResource(android.R.color.transparent);
             }

                return view;
            }
        };



        myref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {

                    Array.clear();

                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        Array.add(data.getKey());
                        arrayAdapter.notifyDataSetChanged();
                        List_view.deferNotifyDataSetChanged();





                    }

                    for(int i = 0 ; i < Array.size() ; i++){
                        if(Array.get(i).equals(LastPosition)){
                            List_view.smoothScrollToPosition(i);
                        }

                    }


                }




            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Order.this, "error"+databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        a =   new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Thread.sleep(10000);
                    After10second = true ;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();

        myref.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {



              if(After10second) {
                  final MediaPlayer player = MediaPlayer.create(Order.this, R.raw.plucky);
                  player.start();
                  LastPosition = dataSnapshot.getKey();



              }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        List_view.setAdapter(arrayAdapter);

        List_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent( Order.this , Orderdetails.class );
                intent.putExtra("Key",Array.get(i));
                startActivity(intent);

            }
        });
    }

    @Override
    public void onBackPressed() {
        a.cancel(true);
        Array.clear();
        After10second = false ;
        finish();

    }
}
