package com.example.RideShare.controller;

import jakarta.validation.Valid;
import com.example.RideShare.dto.CreateRideRequest;
import com.example.RideShare.model.Ride;
import com.example.RideShare.model.User;
import com.example.RideShare.repository.UserRepository;
import com.example.RideShare.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RideController {

    private final RideService rideService;
    private final UserRepository userRepository;

    public RideController(RideService rideService, UserRepository userRepository) {
        this.rideService = rideService;
        this.userRepository = userRepository;
    }

    @PostMapping("/rides")
    public ResponseEntity<Ride> createRide(@Valid @RequestBody CreateRideRequest req, Authentication auth) {
        String username = auth.getName();
        User u = userRepository.findByUsername(username).orElseThrow();
        if (!"ROLE_USER".equals(u.getRole())) {
            return ResponseEntity.status(403).build();
        }
        Ride ride = rideService.createRide(req, u.getId());
        return ResponseEntity.status(201).body(ride);
    }

    @PostMapping("/rides/{rideId}/complete")
    public ResponseEntity<Ride> completeRide(@PathVariable String rideId, Authentication auth) {
        String username = auth.getName();
        User u = userRepository.findByUsername(username).orElseThrow();
        Ride ride = rideService.completeRide(rideId);
        if (!u.getId().equals(ride.getUserId()) && !u.getId().equals(ride.getDriverId())) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(ride);
    }

    @GetMapping("/user/rides")
    public ResponseEntity<List<Ride>> getMyRides(Authentication auth) {
        String username = auth.getName();
        User u = userRepository.findByUsername(username).orElseThrow();
        return ResponseEntity.ok(rideService.getUserRides(u.getId()));
    }
}