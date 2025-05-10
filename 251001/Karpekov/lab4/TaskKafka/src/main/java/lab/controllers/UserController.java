package lab.controllers;

import lab.exceptions.ForbiddenObjectException;
import lab.exceptions.IllegalFieldDataException;
import lab.mappers.UserMapper;
import lab.modelDTOs.UserRequestTo;
import lab.modelDTOs.UserResponseTo;
import lab.models.User;
import lab.services.UserService;
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
        for (User user : userService.getUsers()){
            respList.add(UserMapper.INSTANCE.MapUserToResponseDTO(user));
        }
        return new ResponseEntity<>(respList, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<UserResponseTo> GetUserById(@PathVariable("id") long id){
        User u = userService.findById(id);
        return new ResponseEntity<>(UserMapper.INSTANCE.MapUserToResponseDTO(u), HttpStatus.OK);
    }

    @PostMapping(value = "")
    ResponseEntity<UserResponseTo> CreateUser(@RequestBody UserRequestTo newUser){
        User user = UserMapper.INSTANCE.MapRequestDTOToUser(newUser);
        User createUser = userService.createUser(user);
        return new ResponseEntity<>(UserMapper.INSTANCE.MapUserToResponseDTO(createUser), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<UserResponseTo> DeleteUser(@PathVariable("id") long Id){
        userService.deleteUser(Id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "")
    ResponseEntity<UserResponseTo> UpdateUser(@RequestBody UserRequestTo updUser){
        long Id = updUser.getId();
        User user = UserMapper.INSTANCE.MapRequestDTOToUser(updUser);
        User updateUser = userService.updateUser(user);
        return new ResponseEntity<>(UserMapper.INSTANCE.MapUserToResponseDTO(updateUser), HttpStatus.OK);
    }
}
