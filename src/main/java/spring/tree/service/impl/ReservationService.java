package spring.tree.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring.tree.dto.MailDTO;
import spring.tree.dto.ReservationDTO;
import spring.tree.dto.TourDTO;
import spring.tree.dto.UserInfoDTO;
import spring.tree.repository.ReservationRepository;
import spring.tree.repository.entity.ReservationEntity;
import spring.tree.service.IReservationService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService implements IReservationService {

    private final ReservationRepository reservationRepository;
    private final TourService tourService;
    private final UserInfoService userInfoService;
    private final MailService mailService;

    @Override
    public void makeReservation(ReservationDTO reservationDTO) {
        log.info("makeReservation Start!");

        // 예약 정보 저장
        ReservationEntity reservationEntity = ReservationEntity.builder()
                .tourSeq(reservationDTO.tourSeq())
                .userId(reservationDTO.userId())
                .reservationDate(LocalDateTime.now())
                .build();

        reservationRepository.save(reservationEntity);

        try {
            // 투어 정보 및 등록자 이메일 가져오기
            TourDTO tour = tourService.getTour(TourDTO.builder().tourSeq(reservationDTO.tourSeq()).build(), false);
            UserInfoDTO tourOwner = userInfoService.getUserInfoByUserId(tour.userId());

            // 예약자 정보 가져오기
            UserInfoDTO reservationUser = userInfoService.getUserInfoByUserId(reservationDTO.userId());

            // 이메일 발송
            MailDTO mailDTO = MailDTO.builder()
                    .toMail(tourOwner.email())
                    .title("[투어 예약 알림] ")
                    .contents(reservationUser.nickName() + "님이 " + tour.tourTitle() + " 투어를 예약하셨습니다.")
                    .build();

            mailService.doSendMail(mailDTO);

        } catch (Exception e) {
            log.error("Error fetching tour or sending email: ", e);
        }

        log.info("makeReservation End!");
    }

    @Override
    public List<ReservationDTO> getReservationsForTour(Long tourSeq) {
        List<ReservationEntity> reservations = reservationRepository.findByTourSeq(tourSeq);
        return reservations.stream()
                .map(entity -> ReservationDTO.builder()
                        .reservationId(entity.getReservationId())
                        .tourSeq(entity.getTourSeq())
                        .userId(entity.getUserId())
                        .reservationDate(entity.getReservationDate())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationDTO> getReservationsForUser(String userId) {
        List<ReservationEntity> reservations = reservationRepository.findByUserId(userId);
        return reservations.stream()
                .map(entity -> ReservationDTO.builder()
                        .reservationId(entity.getReservationId())
                        .tourSeq(entity.getTourSeq())
                        .userId(entity.getUserId())
                        .reservationDate(entity.getReservationDate())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAlreadyReserved(Long tourSeq, String userId) {
        return reservationRepository.existsByTourSeqAndUserId(tourSeq, userId);
    }

    @Override
    public int getReservedCount(Long tourSeq) {
        return reservationRepository.countByTourSeq(tourSeq);
    }

    @Override
    public void cancelReservation(Long reservationId) {
        log.info("cancelReservation Start!");

        try {
            // 예약 정보 가져오기
            ReservationEntity reservation = reservationRepository.findById(reservationId)
                    .orElseThrow(() -> new Exception("Reservation not found"));

            // 투어 정보 및 투어 등록자 정보 가져오기
            TourDTO tour = tourService.getTour(TourDTO.builder().tourSeq(reservation.getTourSeq()).build(), false);
            UserInfoDTO tourOwner = userInfoService.getUserInfoByUserId(tour.userId());

            // 예약 취소한 사용자 정보 가져오기
            UserInfoDTO reservationUser = userInfoService.getUserInfoByUserId(reservation.getUserId());

            // 이메일 발송
            MailDTO mailDTO = MailDTO.builder()
                    .toMail(tourOwner.email())
                    .title("[투어 예약 취소 알림] ")
                    .contents(reservationUser.nickName() + "님이 " + tour.tourTitle() + " 투어 예약을 취소하였습니다.")
                    .build();

            mailService.doSendMail(mailDTO);

            // 예약 삭제
            reservationRepository.deleteById(reservationId);

            log.info("Reservation cancelled and email sent.");

        } catch (Exception e) {
            log.error("Error cancelling reservation or sending email: ", e);
        }

        log.info("cancelReservation End!");
    }

}


