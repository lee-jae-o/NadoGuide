package spring.tree.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.tree.dto.TourDTO;
import spring.tree.dto.UserInfoDTO;
import spring.tree.repository.entity.TourEntity;
import spring.tree.repository.TourRepository;
import spring.tree.service.ITourService;
import spring.tree.service.impl.UserInfoService;
import spring.tree.util.CmmUtil;
import spring.tree.util.DateUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TourService implements ITourService {

    private final TourRepository tourRepository;
    private final UserInfoService userInfoService;
    private final ObjectMapper objectMapper;
    @Override
    public List<TourDTO> getTourList() {
        log.info(this.getClass().getName() + ".getTourList Start!");

        List<TourEntity> rList = tourRepository.findAllByOrderByTourSeqDesc();

        List<TourDTO> nList = objectMapper.convertValue(rList, new TypeReference<>() {});

        List<TourDTO> updatedList = new ArrayList<>();
        for (TourDTO tour : nList) {
            try {
                UserInfoDTO userInfo = userInfoService.getUserIdExists(UserInfoDTO.builder().userId(tour.userId()).build());
                if ("Y".equals(userInfo.existsYn())) {
                    TourDTO updatedTour = new TourDTO(
                            tour.tourSeq(),
                            tour.userId(),
                            userInfo.nickName(),
                            userInfo.email(),
                            tour.regId(),
                            tour.regDt(),
                            tour.readCnt(),
                            tour.tourTitle(),
                            tour.tourLocation(),
                            tour.tourDate(),
                            tour.maxParticipants(),
                            tour.tourTime(),
                            tour.supplies(),
                            tour.categories(),
                            tour.tourContents(),
                            tour.imageUrl()
                    );
                    updatedList.add(updatedTour);
                } else {
                    updatedList.add(tour);
                }
            } catch (Exception e) {
                log.error("Error getting user info for userId: " + tour.userId(), e);
                updatedList.add(tour);
            }
        }

        log.info(this.getClass().getName() + ".getTourList End!");
        return updatedList;
    }


    @Transactional
    @Override
    public TourDTO getTour(TourDTO pDTO, boolean type) throws Exception {
        log.info(this.getClass().getName() + ".getTour Start!");

        if (type) {
            int res = tourRepository.updateReadCnt(pDTO.tourSeq());
            log.info("res : " + res);
        }

        TourEntity rEntity = tourRepository.findByTourSeq(pDTO.tourSeq());

        TourDTO rDTO = objectMapper.convertValue(rEntity, TourDTO.class);

//        TourDTO rDTO = new ObjectMapper().convertValue(rEntity, TourDTO.class);

        if (rDTO != null) {
            UserInfoDTO userInfo = userInfoService.getUserIdExists(UserInfoDTO.builder().userId(rDTO.userId()).build());
            if ("Y".equals(userInfo.existsYn())) {
                rDTO = new TourDTO(
                        rDTO.tourSeq(),
                        rDTO.userId(),
                        userInfo.nickName(), // 닉네임 설정
                        userInfo.email(),
                        rDTO.regId(),
                        rDTO.regDt(),
                        rDTO.readCnt(),
                        rDTO.tourTitle(),
                        rDTO.tourLocation(),
                        rDTO.tourDate(),
                        rDTO.maxParticipants(),
                        rDTO.tourTime(),
                        rDTO.supplies(),
                        rDTO.categories(),
                        rDTO.tourContents(),
                        rDTO.imageUrl()
                );
            }
        }

        log.info(this.getClass().getName() + ".getTour End!");
        return rDTO;
    }

    @Override
    public void insertTour(TourDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".insertTour Start!");

        String userId = CmmUtil.nvl(pDTO.userId());
        UserInfoDTO userInfo = userInfoService.getUserIdExists(UserInfoDTO.builder().userId(userId).build());
        String nickName = userInfo.nickName(); // 닉네임 가져오기

        String tourTitle = CmmUtil.nvl(pDTO.tourTitle());
        String tourLocation = CmmUtil.nvl(pDTO.tourLocation());
        LocalDateTime tourDate = pDTO.tourDate();
        int maxParticipants = pDTO.maxParticipants();
        String tourTime = CmmUtil.nvl(pDTO.tourTime());
        String supplies = CmmUtil.nvl(pDTO.supplies());
//        String categories = CmmUtil.nvl(pDTO.categories());
        String categories = String.join("", pDTO.categories());
        String tourContents = CmmUtil.nvl(pDTO.tourContents());
        String imageUrl = CmmUtil.nvl(pDTO.imageUrl());

        TourEntity pEntity = TourEntity.builder()
                .userId(userId)
                .regId(userId)
                .regDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                .readCnt(0L)
                .tourTitle(tourTitle)
                .tourLocation(tourLocation)
                .tourDate(tourDate)
                .maxParticipants(maxParticipants)
                .tourTime(tourTime)
                .supplies(supplies)
                .categories(categories)
                .tourContents(tourContents)
                .imageUrl(imageUrl)
                .build();

        tourRepository.save(pEntity);

        log.info(this.getClass().getName() + ".insertTour End!");
    }

    @Transactional
    @Override
    public void updateTour(TourDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".updateTour Start!");

        Long tourSeq = pDTO.tourSeq();
        String userId = CmmUtil.nvl(pDTO.userId());
        UserInfoDTO userInfo = userInfoService.getUserIdExists(UserInfoDTO.builder().userId(userId).build());
        String nickName = userInfo.nickName(); // 닉네임 가져오기

        String tourTitle = CmmUtil.nvl(pDTO.tourTitle());
        String tourLocation = CmmUtil.nvl(pDTO.tourLocation());
        LocalDateTime tourDate = pDTO.tourDate();
        int maxParticipants = pDTO.maxParticipants();
        String tourTime = CmmUtil.nvl(pDTO.tourTime());
        String supplies = CmmUtil.nvl(pDTO.supplies());
//        String categories = CmmUtil.nvl(pDTO.categories());
        String categories = String.join("", pDTO.categories());
        String tourContents = CmmUtil.nvl(pDTO.tourContents());
        String imageUrl = CmmUtil.nvl(pDTO.imageUrl());

        TourEntity rEntity = tourRepository.findByTourSeq(tourSeq);

        TourEntity pEntity = TourEntity.builder()
                .tourSeq(tourSeq)
                .userId(userId)
                .regId(rEntity.getRegId())
                .regDt(rEntity.getRegDt())
                .readCnt(rEntity.getReadCnt())
                .tourTitle(tourTitle)
                .tourLocation(tourLocation)
                .tourDate(tourDate)
                .maxParticipants(maxParticipants)
                .tourTime(tourTime)
                .supplies(supplies)
                .categories(categories)
                .tourContents(tourContents)
                .imageUrl(imageUrl)
                .build();

        tourRepository.save(pEntity);

        log.info(this.getClass().getName() + ".updateTour End!");
    }

    @Override
    public void deleteTour(TourDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".deleteTour Start!");

        Long tourSeq = pDTO.tourSeq();
        log.info("tourSeq : " + tourSeq);

        tourRepository.deleteById(tourSeq);

        log.info(this.getClass().getName() + ".deleteTour End!");
    }
}



