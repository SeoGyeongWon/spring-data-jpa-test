package codeit.sb06.simplepost.service;

import codeit.sb06.simplepost.dto.request.PostCreateRequest;
import codeit.sb06.simplepost.dto.request.PostUpdateRequest;
import codeit.sb06.simplepost.dto.response.PostResponse;
import codeit.sb06.simplepost.entity.Post;
import codeit.sb06.simplepost.exception.InvalidPasswordException;
import codeit.sb06.simplepost.repository.PostRepository;
import codeit.sb06.simplepost.support.PostFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("성공 검증 - savePost는 Post를 저장하고 PostResponse를 반환한다.")
    void savePost_success() {
        // given (준비) - 테스트하고자 하는 savePost 메서드에 전달할 입력 값 준비
        PostCreateRequest request = PostCreateRequest.builder()
                .author("codeit")
                .title("New Post")
                .build();

        // given - 테스트하고자 하는 savePost가 의존하는 postRepository(mock).save 메소드 동작을 정의
        Post savedPost = PostFixture.createPost(1L, "codeit", "New Post");
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        // when
        PostResponse response = postService.savePost(request);

        // then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.author()).isEqualTo("codeit");

        verify(postRepository).save(any(Post.class));
    }

    @Test
    @DisplayName("예외 검증 - updatePost는 비밀번호가 틀리면 InvalidPasswordException을 발생시킨다.")
    void updatePost_wrongPassword_throwsException() {
        // given
        long postId = 1L;
        String originalPassword = "000000";
        Post post = PostFixture.createPostWithPassword(postId, "codeit", "test", originalPassword);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        String wrongPassword = "111111";
        PostUpdateRequest request = PostUpdateRequest.builder()
                .password(wrongPassword)
                .build();

        // when, then
        InvalidPasswordException ex = assertThrows(InvalidPasswordException.class, () -> {
            postService.updatePost(postId, request);
        });
        assertEquals("비밀번호가 일치하지 않습니다.", ex.getMessage());

        verify(postRepository).findById(postId);
        verify(postRepository, never()).save(any(Post.class));
    }
}
