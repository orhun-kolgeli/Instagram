package com.orhunkolgeli.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {
    public static final String TAG = "FeedActivity";
    public static final int QUERY_ITEM_COUNT = 5;
    SwipeRefreshLayout swipeContainer;
    RecyclerView rvPosts;
    PostAdapter adapter;
    List<Post> allPosts;
    Button btnLogout;
    EndlessRecyclerViewScrollListener scrollListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        // Initialize recyclerview
        rvPosts = findViewById(R.id.rvPosts);
        // Initialize the array to hold posts
        allPosts = new ArrayList<>();
        // create a PostsAdapter
        adapter = new PostAdapter(this, allPosts);
        // Set the adapter on the recycler view
        rvPosts.setAdapter(adapter);
        // Set the layout manager on the recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvPosts.setLayoutManager(linearLayoutManager);
        // Set up scroll listener
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvPosts.addOnScrollListener(scrollListener);
        // Query posts from Parse
        queryPosts(0);

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = ParseUser.getCurrentUser().getUsername().toString();
                ParseUser.logOutInBackground();
                Intent intent = new Intent(FeedActivity.this, LoginActivity.class);
                startActivity(intent);
                // Prevent user from using back button to go back to Main Activity after logout
                finish();
            }
        });

        // Set up swipe-to-refresh
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                queryPosts(0);
                // Call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
                // Reset endless scroll listener when performing a new search
                scrollListener.resetState();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
                R.color.insta_blue,
                R.color.insta_purple_red,
                R.color.insta_yellow,
                R.color.insta_purple);
    }

    private void loadNextDataFromApi(int page) {
        queryPosts(page);
    }

    private void queryPosts(int page) {
        // specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        query.include(Post.KEY_USER);
        // Skip the posts that have already been shown
        query.setSkip(page*QUERY_ITEM_COUNT);
        // limit query to latest 20 items
        query.setLimit(QUERY_ITEM_COUNT); // FOR TESTING PURPOSES
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                // Check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                // For debugging purposes print every post description to logcat
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }
                // Save received posts to list and notify adapter of new data
                allPosts.addAll(posts);
                adapter.notifyItemRangeInserted(page*QUERY_ITEM_COUNT, QUERY_ITEM_COUNT);
            }
        });
    }
}