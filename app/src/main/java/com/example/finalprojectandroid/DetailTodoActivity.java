package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalprojectandroid.databinding.ActivityDetailTodoBinding;
import com.example.finalprojectandroid.model.Todo;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailTodoActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_USERNAME = "extra_username";
    public static final String EXTRA_TOKEN = "extra_token";

    public static final String EXTRA_TODO_ID = "extra_todo_id";
    public static final String EXTRA_USER_ID = "extra_user_id";

    public static final String EXTRA_FROM_USER = "extra_from_user";
    public static final String EXTRA_FROM_HOME = "extra_from_home";

    public static final String EXTRA_VALUE = "extra_value";

    private ActivityDetailTodoBinding binding;
    String todoID, userID;
    Todo todo = new Todo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_todo);

        binding.progressBar.setVisibility(View.INVISIBLE);
        String token = (String)getIntent().getSerializableExtra(EXTRA_TOKEN);
        String username = (String)getIntent().getSerializableExtra(EXTRA_USERNAME);
        String name = (String)getIntent().getSerializableExtra(EXTRA_NAME);

        todoID = (String)getIntent().getSerializableExtra(EXTRA_TODO_ID);
        userID = (String)getIntent().getSerializableExtra(EXTRA_USER_ID);

        String fromUser = (String)getIntent().getSerializableExtra(EXTRA_FROM_USER);
        String fromHome = (String)getIntent().getSerializableExtra(EXTRA_FROM_HOME);

        String lastSearch = (String)getIntent().getSerializableExtra(EXTRA_VALUE);

        loadData();

        binding.setTodo(todo);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fromHome.equals("true")){
                    Intent intent = new Intent(DetailTodoActivity.this, TodoActivity.class);
                    intent.putExtra(TodoActivity.EXTRA_USERNAME, username);
                    intent.putExtra(TodoActivity.EXTRA_NAME, name);
                    intent.putExtra(TodoActivity.EXTRA_TOKEN, token);
                    startActivity(intent);
                    finish();
                }else if(fromUser.equals("true")){
                    Intent intent = new Intent(DetailTodoActivity.this, SearchUserActivity.class);
                    intent.putExtra(SearchUserActivity.EXTRA_USERNAME, username);
                    intent.putExtra(SearchUserActivity.EXTRA_NAME, name);
                    intent.putExtra(SearchUserActivity.EXTRA_TOKEN, token);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(DetailTodoActivity.this, SearchTodoActivity.class);
                    intent.putExtra(SearchTodoActivity.EXTRA_USERNAME, username);
                    intent.putExtra(SearchTodoActivity.EXTRA_NAME, name);
                    intent.putExtra(SearchTodoActivity.EXTRA_TOKEN, token);
                    startActivity(intent);
                    finish();
                }
            }
        });

        if(!userID.equals(token)){
            binding.btnEditTodo.setVisibility(View.INVISIBLE);
        }

        binding.btnEditTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailTodoActivity.this, TodoFormActivity.class);
                intent.putExtra(TodoFormActivity.EXTRA_USERNAME, username);
                intent.putExtra(TodoFormActivity.EXTRA_NAME, name);
                intent.putExtra(TodoFormActivity.EXTRA_TOKEN, token);

                intent.putExtra(TodoFormActivity.EXTRA_TITLE, todo.getTitle());
                intent.putExtra(TodoFormActivity.EXTRA_DESCRIPTION, todo.getDesciption());
                intent.putExtra(TodoFormActivity.ExTRA_TODOID, todoID);
                intent.putExtra(TodoFormActivity.EXTRA_USERID, userID);
                startActivity(intent);
            }
        });
    }

    private void loadData(){
        binding.progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "https://it-division-kepo.herokuapp.com/user/" + userID + "/todo/" + todoID,
                null,
                response -> {
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    try {
                        JSONObject data = response.getJSONObject("data");
                        String title = data.getString("title");
                        String description = data.getString("description");
                        String date = data.getString("last_edited");
                        String changedDate = changeDateFormat(date);

                        todo.setDate(changedDate);
                        todo.setDesciption(description);
                        todo.setTitle(title);
                    }catch (Exception ex){

                    }
                },
                error -> {
                    binding.progressBar.setVisibility(View.INVISIBLE);
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