package spring.tree.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring.tree.dto.FavoriteTourDTO;
import spring.tree.repository.FavoriteTourRepository;
import spring.tree.repository.entity.FavoriteTourEntity;
import spring.tree.service.IFavoriteTourService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteTourService implements IFavoriteTourService {

    private final FavoriteTourRepository favoriteTourRepository;

    @Override
    public void addFavoriteTour(FavoriteTourDTO favoriteTourDTO) {
        FavoriteTourEntity entity = FavoriteTourEntity.builder()
                .userId(favoriteTourDTO.userId())
                .tourSeq(favoriteTourDTO.tourSeq())
                .createdAt(LocalDateTime.now())
                .build();
        favoriteTourRepository.save(entity);
    }

    @Transactional
    @Override
    public void removeFavoriteTour(String userId, Long tourSeq) {
        favoriteTourRepository.deleteByUserIdAndTourSeq(userId, tourSeq);
    }

    @Override
    public List<FavoriteTourDTO> getFavoriteToursForUser(String userId) {
        List<FavoriteTourEntity> entities = favoriteTourRepository.findByUserId(userId);
        return entities.stream()
                .map(entity -> FavoriteTourDTO.builder()
                        .id(entity.getId())
                        .userId(entity.getUserId())
                        .tourSeq(entity.getTourSeq())
                        .createdAt(entity.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public boolean isFavorite(String userId, Long tourSeq) {
        return favoriteTourRepository.existsByUserIdAndTourSeq(userId, tourSeq);
    }
}

