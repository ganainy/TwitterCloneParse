package com.example.twittercloneparse;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private List<String> usernameList=new ArrayList<>();
    RecyclerView recyclerView;
    UserAdapter userAdapter;
    private List<String> isFollowing=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        loadUsers();

    }

    private void loadUsers() {
        /** get registered users*/
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        //exclude logged in user when bringing users
        query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.addAscendingOrder("username");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e==null)
                {
                    if (list.size()>0)
                    {
                        for(ParseUser user:list)
                        {
                            usernameList.add(user.getUsername());
                        }

                        getFollowState();
                    }else
                    {
                        Log.i(TAG, "list.size()=0 ");
                    }

                }else
                {
                    Log.i(TAG, e.getMessage());
                }
            }
        });


    }

    private void getFollowState2() {
        /** another way to get which users that logged in user follows but other one is simpler*/
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        //exclude logged in user when bringing users
        query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e==null)
                {
                    if (list.size()>0)
                    {
                        for(ParseUser user:list)
                        {
                             isFollowing = user.getList("isFollowing");

                        }


                    }else
                    {
                        Log.i(TAG, "list.size()=0 ");
                    }

                }else
                {
                    Log.i(TAG, e.getMessage());
                }

                initRecycler();
            }
        });


    }



    private void getFollowState()
    {

        /**get users that logged in user follows*/
        for(String username:usernameList)
        {
            if(ParseUser.getCurrentUser().getList("isFollowing").contains(username))
            {
                isFollowing.add(username);
            }

        }
        initRecycler();
    }







    private void initRecycler() {
        recyclerView=findViewById(R.id.recyclerView);
        /** username list will show all users
         * isFollowing list will mark only users that logged in user follows
         * */
        userAdapter=new UserAdapter(this,usernameList,isFollowing);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(userAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_signout:
                ParseUser.logOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("source","logout");
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
