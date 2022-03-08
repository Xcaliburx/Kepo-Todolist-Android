package com.example.finalprojectandroid.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.finalprojectandroid.BR;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Todo extends BaseObservable {

    private String todo_id;
    private String title;
    private String date;
    private String desciption;
    private Boolean check;
    private String user_id;
    private String username;

    @Bindable
    public String getTodo_id() {
        return todo_id;
    }

    public void setTodo_id(String todo_id) {
        this.todo_id = todo_id;
        notifyPropertyChanged(BR.todo_id);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        notifyPropertyChanged(BR.date);
    }

    @Bindable
    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
        notifyPropertyChanged(BR.desciption);
    }

    @Bindable
    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
        notifyPropertyChanged(BR.check);
    }

    @Bindable
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
        notifyPropertyChanged(BR.user_id);
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }
}
