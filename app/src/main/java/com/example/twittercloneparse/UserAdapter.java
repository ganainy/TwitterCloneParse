package com.example.twittercloneparse;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Arrays;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UsersViewHolder> {
    private static final String TAG = "UserAdapter";
    Context context;
    List<String> userList;
    List<String>isFollowing;


    public UserAdapter(Context context, List<String> userList, List<String> isFollowing) {
        this.context = context;
        this.userList = userList;
        this.isFollowing = isFollowing;
    }



    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UsersViewHolder(inflate);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    public void onBindViewHolder(UsersViewHolder holder, int position) {
        String currentUser = userList.get(position);
        holder.userName.setText(currentUser);

        for (int i = 0; i < isFollowing.size(); i++) {
            if(currentUser.equals(isFollowing.get(i)))
            {
                holder.follow.setImageResource(R.drawable.success_checked);
            }
        }

    }



    public class UsersViewHolder extends RecyclerView.ViewHolder{


        TextView userName;
        ImageView follow;
        public UsersViewHolder(View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.username_text_view);
            follow=itemView.findViewById(R.id.followImageView);

            follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //follow
                    HomeActivity homeActivity=(HomeActivity)context;


                    if(follow.getDrawable().getConstantState()==(homeActivity.getResources().getDrawable(R.drawable.success_checked)).getConstantState())
                    {
                        //this means we clicked followed user and we should un follow
                        unfollow();

                    }else
                    {
                        //we should follow
                        follow();
                    }

                }
            });
        }

        private void follow() {
            ParseUser.getCurrentUser().addUnique("isFollowing",userList.get(getAdapterPosition()));
            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null)
                    {
                        Toast.makeText(context, "Followed "+userList.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                        follow.setImageResource(R.drawable.success_checked);

                    }else
                    {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, e.getMessage());
                    }
                }
            });
        }


    private void unfollow( ) {
     ParseUser.getCurrentUser().getList("isFollowing").remove(userList.get(getAdapterPosition()));
        List tempUsers=ParseUser.getCurrentUser().getList("isFollowing");
        ParseUser.getCurrentUser().remove("isFollowing");
        ParseUser.getCurrentUser().put("isFollowing",tempUsers);


        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                {
                    Toast.makeText(context, "Unfollowed "+userList.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                    follow.setImageResource(R.drawable.success_unchecked);

                }else
                {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, e.getMessage());
                }
            }
        });
    }
    
    }
}
