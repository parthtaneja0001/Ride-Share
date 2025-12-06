package com.example.RideShare.service;


import com.example.RideShare.dto.CreateRideRequest;
import com.example.RideShare.model.Ride;
import com.example.RideShare.repository.RideRepository;
import com.example.RideShare.exception.NotFoundException;
import com.example.RideShare.exception.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideService {

    private final RideRepository rideRepository;

    public RideService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    public Ride createRide(CreateRideRequest req, String userId) {
        Ride ride = new Ride();
        ride.setPickupLocation(req.getPickupLocation());
        ride.setDropLocation(req.getDropLocation());
        ride.setUserId(userId);
        ride.setStatus("REQUESTED");
        return rideRepository.save(ride);
    }

    public List<Ride> getUserRides(String userId) {
        return rideRepository.findByUserId(userId);
    }

    public List<Ride> getPendingRides() {
        return rideRepository.findByStatus("REQUESTED");
    }

    public Ride acceptRide(String rideId, String driverId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new NotFoundException("Ride not found"));
        if (!"REQUESTED".equals(ride.getStatus())) {
            throw new BadRequestException("Ride not available to accept");
        }
        ride.setDriverId(driverId);
        ride.setStatus("ACCEPTED");
        return rideRepository.save(ride);
    }

    public Ride completeRide(String rideId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new NotFoundException("Ride not found"));
        if (!"ACCEPTED".equals(ride.getStatus())) {
            throw new BadRequestException("Ride not in ACCEPTED state");
        }
        ride.setStatus("COMPLETED");
        return rideRepository.save(ride);
    }
}
