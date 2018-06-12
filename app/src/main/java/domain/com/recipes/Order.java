package domain.com.recipes;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.ColorSpace;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Order extends AppCompatActivity {
    ArrayList<String> Array ;
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
                    textview.setTextSize(30);
                }
                //Set your Font Size Here.


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
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Order.this, "error"+databaseError.toString(), Toast.LENGTH_SHORT).show();
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

}
