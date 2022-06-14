package com.orhunkolgeli.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    Button btnLogout;
    Button btnSubmit;
    ImageView ivPostImg;
    Button btnTakePic;
    EditText etDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etDescription = findViewById(R.id.etDescription);
        btnTakePic = findViewById(R.id.btnTakePic);
        ivPostImg = findViewById(R.id.ivPostImg);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = ParseUser.getCurrentUser().getUsername().toString();
                Toast.makeText(MainActivity.this, user, Toast.LENGTH_SHORT).show();
                ParseUser.logOutInBackground();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                // Prevent user from using back button to go back to Main Activity
                finish();
            }
        });
        // Get Instagram posts from the Parse database
        queryPosts();
    }

    private void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // Include user information
        query.include(Post.KEY_USER);
        // To get Instagram posts from the database, call findInBackground
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) { // exception thrown
                    Log.e(TAG, "Couldn't get posts", e);
                    return;
                }
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + " User: " + post.getUser().getUsername());
                }
            }
        });

    }
}