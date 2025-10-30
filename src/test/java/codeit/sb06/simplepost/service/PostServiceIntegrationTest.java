package codeit.sb06.simplepost.service;

import codeit.sb06.simplepost.dto.request.PostUpdateRequest;
import codeit.sb06.simplepost.entity.Post;
import codeit.sb06.simplepost.repository.PostRepository;
import codeit.sb06.simplepost.support.PostFixture;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostServiceIntegrationTest {

    public static final String ORIGINAL_TITLE = "Original Title";
    public static final String UPDATED_TITLE = "Updated Title";
    public static final String PASSWORD = "000000";

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private EntityManager em;

    private static Long postId = 0L;

    @Test
    @DisplayName("트랜잭션 동작 검증 - updatePost는 DB의 데이터를 실제로 변경한다.")
    @Order(1)
    void updatePost_integration_success() {
        postRepository.deleteAll();
        Post savedPost = postRepository.save(
                PostFixture.createPostWithPassword(
                        "codeit",
                        ORIGINAL_TITLE,
                        PASSWORD
                )
        );

        postId = savedPost.getId();

        // given
        PostUpdateRequest request = PostUpdateRequest.builder()
                .password(PASSWORD)
                .title(UPDATED_TITLE)
                .content(savedPost.getContent())
                .build();

        // when
        postService.updatePost(savedPost.getId(), request);

        em.flush();
        em.clear();

        // then
        Post updatedPost = postRepository.findById(savedPost.getId()).get();
        assertNotNull(updatedPost);
        assertThat(updatedPost.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @DisplayName("롤백 검증 - 이전 테스트의 변경사항이 적용되지 않았는지 확인.")
    @Order(2)
    void verify_rollback() {
        Post post = postRepository.findById(postId).orElse(null);
        assertNull(post);
    }
}
