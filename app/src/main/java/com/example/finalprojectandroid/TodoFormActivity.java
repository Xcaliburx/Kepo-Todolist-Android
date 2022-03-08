package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalprojectandroid.bottomdialog.BottomSheetDialog;
import com.example.finalprojectandroid.databinding.ActivityTodoFormBinding;
import com.example.finalprojectandroid.model.Todo;

import org.json.JSONObject;

public class TodoFormActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_USERNAME = "extra_username";
    public static final String EXTRA_TOKEN = "extra_token";

    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_DESCRIPTION = "extra_description";
    public static final String ExTRA_TODOID = "extra_todoid";
    public static final String EXTRA_USERID = "extra_userid";

    private ActivityTodoFormBinding binding;
    String token, username, name;
    String title, description, todoID, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_todo_form);

        binding.progressBar.setVisibility(View.INVISIBLE);
        token = (String)getIntent().getSerializableExtra(EXTRA_TOKEN);
        username = (String)getIntent().getSerializableExtra(EXTRA_USERNAME);
        name = (String)getIntent().getSerializableExtra(EXTRA_NAME);

        title = (String)getIntent().getSerializableExtra(EXTRA_TITLE);
        description = (String)getIntent().getSerializableExtra(EXTRA_DESCRIPTION);
        todoID = (String)getIntent().getSerializableExtra(ExTRA_TODOID);
        userID = (String)getIntent().getSerializableExtra(EXTRA_USERID);

        binding.description.addTextChangedListener(textWatcher);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodoFormActivity.this, TodoActivity.class);
                intent.putExtra(TodoActivity.EXTRA_USERNAME, username);
                intent.putExtra(TodoActivity.EXTRA_NAME, name);
                intent.putExtra(TodoActivity.EXTRA_TOKEN, token);
                startActivity(intent);
                finish();
            }
        });

        Todo todo = new Todo();
        todo.setTitle(title);
        todo.setDesciption(description);

        if(title.equals("") && description.equals("") && todoID.equals("")){
            binding.titleForm.setText("Create Todo");
            binding.setTodo(new Todo());
            binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createTodo();
                }
            });
        }else{
            binding.titleForm.setText("Update Todo");
            binding.setTodo(todo);
            binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateTodo();
                }
            });
        }
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String text = charSequence.toString();
            binding.numCount.setText(String.valueOf(text.length()));
            if(text.length() > 100){
                binding.numCount.setTextColor(Color.RED);
                binding.maxNum.setTextColor(Color.RED);
                String error = "Your description exceeded the maximum words";
                binding.errorMessage.setText(error);
                binding.btnConfirm.setEnabled(false);
            }else{
                binding.numCount.setTextColor(Color.GRAY);
                binding.maxNum.setTextColor(Color.GRAY);
                String error = "";
                binding.errorMessage.setText(error);
                binding.btnConfirm.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void createTodo(){
        Todo todo = binding.getTodo();
        if(binding.title.getText().toString().equals("") || binding.description.getText().toString().equals("")) {
            String error = "Text field cannot be empty";
            binding.errorMessage.setText(error);
        }else{
            binding.progressBar.setVisibility(View.VISIBLE);
            if(todo.getTitle() != null && todo.getDesciption() != null){
                String error = "";
                binding.errorMessage.setText(error);
            }
            Log.d("<RESULT>", "onResponse: " + "https://it-division-kepo.herokuapp.com/user/" + token + "/todo");
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    "https://it-division-kepo.herokuapp.com/user/" + token + "/todo",
                    getDataTodo(todo),
                    (response) -> {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        Log.d("<RESULT>", "createTodo: masuk gan");
                        Toast.makeText(this, "Todo created successfully", Toast.LENGTH_LONG).show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(TodoFormActivity.this, TodoActivity.class);
                                intent.putExtra(TodoActivity.EXTRA_USERNAME, username);
                                intent.putExtra(TodoActivity.EXTRA_NAME, name);
                                intent.putExtra(TodoActivity.EXTRA_TOKEN, token);
                                startActivity(intent);
                                finish();
                            }
                        }, 3500);
                    },
                    error -> {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        Log.d("<RESULT>", "createTodo: error gan");
                        String err = "Insert data failed, Something went wrong";
                        binding.errorMessage.setText(err);
                    }
            );
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjectRequest);
        }
    }

    private void updateTodo(){
        Todo todos = binding.getTodo();
        if(binding.title.getText().toString().equals("") || binding.description.getText().toString().equals("")) {
            String error = "Text field cannot be empty";
            binding.errorMessage.setText(error);
        }else {
            binding.progressBar.setVisibility(View.VISIBLE);
            if(todos.getTitle() != null && todos.getDesciption() != null){
                String error = "";
                binding.errorMessage.setText(error);
            }
            Log.d("<RESULT>", "updateTodo: " + "https://it-division-kepo.herokuapp.com/user/" + userID + "/todo/" + todoID);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.PUT,
                    "https://it-division-kepo.herokuapp.com/user/" + userID + "/todo/" + todoID,
                    getDataTodo(todos),
                    response -> {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(this, "Todo updated successfully", Toast.LENGTH_LONG).show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(TodoFormActivity.this, DetailTodoActivity.class);
                                intent.putExtra(DetailTodoActivity.EXTRA_USERNAME, username);
                                intent.putExtra(DetailTodoActivity.EXTRA_NAME, name);
                                intent.putExtra(DetailTodoActivity.EXTRA_TOKEN, token);

                                intent.putExtra(DetailTodoActivity.EXTRA_TODO_ID, todoID);
                                intent.putExtra(DetailTodoActivity.EXTRA_USER_ID, userID);

                                String empty = "";
                                intent.putExtra(DetailTodoActivity.EXTRA_FROM_USER, empty);
                                intent.putExtra(DetailTodoActivity.EXTRA_FROM_HOME, "true");
                                startActivity(intent);
                                startActivity(intent);
                                finish();
                            }
                        }, 3500);
                    },
                    error -> {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        String err = "Insert data failed, Something went wrong";
                        binding.errorMessage.setText(err);
                    }
            );
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjectRequest);
        }
    }

    private JSONObject getDataTodo(Todo todo){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", todo.getTitle());
            jsonObject.put("description", todo.getDesciption());
            return jsonObject;
        }catch (Exception ex){
            return null;
        }
    }
}