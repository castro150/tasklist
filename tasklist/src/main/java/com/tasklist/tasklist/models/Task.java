package com.tasklist.tasklist.models;

import lombok.Getter;
import lombok.Setter;

public class Task {

    @Getter
    @Setter
    private String name;

    public Task(String name) {
        this.name = name;
    }
}