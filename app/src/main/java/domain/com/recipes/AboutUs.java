package domain.com.recipes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

public class AboutUs extends AppCompatActivity {
TextView rdTitleTxt,tvAbout,tvContact,tvNumber,tvMail;
    ImageView Image1;


    /* Variables */
    ParseObject recipeObj;
    List<ParseObject> likesArray;
    MarshMallowPermission mmp = new MarshMallowPermission(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        // Hide ActionBar
        getSupportActionBar().hide();


        // Init views
        rdTitleTxt =findViewById(R.id.rdTitleTxt);
        tvAbout =findViewById(R.id.tvAbout);
        tvContact =findViewById(R.id.tvContact);
        tvNumber = findViewById(R.id.tvNumber);
        tvMail = findViewById(R.id.tvMail);
        Image1 =findViewById(R.id.Image1);

        // MARK: - BACK BUTTON ------------------------------------
        Button backButt = findViewById(R.id.rdBackButt2);
        backButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }});
    }
}
