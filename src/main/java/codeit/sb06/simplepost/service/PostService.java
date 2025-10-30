package codeit.sb06.simplepost.service;

import codeit.sb06.simplepost.dto.request.PostCreateRequest;
import codeit.sb06.simplepost.dto.request.PostUpdateRequest;
import codeit.sb06.simplepost.dto.response.PostResponse;
import codeit.sb06.simplepost.entity.Post;
import codeit.sb06.simplepost.exception.InvalidPasswordException;
import codeit.sb06.simplepost.exception.PostNotFoundException;
import codeit.sb06.simplepost.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public PostResponse savePost(PostCreateRequest request) {
        Post post = postRepository.save(request.toEntity());
        return PostResponse.from(post);
    }

    @Transactional
    public PostResponse updatePost(Long id, PostUpdateRequest request) {
        Post post = findPostById(id);
        verifyPassword(post, request.password());
        post.update(request.title(), request.content(), request.tags());
        postRepository.save(post);
        return PostResponse.from(post);
    }

    @Transactional
    public void deletePost(Long id, String password) {
        Post post = findPostById(id);
        verifyPassword(post, password);
        postRepository.delete(post);
    }

    public PostResponse getPostById(Long id) {
        Post post = findPostById(id);
        return PostResponse.from(post);
    }

    public List<PostResponse> findAllPosts() {
        return postRepository.findAll().stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());
    }

    private Post findPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다. ID: " + id));
    }


    private void verifyPassword(Post post, String providedPassword) {
        if (!post.getPassword().equals(providedPassword)) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }
}