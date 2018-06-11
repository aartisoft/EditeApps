package domain.com.recipes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
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
                ArrayDetails );


        List_view.setAdapter(arrayAdapter);

    }



}
