package spring.tree.service;

import spring.tree.dto.FavoriteTourDTO;

import java.util.List;

public interface IFavoriteTourService {

    void addFavoriteTour(FavoriteTourDTO favoriteTourDTO);

    void removeFavoriteTour(String userId, Long tourSeq);

    List<FavoriteTourDTO> getFavoriteToursForUser(String userId);

    boolean isFavorite(String userId, Long tourSeq);
}
