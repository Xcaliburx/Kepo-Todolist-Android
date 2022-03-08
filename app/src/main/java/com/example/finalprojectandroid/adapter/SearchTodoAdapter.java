package com.example.finalprojectandroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectandroid.Clickable.IClickable;
import com.example.finalprojectandroid.databinding.SearchTodoLayoutBinding;
import com.example.finalprojectandroid.model.Todo;

import java.util.ArrayList;

public class SearchTodoAdapter extends RecyclerView.Adapter<SearchTodoAdapter.SearchTodoViewHolder>{

    private ArrayList<Todo> todos;
    private IClickable listener;

    public SearchTodoAdapter(IClickable listener) {
        this.todos = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchTodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        SearchTodoLayoutBinding searchTodoLayoutBinding = SearchTodoLayoutBinding.inflate(layoutInflater, parent, false);
        return new SearchTodoViewHolder(searchTodoLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchTodoViewHolder holder, int position) {
        Todo todo = todos.get(position);
        holder.searchTodoLayoutBinding.setTodo(todo);

        holder.searchTodoLayoutBinding.btnViewdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(todo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    public void updateData(ArrayList<Todo> newTodo){
        todos.clear();
        todos.addAll(newTodo);
        notifyDataSetChanged();
    }

    class SearchTodoViewHolder extends RecyclerView.ViewHolder{

        private SearchTodoLayoutBinding searchTodoLayoutBinding;

        public SearchTodoViewHolder(@NonNull SearchTodoLayoutBinding searchTodoLayoutBinding) {
            super(searchTodoLayoutBinding.getRoot());
            this.searchTodoLayoutBinding = searchTodoLayoutBinding;
        }
    }
}
