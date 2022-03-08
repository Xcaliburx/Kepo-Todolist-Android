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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalprojectandroid.Clickable.IClickable;
import com.example.finalprojectandroid.adapter.SearchTodoAdapter;
import com.example.finalprojectandroid.bottomdialog.BottomSheetDialog;
import com.example.finalprojectandroid.databinding.ActivitySearchTodoBinding;
import com.example.finalprojectandroid.model.Todo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchTodoActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_USERNAME = "extra_username";
    public static final String EXTRA_TOKEN = "extra_token";

    private ActivitySearchTodoBinding binding;
    private SearchTodoAdapter searchTodoAdapter;
    String token, username, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_todo);

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
                Intent intent = new Intent(SearchTodoActivity.this, HomeActivity.class);
                intent.putExtra(HomeActivity.EXTRA_USERNAME, username);
                intent.putExtra(HomeActivity.EXTRA_NAME, name);
                intent.putExtra(HomeActivity.EXTRA_TOKEN, token);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initAdapter(){
        IClickable listener = new IClickable() {
            @Override
            public void onItemClick(Todo todo) {
                Intent intent = new Intent(SearchTodoActivity.this, DetailTodoActivity.class);
                intent.putExtra(DetailTodoActivity.EXTRA_USERNAME, username);
                intent.putExtra(DetailTodoActivity.EXTRA_NAME, name);
                intent.putExtra(DetailTodoActivity.EXTRA_TOKEN, token);

                intent.putExtra(DetailTodoActivity.EXTRA_TODO_ID, todo.getTodo_id());
                intent.putExtra(DetailTodoActivity.EXTRA_USER_ID, todo.getUser_id());

                String empty = "";
                intent.putExtra(DetailTodoActivity.EXTRA_FROM_USER, empty);
                intent.putExtra(DetailTodoActivity.EXTRA_FROM_HOME, empty);
                startActivity(intent);
            }
        };
        searchTodoAdapter = new SearchTodoAdapter(listener);
        binding.rvSearchTodo.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSearchTodo.setAdapter(searchTodoAdapter);
    }

    private void loadData(){
        binding.progressBar.setVisibility(View.VISIBLE);
        if(!binding.cbUser.isChecked() && !binding.cbTodo.isChecked()){
            binding.progressBar.setVisibility(View.INVISIBLE);
            BottomSheetDialog bottomSheet = new BottomSheetDialog();
            BottomSheetDialog.message = "Please check one of the checkbox";
            bottomSheet.show(getSupportFragmentManager(), "BottomSheet");
        }else if(binding.etInput.getText().toString().equals("")){
            binding.progressBar.setVisibility(View.INVISIBLE);
            BottomSheetDialog bottomSheet = new BottomSheetDialog();
            BottomSheetDialog.message = "Please input search key in the textbox";
            bottomSheet.show(getSupportFragmentManager(), "BottomSheet");
        } else {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    "https://it-division-kepo.herokuapp.com/searchTodos",
                    getLoadBody(),
                    response -> {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        try {
                            ArrayList<Todo> result = new ArrayList<>();
                            JSONArray array = response.getJSONArray("data");
                            Log.d("<RESULT>", "loadData: " + array);
                            for(int a = 0; a < array.length(); a++){
                                JSONObject jsonObject = array.getJSONObject(a);
                                String user_id = jsonObject.getString("user_id");
                                String username = jsonObject.getString("username");
                                String todo_id = jsonObject.getString("todo_id");
                                String title = jsonObject.getString("title");
                                String date = jsonObject.getString("last_edited");
                                String changedDate = changeDateFormat(date);

                                Todo todo = new Todo();
                                todo.setTodo_id(todo_id);
                                todo.setUser_id(user_id);
                                todo.setUsername(username);
                                todo.setTitle(title);
                                todo.setDate(changedDate);
                                result.add(todo);
                            }
                            searchTodoAdapter.updateData(result);
                            if(searchTodoAdapter.getItemCount() == 0){
                                String text = "No Data";
                                binding.emptyData.setText(text);
                            }else{
                                String text = "";
                                binding.emptyData.setText(text);
                            }
                        } catch (Exception e) {
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

    private JSONObject getLoadBody(){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("searchQuery", binding.etInput.getText().toString());

            if(binding.cbUser.isChecked()) jsonObject.put("filterUser", 1);
            else jsonObject.put("filterUser", 0);

            if(binding.cbTodo.isChecked()) jsonObject.put("filterTodo", 1);
            else jsonObject.put("filterTodo", 0);

            return jsonObject;
        }catch (Exception e){
            return null;
        }
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