package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalprojectandroid.Clickable.IClickable;
import com.example.finalprojectandroid.adapter.SearchTodoAdapter;
import com.example.finalprojectandroid.adapter.SearchUserAdapter;
import com.example.finalprojectandroid.bottomdialog.BottomSheetDialog;
import com.example.finalprojectandroid.databinding.ActivityDetailUserBinding;
import com.example.finalprojectandroid.model.Todo;
import com.example.finalprojectandroid.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DetailUserActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_USERNAME = "extra_username";
    public static final String EXTRA_TOKEN = "extra_token";

    public static final String EXTRA_USER_NAME = "extra_user_name";
    public static final String EXTRA_USER_USERNAME = "extra_user_username";
    public static final String EXTRA_USER_ID = "extra_user_id";

    private ActivityDetailUserBinding binding;
    private SearchTodoAdapter searchTodoAdapter;
    String userID, token, username, name, user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_user);

        token = (String)getIntent().getSerializableExtra(EXTRA_TOKEN);
        username = (String)getIntent().getSerializableExtra(EXTRA_USERNAME);
        name = (String)getIntent().getSerializableExtra(EXTRA_NAME);

        user_name = (String)getIntent().getSerializableExtra(EXTRA_USER_NAME);
        String user_username = (String)getIntent().getSerializableExtra(EXTRA_USER_USERNAME);
        userID = (String)getIntent().getSerializableExtra(EXTRA_USER_ID);

        binding.progressBar.setVisibility(View.VISIBLE);

        User user = new User();
        user.setName(user_name);
        user.setUsername(user_username);

        binding.setUser(user);

        initAdapter();
        loadData();

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailUserActivity.this, SearchUserActivity.class);
                intent.putExtra(SearchUserActivity.EXTRA_USERNAME, username);
                intent.putExtra(SearchUserActivity.EXTRA_NAME, name);
                intent.putExtra(SearchUserActivity.EXTRA_TOKEN, token);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initAdapter(){
        IClickable listener = new IClickable() {
            @Override
            public void onItemClick(Todo todo) {
                Intent intent = new Intent(DetailUserActivity.this, DetailTodoActivity.class);
                intent.putExtra(DetailTodoActivity.EXTRA_USERNAME, username);
                intent.putExtra(DetailTodoActivity.EXTRA_NAME, name);
                intent.putExtra(DetailTodoActivity.EXTRA_TOKEN, token);

                intent.putExtra(DetailTodoActivity.EXTRA_TODO_ID, todo.getTodo_id());
                intent.putExtra(DetailTodoActivity.EXTRA_USER_ID, userID);

                String empty = "";
                intent.putExtra(DetailTodoActivity.EXTRA_FROM_USER, "true");
                intent.putExtra(DetailTodoActivity.EXTRA_FROM_HOME, empty);
                startActivity(intent);
            }
        };
        searchTodoAdapter = new SearchTodoAdapter(listener);
        binding.rvTodo.setLayoutManager(new LinearLayoutManager(this));
        binding.rvTodo.setAdapter(searchTodoAdapter);
    }

    private void loadData(){
        Log.d("<RESULT>", "onResponse: " + "https://it-division-kepo.herokuapp.com/user/" + userID + "/todo");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "https://it-division-kepo.herokuapp.com/user/" + userID + "/todo",
                null,
                (response) -> {
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    try {
                        ArrayList<Todo> result = new ArrayList<>();
                        String message = response.getString("message");
                        JSONObject data = response.getJSONObject("data");
                        JSONArray array = data.getJSONArray("listTodo");
                        Log.d("<RESULT>", "onResponse: " + message);
                        for(int i = 0; i < array.length(); i++){
                            JSONObject jsonObject = array.getJSONObject(i);
                            String id = jsonObject.getString("todo_id");
                            String title = jsonObject.getString("title");
                            String date = jsonObject.getString("last_edited");
                            String changedDate = changeDateFormat(date);

                            Log.d("<RESULT>", "loadData: " + id + title + date);
                            Todo todo = new Todo();
                            todo.setTodo_id(id);
                            todo.setTitle(title);
                            todo.setDate(changedDate);
                            todo.setUsername(user_name);
                            result.add(todo);
                        }
                        searchTodoAdapter.updateData(result);
                        if(searchTodoAdapter.getItemCount() == 0){
                            String text = "No Data";
                            binding.emptyData.setText(text);
                        }
                        binding.countTodo.setText(String.valueOf(searchTodoAdapter.getItemCount()));
                    }catch (Exception e){
                        BottomSheetDialog bottomSheet = new BottomSheetDialog();
                        BottomSheetDialog.message = "Something wrong occured";
                        bottomSheet.show(getSupportFragmentManager(), "BottomSheet");
                    }
                },
                (error) -> {
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    BottomSheetDialog bottomSheet = new BottomSheetDialog();
                    BottomSheetDialog.message = "Something wrong occured";
                    bottomSheet.show(getSupportFragmentManager(), "BottomSheet");
                }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    private String changeDateFormat(String dates){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date date = null;
        try {
            date = fmt.parse(dates);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("dd MMM yyyy HH:mm");

        return fmtOut.format(date);
    }
}