package codeit.sb06.simplepost.support;

import codeit.sb06.simplepost.entity.Post;

import java.lang.reflect.Field;

public class PostFixture {

    public static Post createPost(String author, String title) {
        return Post.builder()
                .author(author)
                .title(title)
                .content("Content for " + title)
                .password("000000")
                .build();
    }

    public static Post createPost(long id, String author, String title) {
        Post post = createPost(author, title);
        setPostId(post, id);
        return post;
    }

    public static Post createPostWithPassword(String author, String title, String password) {
        return Post.builder()
                .author(author)
                .title(title)
                .password(password)
                .content("Content for " + title)
                .build();
    }

    public static Post createPostWithPassword(long id, String author, String title, String password) {
        Post post = createPostWithPassword(author, title, password);
        setPostId(post, id);
        return post;
    }

    private static void setPostId(Post post, long id) {
        try {
            Class<?> postClass = post.getClass();
            Field idField = postClass.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(post, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
