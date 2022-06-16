package com.orhunkolgeli.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

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
            tvCaption = findViewById(R.id.tvCaption);
            tvDate = findViewById(R.id.tvDate);
            tvCaption.setText(caption);
            tvDate.setText(date);
        }
    }
}