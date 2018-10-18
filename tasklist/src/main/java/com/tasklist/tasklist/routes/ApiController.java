package com.tasklist.tasklist.routes;

import com.tasklist.tasklist.models.Task;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    @RequestMapping(method=RequestMethod.GET,path="/task")
    public Task greeting(@RequestParam(value="name", defaultValue="To Do") String name) {
        System.out.println(String.format("Creating new task '%s'.", name));
        return new Task(name);
    }
}