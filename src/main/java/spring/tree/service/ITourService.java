package spring.tree.service;

import spring.tree.dto.TourDTO;

import java.util.List;

public interface ITourService {

    void insertTour(TourDTO pDTO) throws Exception; // 해당 투어 저장하기

    List<TourDTO> getTourList(); // 등록된 투어 전체 가져오기

    TourDTO getTour(TourDTO pDTO, boolean type) throws Exception; // 등록된 투어 상세 정보 가져오기

    void updateTour(TourDTO pDTO) throws Exception;

    void deleteTour(TourDTO pDTO) throws Exception;

}
