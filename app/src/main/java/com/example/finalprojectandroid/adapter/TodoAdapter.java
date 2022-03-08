package com.example.finalprojectandroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectandroid.Clickable.IClickable;
import com.example.finalprojectandroid.databinding.MyTodoLayoutBinding;
import com.example.finalprojectandroid.model.Todo;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder>{

    private ArrayList<Todo> todos;
    private IClickable listener;
    private IClickable delete;

    public TodoAdapter(IClickable listener, IClickable delete){
        this.todos = new ArrayList<>();
        this.listener = listener;
        this.delete = delete;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MyTodoLayoutBinding myTodoLayoutBinding = MyTodoLayoutBinding.inflate(layoutInflater, parent, false);
        return new TodoViewHolder(myTodoLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Todo todo = todos.get(position);
        holder.myTodoLayoutBinding.setTodo(todo);

        holder.myTodoLayoutBinding.btnViewdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(todo);
            }
        });

        holder.myTodoLayoutBinding.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.myTodoLayoutBinding.checkbox.isChecked()) todo.setCheck(true);
                else todo.setCheck(false);
                delete.onItemClick(todo);
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

    class TodoViewHolder extends RecyclerView.ViewHolder{

        private MyTodoLayoutBinding myTodoLayoutBinding;

        public TodoViewHolder(@NonNull MyTodoLayoutBinding myTodoLayoutBinding) {
            super(myTodoLayoutBinding.getRoot());
            this.myTodoLayoutBinding = myTodoLayoutBinding;
        }
    }

}
