package com.codepath.apps.mytwitterclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mytwitterclient.models.Tweet;
import com.codepath.apps.mytwitterclient.models.User;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TweetsAdapter extends ArrayAdapter<Tweet>{

  public TweetsAdapter(Context context, ArrayList<Tweet> tweets) {
    super(context, android.R.layout.simple_list_item_1 ,tweets);
  }

  @Override
  public View getView(int position, View view, ViewGroup parent) {
    ViewHolder holder;
    if (view != null) {
      holder = (ViewHolder) view.getTag();
    } else {
      view = LayoutInflater.from(getContext()).inflate(R.layout.tweet_item, parent, false);
      holder = new ViewHolder(view);
      view.setTag(holder);
    }

    Tweet tweet = getItem(position);
    User user = tweet.getUser();
    holder.tvBody.setText(tweet.getBody());
    holder.tvUserName.setText(user.getName() + " (" + user.getScreenName() + ")");
    holder.tvCreatedAt.setText(tweet.getCreatedAtInWords());
    Glide.with(getContext()).load(user.getProfileImageUrl()).into(holder.ivUser);
    return view;
  }

  static class ViewHolder {
    @Bind(R.id.tvTweetBody) TextView tvBody;
    @Bind(R.id.tvTweetUserName) TextView tvUserName;
    @Bind(R.id.tvTweetCreatedAt) TextView tvCreatedAt;
    @Bind(R.id.ivTweetUserImage) ImageView ivUser;

    ViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }
}
