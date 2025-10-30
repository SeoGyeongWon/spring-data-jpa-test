package codeit.sb06.simplepost.repository;

import codeit.sb06.simplepost.entity.Post;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PostRepositoryBasicTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("save 메서드는 Post 엔티티를 데이터베이스에 저장해야 한다")
    void save_post_entity() {
        // given
        Post post = Post.builder()
                .author("codeit")
                .title("Test title")
                .content("Test Content")
                .password("123456")
                .tags(List.of("spring", "jpa"))
                .build();

        // when
        Post savedPost = postRepository.save(post);

        // then
        assertNotNull(savedPost.getId());
        assertEquals("codeit", savedPost.getAuthor());
    }

    @Test
    @DisplayName("findById 메서드는 ID에 해당하는 Post 엔티티를 DB에서 찾아야 한다")
    void find_post_by_id() {
        // given
        Post post = Post.builder()
                .author("codeit")
                .title("Find test")
                .content("Content for find test")
                .password("123456")
                .build();
        postRepository.save(post);

        em.clear();

        // when
        Post foundPost = postRepository.findById(post.getId()).orElse(null);

        // then
        assertNotNull(foundPost);
        assertEquals("Find test", foundPost.getTitle());
    }
}
