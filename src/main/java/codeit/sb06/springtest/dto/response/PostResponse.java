package codeit.sb06.springtest.dto.response;

import codeit.sb06.springtest.entity.Post;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PostResponse(
        Long id,
        String author,
        String title,
        String content,
        List<String> tags,
        LocalDateTime createdAt
) {
    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .author(post.getAuthor())
                .title(post.getTitle())
                .content(post.getContent())
                .tags(post.getTags())
                .createdAt(post.getCreatedAt())
                .build();
    }
}