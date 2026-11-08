package com.mitra.ai.controller;

import com.mitra.ai.dto.UserDTO;
import com.mitra.ai.entity.Trip;
import com.mitra.ai.entity.User;
import com.mitra.ai.service.TripService;
import com.mitra.ai.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final TripService tripService;

    public UserController(
            UserService userService,
            TripService tripService) {

        this.userService = userService;
        this.tripService = tripService;
    }

    // CREATE USER
    @PostMapping
    public ResponseEntity<User> createUser(
            @Valid @RequestBody UserDTO userDTO) {

        User user = new User(
                userDTO.getName(),
                userDTO.getEmail()
        );

        User createdUser = userService.createUser(user);

        return new ResponseEntity<>(
                createdUser,
                HttpStatus.CREATED
        );
    }

    // GET ALL USERS
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }

    // GET USER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @PathVariable Long id) {

        Optional<User> user =
                userService.getUserById(id);

        return user
                .map(ResponseEntity::ok)
                .orElseGet(
                        () -> ResponseEntity.notFound().build()
                );
    }

    // GET TRIPS BELONGING TO A USER
    @GetMapping("/{userId}/trips")
    public ResponseEntity<List<Trip>> getUserTrips(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                tripService.getTripsByUser(userId)
        );
    }

    // GET USER BY EMAIL
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(
            @PathVariable String email) {

        Optional<User> user =
                userService.getUserByEmail(email);

        return user
                .map(ResponseEntity::ok)
                .orElseGet(
                        () -> ResponseEntity.notFound().build()
                );
    }

    // UPDATE USER
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDTO userDTO) {

        User updatedUser = new User(
                userDTO.getName(),
                userDTO.getEmail()
        );

        User user =
                userService.updateUser(id, updatedUser);

        return ResponseEntity.ok(user);
    }

    // DELETE USER
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}