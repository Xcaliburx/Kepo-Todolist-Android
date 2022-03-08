package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalprojectandroid.Clickable.IUserClickable;
import com.example.finalprojectandroid.adapter.SearchUserAdapter;
import com.example.finalprojectandroid.bottomdialog.BottomSheetDialog;
import com.example.finalprojectandroid.databinding.ActivitySearchUserBinding;
import com.example.finalprojectandroid.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchUserActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_USERNAME = "extra_username";
    public static final String EXTRA_TOKEN = "extra_token";

    private ActivitySearchUserBinding binding;
    private SearchUserAdapter searchUserAdapter;
    String token, username, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_user);

        token = (String)getIntent().getSerializableExtra(EXTRA_TOKEN);
        username = (String)getIntent().getSerializableExtra(EXTRA_USERNAME);
        name = (String)getIntent().getSerializableExtra(EXTRA_NAME);

        initAdapter();

        binding.progressBar.setVisibility(View.INVISIBLE);

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputted = "\"" + binding.etInput.getText().toString() + "\"";
                binding.tvSearched.setText(inputted);
                loadData();
            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchUserActivity.this, HomeActivity.class);
                intent.putExtra(HomeActivity.EXTRA_USERNAME, username);
                intent.putExtra(HomeActivity.EXTRA_NAME, name);
                intent.putExtra(HomeActivity.EXTRA_TOKEN, token);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initAdapter(){
        IUserClickable listener = new IUserClickable() {
            @Override
            public void onItemClick(User user) {
                Intent intent = new Intent(SearchUserActivity.this, DetailUserActivity.class);
                intent.putExtra(DetailUserActivity.EXTRA_USERNAME, username);
                intent.putExtra(DetailUserActivity.EXTRA_NAME, name);
                intent.putExtra(DetailUserActivity.EXTRA_TOKEN, token);

                intent.putExtra(DetailUserActivity.EXTRA_USER_ID, user.getUserID());
                intent.putExtra(DetailUserActivity.EXTRA_USER_NAME, user.getName());
                intent.putExtra(DetailUserActivity.EXTRA_USER_USERNAME, user.getUsername());
                startActivity(intent);
            }
        };
        searchUserAdapter = new SearchUserAdapter(listener);
        binding.rvUser.setLayoutManager(new LinearLayoutManager(this));
        binding.rvUser.setAdapter(searchUserAdapter);
    }

    private void loadData(){
        if(binding.etInput.getText().toString().equals("")){
            binding.progressBar.setVisibility(View.INVISIBLE);
            BottomSheetDialog bottomSheet = new BottomSheetDialog();
            BottomSheetDialog.message = "Please input search key in the textbox";
            bottomSheet.show(getSupportFragmentManager(), "BottomSheet");
        }else{
            binding.progressBar.setVisibility(View.VISIBLE);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    "https://it-division-kepo.herokuapp.com/searchUser",
                    getBody(),
                    response -> {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        try {
                            ArrayList<User> result = new ArrayList<>();
                            JSONArray array = response.getJSONArray("data");
                            Log.d("<RESULT>", "loadData: " + array);
                            for(int a = 0; a < array.length(); a++){
                                JSONObject jsonObject = array.getJSONObject(a);
                                String user_id = jsonObject.getString("user_id");
                                String username = jsonObject.getString("username");
                                String name = jsonObject.getString("name");

                                User user = new User();
                                user.setUserID(user_id);
                                user.setUsername(username);
                                user.setName(name);
                                result.add(user);
                            }
                            searchUserAdapter.updateData(result);
                            if(searchUserAdapter.getItemCount() == 0){
                                String text = "No Data";
                                binding.emptyData.setText(text);
                            }else{
                                String text = "";
                                binding.emptyData.setText(text);
                            }
                        }catch (Exception e){
                            BottomSheetDialog bottomSheet = new BottomSheetDialog();
                            BottomSheetDialog.message = "Something wrong occured";
                            bottomSheet.show(getSupportFragmentManager(), "BottomSheet");
                        }

                    },
                    error -> {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        BottomSheetDialog bottomSheet = new BottomSheetDialog();
                        BottomSheetDialog.message = "Something error occured";
                        bottomSheet.show(getSupportFragmentManager(), "BottomSheet");
                    }
            );
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjectRequest);
            binding.etInput.setText("");
        }
    }

    private JSONObject getBody(){
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", token);
            jsonObject.put("searchQuery", binding.etInput.getText().toString());
            return jsonObject;
        }catch (Exception ex){
            return null;
        }
    }
}