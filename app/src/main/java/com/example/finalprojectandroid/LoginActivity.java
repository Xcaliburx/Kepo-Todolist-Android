package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalprojectandroid.SharedPref.SharedPref;
import com.example.finalprojectandroid.bottomdialog.BottomSheetDialog;
import com.example.finalprojectandroid.databinding.ActivityLoginBinding;
import com.example.finalprojectandroid.model.User;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private SharedPref pref;
    private static final String BASE_URL = "https://it-division-kepo.herokuapp.com/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        binding.setUser(new User());
        pref = new SharedPref(this);
        if(!pref.getData().equals("")){
            gotoHomeActivity();
        }
    }

    private void login(){
        binding.progressBar.setVisibility(View.VISIBLE);
        User user = binding.getUser();
        if(binding.username.getText().toString().equals("") || binding.password.getText().toString().equals("")){
            binding.progressBar.setVisibility(View.INVISIBLE);
            BottomSheetDialog bottomSheet = new BottomSheetDialog();
            BottomSheetDialog.message = "Please input username or password";
            bottomSheet.show(getSupportFragmentManager(), "BottomSheet");
        }
        else{
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    BASE_URL,
                    getLoginBody(user),
                    (response) -> {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        try{
                            String message = response.getString("message");
                            if(message.equals("Username or password is incorrect")){
                                BottomSheetDialog bottomSheet = new BottomSheetDialog();
                                BottomSheetDialog.message = "Username or password is incorrect";
                                bottomSheet.show(getSupportFragmentManager(), "BottomSheet");
                            }else if(message.equals("Something wrong occured while logging in")){
                                BottomSheetDialog bottomSheet = new BottomSheetDialog();
                                BottomSheetDialog.message = "User not found";
                                bottomSheet.show(getSupportFragmentManager(), "BottomSheet");
                            }else{
                                String data = response.getString("data");
                                pref.save(data);
                                gotoHomeActivity();
                                Log.d("<RESULT>", "onResponse: " + data + message);
                            }
                        }catch (Exception ex){
                            BottomSheetDialog bottomSheet = new BottomSheetDialog();
                            BottomSheetDialog.message = "User not found";
                            bottomSheet.show(getSupportFragmentManager(), "BottomSheet");
                        }
                    },
                    (error) -> {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        BottomSheetDialog bottomSheet = new BottomSheetDialog();
                        BottomSheetDialog.message = "User not found";
                        bottomSheet.show(getSupportFragmentManager(), "BottomSheet");
                    }
            );
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjectRequest);
        }
    }

    private JSONObject getLoginBody(User user){
        Log.d("<RESULT>", "getLoginBody: " + user.getUsername());
        Log.d("<RESULT>", "getLoginBody: " + user.getPassword());
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("username", user.getUsername());
            jsonObject.put("password", user.getPassword());
            return jsonObject;
        } catch (Exception ex){
            return null;
        }
    }

    private void gotoHomeActivity(){
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        String username = pref.getUsername();
        intent.putExtra(HomeActivity.EXTRA_USERNAME, username);
        String token = pref.getToken();
        intent.putExtra(HomeActivity.EXTRA_TOKEN, token);
        String name = pref.getName();
        intent.putExtra(HomeActivity.EXTRA_NAME, name);
        startActivity(intent);
        finish();
    }
}