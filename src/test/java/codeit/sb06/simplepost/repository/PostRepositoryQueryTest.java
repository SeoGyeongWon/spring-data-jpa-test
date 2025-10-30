package codeit.sb06.simplepost.repository;

import codeit.sb06.simplepost.entity.Post;
import codeit.sb06.simplepost.support.PostFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PostRepositoryQueryTest {

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        postRepository.save(PostFixture.createPost("codeit", "JPA Test"));
        postRepository.save(PostFixture.createPost("codeit", "Spring Boot Test"));
        postRepository.save(PostFixture.createPost("user", "Another JPA Test"));
    }


    @Test
    @DisplayName("findByAuthorAndTitleContains는 작성자와 제목 키워드가 일치하는 게시물 목록을 반환해야 한다")
    void findByAuthorAndTitleContains_정상_동작_테스트() {
        // when
        List<Post> posts = postRepository.findByAuthorAndTitleContains("codeit", "Test");

        // then
        assertThat(posts).hasSize(2);
        assertThat(posts).extracting(Post::getTitle)
                .containsExactlyInAnyOrder("JPA Test", "Spring Boot Test");
    }

    @Test
    @DisplayName("findByAuthorAndTitleContains는 조건에 맞는 게시물이 없으면 빈 목록을 반환해야 한다.")
    void findByAuthorAndTitleContains_결과_없음_테스트() {
        // when
        List<Post> posts = postRepository.findByAuthorAndTitleContains("codei", "Test");

        // then
        assertThat(posts).isEmpty();
    }

    @Test
    @DisplayName("@Query - findByTitleOrContentContaining는 제목 또는 내용에 키워드가 포함된 게시물 목록을 반환해야 한다.")
    void findByTitleOrContentContaining_쿼리_테스트(){
        // when
        List<Post> posts = postRepository.findByTitleOrContentContaining("JPA");

        // then
        assertThat(posts).hasSize(2);
        assertThat(posts).extracting(Post::getTitle)
                .containsExactlyInAnyOrder("JPA Test", "Another JPA Test");
    }

    @Test
    @DisplayName("@Query - findByTitleOrContentContaining는 제목 또는 내용에 키워드가 포함된 게시물 목록을 반환해야 한다.")
    void findByTitleOrContentContaining_쿼리_테스트_대소문자(){
        // when
        List<Post> posts = postRepository.findByTitleOrContentContaining("jpa");

        // then
        assertThat(posts).hasSize(0);
    }
}
