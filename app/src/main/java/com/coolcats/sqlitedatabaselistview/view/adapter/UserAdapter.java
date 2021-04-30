package com.coolcats.sqlitedatabaselistview.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.coolcats.sqlitedatabaselistview.R;
import com.coolcats.sqlitedatabaselistview.databinding.UserItemLayoutBinding;
import com.coolcats.sqlitedatabaselistview.model.User;

import java.util.List;

public class UserAdapter extends BaseAdapter {

    private List<User> users;

    public UserAdapter(List<User> users) {
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return -i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserItemLayoutBinding binding =
                UserItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        User user = users.get(position);

        binding.nameTextview.setText(user.getName());

        return binding.getRoot();
    }

    public void upDateList(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }
}
