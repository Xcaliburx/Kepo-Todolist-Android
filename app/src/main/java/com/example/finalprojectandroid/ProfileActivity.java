package com.example.finalprojectandroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalprojectandroid.SharedPref.SharedPref;

public class ProfileActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_USERNAME = "extra_username";
    public static final String EXTRA_TOKEN = "extra_token";
    TextView tvName;
    TextView tvUsername;
    TextView btnLogout;
    ImageView btnBack;
    private SharedPref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String name = (String)getIntent().getSerializableExtra(EXTRA_NAME);
        String username = (String)getIntent().getSerializableExtra(EXTRA_USERNAME);
        String token = (String)getIntent().getSerializableExtra(EXTRA_TOKEN);

        tvName = findViewById(R.id.tv_nama);
        tvUsername = findViewById(R.id.tv_username);
        tvName.setText(name);
        tvUsername.setText(username);

        btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAlertConfirm();
            }
        });

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                intent.putExtra(HomeActivity.EXTRA_USERNAME, username);
                intent.putExtra(HomeActivity.EXTRA_NAME, name);
                intent.putExtra(HomeActivity.EXTRA_TOKEN, token);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getAlertConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);

        builder.setTitle("Logout")
                .setMessage("Are you sure want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        SharedPreferences preferences = getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();
                        startActivity(intent);
                    }
                }).setNegativeButton("No", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}