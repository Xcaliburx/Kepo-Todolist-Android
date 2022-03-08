package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.finalprojectandroid.bottomdialog.BottomSheetDialog;
import com.example.finalprojectandroid.databinding.ActivityHomeBinding;
import com.example.finalprojectandroid.model.User;

public class HomeActivity extends AppCompatActivity {
    public static final String EXTRA_USERNAME = "extra_username";
    public static final String EXTRA_TOKEN = "extra_token";
    public static final String EXTRA_NAME = "extra_name";

    private ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        String name = (String) getIntent().getSerializableExtra(EXTRA_NAME);
        User user = new User();
        user.setUsername(name);
        user.setPassword(null);

        String token = (String)getIntent().getSerializableExtra(EXTRA_TOKEN);
        String username = (String)getIntent().getSerializableExtra(EXTRA_USERNAME);

        binding.setUser(user);

        binding.btnTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TodoActivity.class);
                intent.putExtra(TodoActivity.EXTRA_NAME, name);
                intent.putExtra(TodoActivity.EXTRA_USERNAME, username);
                intent.putExtra(TodoActivity.EXTRA_TOKEN, token);
                startActivity(intent);
            }
        });

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SearchTodoActivity.class);
                intent.putExtra(SearchTodoActivity.EXTRA_NAME, name);
                intent.putExtra(SearchTodoActivity.EXTRA_USERNAME, username);
                intent.putExtra(SearchTodoActivity.EXTRA_TOKEN, token);
                startActivity(intent);
            }
        });

        binding.btnSearchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SearchUserActivity.class);
                intent.putExtra(SearchUserActivity.EXTRA_NAME, name);
                intent.putExtra(SearchUserActivity.EXTRA_USERNAME, username);
                intent.putExtra(SearchUserActivity.EXTRA_TOKEN, token);
                startActivity(intent);
            }
        });

        binding.btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra(ProfileActivity.EXTRA_NAME, name);
                intent.putExtra(ProfileActivity.EXTRA_USERNAME, username);
                intent.putExtra(ProfileActivity.EXTRA_TOKEN, token);
                startActivity(intent);
            }
        });

    }
}