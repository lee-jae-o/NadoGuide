package spring.tree.repository;

import spring.tree.repository.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, String> {
    Optional<UserInfoEntity> findByUserId(String userId);

    Optional<UserInfoEntity> findByNickName(String nickName);

    Optional<UserInfoEntity> findByUserIdAndPassword(String userId, String password);

    Optional<UserInfoEntity> findByEmail(String email);

    Optional<UserInfoEntity> findByUserNameAndEmail(String email, String userName);

    Optional<UserInfoEntity> findByUserIdAndUserNameAndEmail(String userId, String email, String userName);

    List<UserInfoEntity> findAllByUserIdOrderByUserIdDesc(String userId);


}