package lab1.controllers;

import lab1.Mappers.UserMapper;
import lab1.modelDTOs.UserResponseTo;
import lab1.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1.0", consumes = {"application/JSON"}, produces = {"application/JSON"})
public class DefaultController {
    @GetMapping
    ResponseEntity<String> GetDefPage(){
        User u1 = new User(1, "Log", "A", "AA");
        UserResponseTo uDTO = UserMapper.INSTANCE.MapUserToResponseDTO(u1);

        return new ResponseEntity<>("DEFAULT PAGE ACCESSED " + uDTO.getLogin() + " " + uDTO.getId(), HttpStatus.OK);
    }
}
