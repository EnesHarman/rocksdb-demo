package com.demo.rocksdb.controller;

import com.demo.rocksdb.model.User;
import com.demo.rocksdb.service.UserService;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user/")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("add")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        userService.addUser(user);
        return ResponseEntity.ok().body("The user is saved!");
    }

    @GetMapping("get-rocks/{id}")
    public ResponseEntity<User> getUserFromRocksDb(@PathVariable(name = "id") Long id) {
        User user = userService.getUserFromRocksDb(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.internalServerError().build();
    }

    @GetMapping("get/{id}")
    public ResponseEntity<User> getUser(@PathVariable(name = "id") Long id) {
        User user = userService.getUser(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.internalServerError().build();
    }

    @GetMapping("list")
    public ResponseEntity<List<User>> listUsers() {
        List<User> users = userService.listUsers();
        return ResponseEntity.ok(users);
    }

}
