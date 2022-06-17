package com.orhunkolgeli.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView tvCaption;
        TextView tvDate;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String caption = bundle.getString("description");
            String date = bundle.getString("date");
            ParseFile profile_pic = ParseUser.getCurrentUser().getParseFile("profile_pic");
            tvCaption = findViewById(R.id.tvCaption);
            tvDate = findViewById(R.id.tvDate);
            tvCaption.setText(caption);
            tvDate.setText(date);
        }
    }
}