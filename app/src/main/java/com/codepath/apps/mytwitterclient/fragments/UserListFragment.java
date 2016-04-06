package com.codepath.apps.mytwitterclient.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.apps.mytwitterclient.ProfileActivity;
import com.codepath.apps.mytwitterclient.R;
import com.codepath.apps.mytwitterclient.UsersAdapter;
import com.codepath.apps.mytwitterclient.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserListFragment extends Fragment {

  private ArrayList<User> users;
  private UsersAdapter aUsers;
  ListView lvUsers;
  private ProgressDialog progress;
//  @Bind(R.id.lvUsers);

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_user_list, parent, false);
//    ButterKnife.bind(v);
    lvUsers = (ListView) v.findViewById(R.id.lvUsers);
    lvUsers.setAdapter(aUsers);
    lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        User u = (User) lvUsers.getItemAtPosition(position);
        String screenName = u.getScreenName();
        Intent i = new Intent(getActivity(), ProfileActivity.class);
        i.putExtra("screen_name", screenName);
        startActivity(i);
      }
    });
    return v;
  }

  // creation lifecyle event

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    users = new ArrayList<>();
    aUsers = new UsersAdapter(getActivity(), users);
  }

  public void addAll(List<User> list) {
    aUsers.addAll(list);
  }

  public void addToBeginning(User user) {
    users.add(0, user);
    aUsers.notifyDataSetChanged();
  }

  public void add(User user) {
    users.add(user);
    aUsers.notifyDataSetChanged();
  }

  public long lastTweetID() {
    return users.get(users.size() - 1).getUid();
  }

  public void progressOn(String str) {
    progress = new ProgressDialog(getActivity());
    progress.setMessage(str);
    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progress.setIndeterminate(true);
    progress.show();
  }

  public void progressOff() {
    progress.hide();
  }
}
