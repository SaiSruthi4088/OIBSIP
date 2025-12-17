package com.online.reservation.repository;

import com.online.reservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // Find reservation by PNR
    Optional<Reservation> findByPnr(String pnr);
}
