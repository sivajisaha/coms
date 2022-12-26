package coms.api.userservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import coms.api.userservice.dbservice.UserService;
import coms.api.userservice.model.User;

@RestController
public class UserController {
	@Autowired
	public UserService userService;
	@GetMapping("/getallusers")
	public List<User> getallusers () {
		return userService.getallusers();
	}
	@GetMapping("/getuserbyloginid/{loginid}")
	public User getuserbyloginid(@PathVariable String loginid) {
		return userService.getuserbyloginid(loginid);
	}
	@PostMapping("/registeruser")
    public String register(@RequestBody User user) {
		int result = userService.register(user);
    	return "{\"status\":\"success\"}";
    }
}
