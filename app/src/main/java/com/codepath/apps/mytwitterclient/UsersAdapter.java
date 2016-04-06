package com.codepath.apps.mytwitterclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mytwitterclient.models.User;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UsersAdapter extends ArrayAdapter<User>{

  public UsersAdapter(Context context, ArrayList<User> users) {
    super(context, android.R.layout.simple_list_item_1, users);
  }

  @Override
  public View getView(int position, View view, ViewGroup parent) {
    ViewHolder holder;
    if (view != null) {
      holder = (ViewHolder) view.getTag();
    } else {
      view = LayoutInflater.from(getContext()).inflate(R.layout.user_item, parent, false);
      holder = new ViewHolder(view);
      view.setTag(holder);
    }

    User user = getItem(position);
    holder.tvName.setText(user.getName());
    holder.tvScreenName.setText(user.getScreenName());
    holder.tvTagline.setText(user.getTagline());
    Glide.with(getContext()).load(user.getProfileImageUrl()).into(holder.ivUserImage);
    return view;
  }

  static class ViewHolder {
    @Bind(R.id.ivUserImage) ImageView ivUserImage;
    @Bind(R.id.tvName) TextView tvName;
    @Bind(R.id.tvScreenName) TextView tvScreenName;
    @Bind(R.id.tvTagline) TextView tvTagline;

    ViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }
}
