package com.example.finalprojectandroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalprojectandroid.Clickable.IClickable;
import com.example.finalprojectandroid.adapter.TodoAdapter;
import com.example.finalprojectandroid.bottomdialog.BottomSheetDialog;
import com.example.finalprojectandroid.databinding.ActivityTodoBinding;
import com.example.finalprojectandroid.model.Todo;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TodoActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_USERNAME = "extra_username";
    public static final String EXTRA_TOKEN = "extra_token";
    String token, username, name;
    private ActivityTodoBinding binding;
    private TodoAdapter todoAdapter;
    private ArrayList<Todo> deleteTodo = new ArrayList<>();
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_todo);

        token = (String)getIntent().getSerializableExtra(EXTRA_TOKEN);
        username = (String)getIntent().getSerializableExtra(EXTRA_USERNAME);
        name = (String)getIntent().getSerializableExtra(EXTRA_NAME);

        initAdapter();
        loadData();

        Todo todo = new Todo();

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodoActivity.this, HomeActivity.class);
                intent.putExtra(HomeActivity.EXTRA_USERNAME, username);
                intent.putExtra(HomeActivity.EXTRA_NAME, name);
                intent.putExtra(HomeActivity.EXTRA_TOKEN, token);
                startActivity(intent);
                finish();
            }
        });

        binding.btnCreateTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodoActivity.this, TodoFormActivity.class);
                intent.putExtra(TodoFormActivity.EXTRA_USERNAME, username);
                intent.putExtra(TodoFormActivity.EXTRA_NAME, name);
                intent.putExtra(TodoFormActivity.EXTRA_TOKEN, token);
                String empty = "";
                intent.putExtra(TodoFormActivity.EXTRA_TITLE, empty);
                intent.putExtra(TodoFormActivity.EXTRA_DESCRIPTION, empty);
                intent.putExtra(TodoFormActivity.ExTRA_TODOID, empty);
                intent.putExtra(TodoFormActivity.EXTRA_USERID,empty);
                startActivity(intent);
            }
        });
    }

    private void initAdapter(){
        IClickable listener = new IClickable() {
            @Override
            public void onItemClick(Todo todo) {
                Intent intent = new Intent(TodoActivity.this, DetailTodoActivity.class);
                intent.putExtra(DetailTodoActivity.EXTRA_USERNAME, username);
                intent.putExtra(DetailTodoActivity.EXTRA_NAME, name);
                intent.putExtra(DetailTodoActivity.EXTRA_TOKEN, token);

                intent.putExtra(DetailTodoActivity.EXTRA_TODO_ID, todo.getTodo_id());
                intent.putExtra(DetailTodoActivity.EXTRA_USER_ID, token);

                String empty = "";
                intent.putExtra(DetailTodoActivity.EXTRA_FROM_USER, empty);
                intent.putExtra(DetailTodoActivity.EXTRA_FROM_HOME, "true");
                startActivity(intent);
            }
        };
        IClickable delete = new IClickable() {
            @Override
            public void onItemClick(Todo todo) {
                if(todo.getCheck()) deleteTodo.add(todo);
                else deleteTodo.remove(todo);
                if(deleteTodo.size() > 0) getSnackBar();
                if(deleteTodo.isEmpty()) snackbar.dismiss();
            }
        };
        todoAdapter = new TodoAdapter(listener, delete);
        binding.rvTodo.setLayoutManager(new LinearLayoutManager(this));
        binding.rvTodo.setAdapter(todoAdapter);
    }

    private void getSnackBar(){
        snackbar = Snackbar.make(binding.rlMytodo, String.valueOf(deleteTodo.size()) + " item(s)", Snackbar.LENGTH_INDEFINITE)
                .setAction("DELETE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getAlertConfirm();
                    }
                });
        snackbar.setActionTextColor(getResources().getColor(R.color.red));
        snackbar.show();
    }

    private void getAlertConfirm(){
        AlertDialog.Builder builder = new AlertDialog.Builder(TodoActivity.this);

        builder.setTitle("Delete Todo")
                .setMessage("Are you sure want to delete all this todos?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteTodo();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        snackbar.show();
                    }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteTodo(){
        binding.progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                "https://it-division-kepo.herokuapp.com/user/" + token + "/deleteTodo",
                getDeleteBody(),
                response -> {
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    try {
                        Toast.makeText(this, "Success delete Todo", Toast.LENGTH_LONG).show();
                        deleteTodo.clear();
                        loadData();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                },
                error -> {
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    error.printStackTrace();
                }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    private JSONObject getDeleteBody(){
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for(int a = 0; a < deleteTodo.size(); a++){
                String id = deleteTodo.get(a).getTodo_id();
                jsonArray.put(id);
            }
            jsonObject.put("todos", jsonArray);
            return jsonObject;
        }catch (Exception e){
            return null;
        }
    }

    private void loadData(){
        binding.progressBar.setVisibility(View.VISIBLE);
        Log.d("<RESULT>", "onResponse: " + "https://it-division-kepo.herokuapp.com/user/" + token + "/todo");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "https://it-division-kepo.herokuapp.com/user/" + token + "/todo",
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
                            result.add(todo);
                        }
                        todoAdapter.updateData(result);
                        if(todoAdapter.getItemCount() == 0){
                            String text = "No Data";
                            binding.emptyData.setText(text);
                        }
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