package spring.tree.service;

import spring.tree.dto.ReservationDTO;

import java.util.List;

public interface IReservationService {

    void makeReservation(ReservationDTO reservationDTO);
    List<ReservationDTO> getReservationsForTour(Long tourSeq);

    List<ReservationDTO> getReservationsForUser(String userId);

    boolean isAlreadyReserved(Long tourSeq, String userId);

    int getReservedCount(Long tourSeq);

    void cancelReservation(Long reservationId);
}
