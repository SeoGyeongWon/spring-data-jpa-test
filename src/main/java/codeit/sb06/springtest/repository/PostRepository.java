package codeit.sb06.springtest.repository;

import codeit.sb06.springtest.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}