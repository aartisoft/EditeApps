package domain.com.recipes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class Offline extends AppCompatActivity {
ImageView menu1,menu2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        // Hide ActionBar
        getSupportActionBar().hide();
        menu1 = findViewById(R.id.menu1);
        menu2 = findViewById(R.id.menu2);

    }
}
