package com.example.finalprojectandroid.SharedPref;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.finalprojectandroid.model.User;

public class SharedPref {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public SharedPref(Context context){
        pref = context.getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void save(String data){
        editor.putString("data", data);
        editor.apply();
    }

    public String getData(){
        return pref.getString("data", "");
    }

    public String getUsername(){
        String data = pref.getString("data", "");
        String username = data.substring(data.indexOf("username") + 11, data.indexOf("name", data.indexOf("username") + 11) - 3);

        return username;
    }

    public String getToken(){
        String data = pref.getString("data", "");
        String token = data.substring(12, data.indexOf("username") - 3);

        return token;
    }

    public String getName(){
        String data = pref.getString("data", "");
        String name = data.substring(data.indexOf("name", data.indexOf("username") + 10) + 7, data.length() - 2);

        return name;
    }
}
