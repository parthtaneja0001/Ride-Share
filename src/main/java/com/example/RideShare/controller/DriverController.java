package com.example.RideShare.controller;

import com.example.RideShare.model.Ride;
import com.example.RideShare.model.User;
import com.example.RideShare.repository.UserRepository;
import com.example.RideShare.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/driver/rides")
public class DriverController {

    private final RideService rideService;
    private final UserRepository userRepository;

    public DriverController(RideService rideService, UserRepository userRepository) {
        this.rideService = rideService;
        this.userRepository = userRepository;
    }

    @GetMapping("/requests")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public ResponseEntity<List<Ride>> getRequests() {
        return ResponseEntity.ok(rideService.getPendingRides());
    }

    @PostMapping("/{rideId}/accept")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public ResponseEntity<Ride> acceptRide(@PathVariable String rideId, Authentication auth) {
        String username = auth.getName();
        User u = userRepository.findByUsername(username).orElseThrow();
        Ride updated = rideService.acceptRide(rideId, u.getId());
        return ResponseEntity.ok(updated);
    }
}
