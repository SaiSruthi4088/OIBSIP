package com.online.reservation.controller;

import com.online.reservation.model.Reservation;
import com.online.reservation.repository.ReservationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationRepository reservationRepository;

    public ReservationController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    // GET all reservations
    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // GET reservation by ID
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        return reservation.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }

    // POST create a new reservation with automatic PNR
    @PostMapping
    public Reservation createReservation(@RequestBody Reservation reservation) {
        // Generate a unique PNR
        String pnr = "RES" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        reservation.setPnr(pnr);
        return reservationRepository.save(reservation);
    }

    // PUT update a reservation by ID
    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(
            @PathVariable Long id, @RequestBody Reservation reservationDetails) {

        return reservationRepository.findById(id).map(reservation -> {
            reservation.setName(reservationDetails.getName());
            reservation.setEmail(reservationDetails.getEmail());
            reservation.setDate(reservationDetails.getDate());
            reservation.setTrainNumber(reservationDetails.getTrainNumber());
            reservation.setTrainName(reservationDetails.getTrainName());
            reservation.setClassType(reservationDetails.getClassType());
            reservation.setFromPlace(reservationDetails.getFromPlace());
            reservation.setToPlace(reservationDetails.getToPlace());
            Reservation updated = reservationRepository.save(reservation);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE a reservation by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        return reservationRepository.findById(id).map(reservation -> {
            reservationRepository.delete(reservation);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

// DELETE a reservation by PNR
@DeleteMapping("/cancel/{pnr}")
public ResponseEntity<String> cancelReservation(@PathVariable String pnr) {
    Optional<Reservation> reservation = reservationRepository.findByPnr(pnr);
    if (reservation.isPresent()) {
        reservationRepository.delete(reservation.get());
        return ResponseEntity.ok("Reservation with PNR " + pnr + " cancelled successfully");
    } else {
        return ResponseEntity.status(404).body("Reservation with PNR " + pnr + " not found");
    }
}
}

