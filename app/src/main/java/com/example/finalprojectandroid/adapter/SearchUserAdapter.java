package com.example.finalprojectandroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectandroid.Clickable.IUserClickable;
import com.example.finalprojectandroid.databinding.SearchUserLayoutBinding;
import com.example.finalprojectandroid.model.User;

import java.util.ArrayList;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.SearchUserViewHolder>{

    private ArrayList<User> users;
    private IUserClickable listener;

    public SearchUserAdapter(IUserClickable listener) {
        this.users = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        SearchUserLayoutBinding searchUserLayoutBinding = SearchUserLayoutBinding.inflate(layoutInflater, parent, false);
        return new SearchUserViewHolder(searchUserLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUserViewHolder holder, int position) {
        User user = users.get(position);
        holder.searchUserLayoutBinding.setUser(user);

        holder.searchUserLayoutBinding.btnViewdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void updateData(ArrayList<User> newUser){
        users.clear();
        users.addAll(newUser);
        notifyDataSetChanged();
    }

    class SearchUserViewHolder extends RecyclerView.ViewHolder{

        private SearchUserLayoutBinding searchUserLayoutBinding;

        public SearchUserViewHolder(SearchUserLayoutBinding searchUserLayoutBinding) {
            super(searchUserLayoutBinding.getRoot());
            this.searchUserLayoutBinding = searchUserLayoutBinding;
        }
    }
}
