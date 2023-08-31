package org.adaschool.api.controller.user;

import org.adaschool.api.exception.UserNotFoundException;
import org.adaschool.api.repository.user.User;
import org.adaschool.api.repository.user.UserDto;
import org.adaschool.api.service.user.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/users/")
public class UsersController {

    private final UsersService usersService;

    public UsersController(@Autowired UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto newUser) {
        User userToAdd = new User(newUser);

        URI createdUserUri = URI.create("");
        return ResponseEntity.created(createdUserUri).body(usersService.save(userToAdd));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(usersService.all());
    }

    @GetMapping("{id}")
    public ResponseEntity<User> findById(@PathVariable("id") String id) {
        return ResponseEntity.ok(usersService.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@RequestBody UserDto newUser, @PathVariable("id") String id) {
        Optional<User> userToUpdate = usersService.findById(id);
        if (userToUpdate.isEmpty()) {
            throw new UserNotFoundException(id);
        }

        usersService.save(userToUpdate.get());
        return ResponseEntity.ok(userToUpdate.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        usersService.findById(id).ifPresentOrElse(
                action -> this.usersService.deleteById(id),
                () -> { throw new UserNotFoundException(id); }
        );
        return ResponseEntity.ok().build();
    }
}
