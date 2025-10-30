package codeit.sb06.simplepost.support;

import codeit.sb06.simplepost.entity.Post;

public class PostFixture {

    public static Post createPost(String author, String title) {
        return Post.builder()
                .author(author)
                .title(title)
                .content("Content for " + title)
                .password("000000")
                .build();
    }
}
