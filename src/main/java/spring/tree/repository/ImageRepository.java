package spring.tree.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.tree.repository.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
