package com.example.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Task {

    private Long id;
    private String name;
    private Boolean pending = true;
    private String image;

    public Task (Long id, String name, Boolean pending, String image) {
        this.id = id;
        this.name = name;
        this.pending = pending;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPending() {
        return pending;
    }

    public void setPending(Boolean pending) {
        this.pending = pending;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s, %s, %s)", this.id, this.name, this.pending, this.image);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Task) {
            return ((Task) obj).getId() == this.id;
        }
        return false;
    }

    public static ArrayList<Task> fromJsonArrayString(String jsonArray) {
        ArrayList<Task> result = new ArrayList<>();
        Gson converter = new GsonBuilder().create();
        result.addAll(Arrays.asList(converter.fromJson(jsonArray, Task[].class)));
        return result;
    }
}
