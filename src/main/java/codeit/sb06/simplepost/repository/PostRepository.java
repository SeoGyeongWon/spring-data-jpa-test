package codeit.sb06.simplepost.repository;

import codeit.sb06.simplepost.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}