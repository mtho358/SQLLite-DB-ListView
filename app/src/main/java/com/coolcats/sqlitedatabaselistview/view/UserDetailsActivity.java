package com.coolcats.sqlitedatabaselistview.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.coolcats.sqlitedatabaselistview.R;
import com.coolcats.sqlitedatabaselistview.databinding.ActivityMainBinding;
import com.coolcats.sqlitedatabaselistview.databinding.ActivityUserDetailsBinding;
import com.coolcats.sqlitedatabaselistview.model.User;
import com.coolcats.sqlitedatabaselistview.util.Logger;
import com.coolcats.sqlitedatabaselistview.util.Position;

public class UserDetailsActivity extends AppCompatActivity {

    Logger log = new Logger();
    private ActivityUserDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getBundleExtra("READ");

        User user = (User) intent.getSerializableExtra("READ");

        binding = ActivityUserDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.idTextview.setText(user.getId()+"");
        binding.nameTextview.setText(user.getName());
        binding.positionTextview.setText(user.getPosition().name().toString());

    }
}