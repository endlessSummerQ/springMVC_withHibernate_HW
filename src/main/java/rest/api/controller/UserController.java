package rest.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rest.api.entity.User;
import rest.api.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    //список всех юзеров
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getUserList();
    }
    // один юзер по айди
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") int id) {
        return userService.getUser(id);
    }
    //регистрация
    @PostMapping("/users")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        userService.addUser(user);
        return new ResponseEntity<>("registration completed successfully!", HttpStatus.CREATED);
    }
    //логин
    @PatchMapping("/users")
    public ResponseEntity<String> login(@RequestBody User user) {
        userService.loginUser(user);
        return new ResponseEntity<>("successful login, Welcome!", HttpStatus.CREATED);
    }
    //смена пароля
    @PutMapping("/users")
    public ResponseEntity<String> changePassword(@RequestBody User user) {
        userService.changePassword(user);
        return new ResponseEntity<>("password changed successfully!", HttpStatus.ACCEPTED);
    }
}
