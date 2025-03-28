package lab1.controllers;

import lab1.Mappers.UserMapper;
import lab1.modelDTOs.UserRequestTo;
import lab1.modelDTOs.UserResponseTo;
import lab1.models.User;
import lab1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1.0/users", consumes = {"application/JSON"}, produces = {"application/JSON"})
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "")
    ResponseEntity<List<UserResponseTo>> GetUsers(){
        List<UserResponseTo> respList = new ArrayList<>();
        for (User user : userService.GetUsers()){
            respList.add(UserMapper.INSTANCE.MapUserToResponseDTO(user));
        }
        return new ResponseEntity<>(respList, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<UserResponseTo> GetUserById(@PathVariable("id") long id){
        User u = userService.GetUserById(id);
        if (u != null)
            return new ResponseEntity<>(UserMapper.INSTANCE.MapUserToResponseDTO(u), HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "")
    ResponseEntity<UserResponseTo> CreateUser(@RequestBody UserRequestTo newUser){
        User newU = UserMapper.INSTANCE.MapRequestDTOToUser(newUser);
        boolean added = userService.CreateUser(newU);
        if (added)
            return new ResponseEntity<>(UserMapper.INSTANCE.MapUserToResponseDTO(newU), HttpStatus.CREATED);
        else
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<UserResponseTo> DeleteUser(@PathVariable("id") long Id){
        User del = userService.DeleteUser(Id);
        if (del == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping(value = "")
    ResponseEntity<UserResponseTo> UpdateUser(@RequestBody UserRequestTo updUser){
        long Id = updUser.getId();
        User newU = UserMapper.INSTANCE.MapRequestDTOToUser(updUser);
        User updated = userService.UpdateUser(newU);
        if (updated != null)
            return new ResponseEntity<>(UserMapper.INSTANCE.MapUserToResponseDTO(newU), HttpStatus.OK);
        else
            return new ResponseEntity<>(UserMapper.INSTANCE.MapUserToResponseDTO(newU), HttpStatus.NOT_FOUND);
    }

}
