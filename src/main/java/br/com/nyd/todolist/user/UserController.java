package br.com.nyd.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository repository;

    @GetMapping
    public ResponseEntity listAll(){
        return  ResponseEntity.ok().body(repository.findAll());
    }

    @PostMapping
    public ResponseEntity create(@RequestBody UserModel userModel){
        var user = repository.findByUsername(userModel.getUsername());
        if(user != null){
            return ResponseEntity.badRequest().body("User already exists");
        }
        var passwordHashed = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(passwordHashed);
        return ResponseEntity.ok().body(repository.save(userModel));
    }
}
