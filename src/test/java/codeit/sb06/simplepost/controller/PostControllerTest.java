package codeit.sb06.simplepost.controller;

import codeit.sb06.simplepost.dto.request.PostCreateRequest;
import codeit.sb06.simplepost.dto.response.PostResponse;
import codeit.sb06.simplepost.exception.ErrorCode;
import codeit.sb06.simplepost.exception.PostNotFoundException;
import codeit.sb06.simplepost.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    public static final String TEST_AUTHOR = "codeit";
    public static final String TEST_PASSWORD = "123456";
    public static final String TEST_TITLE = "New Title";
    public static final String TEST_CONTENT = "New Content";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PostService postService;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMappingContext;

    @Test
    @DisplayName("createPost는 게시물 생성을 성공하고 201 Created를 반환한다.")
    void createPost_success() throws Exception {
        PostCreateRequest request = PostCreateRequest.builder()
                .author(TEST_AUTHOR)
                .password(TEST_PASSWORD)
                .title(TEST_TITLE)
                .content(TEST_CONTENT)
                .build();

        PostResponse response = PostResponse.builder()
                .id(1L)
                .author(TEST_AUTHOR)
                .title(TEST_TITLE)
                .content(TEST_CONTENT)
                .createdAt(LocalDateTime.now())
                .build();

        /*
        {
          "id": 1,
          "author": "codeit",
          "title": "New Title",
          "content": "New Content",
          "createdAt" "2025-..."
        }
         */

        when(postService.savePost(request)).thenReturn(response);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.author").value(TEST_AUTHOR))
                .andExpect(jsonPath("$.title").value(TEST_TITLE))
                .andExpect(jsonPath("$.content").value(TEST_CONTENT));
    }

    @Test
    @DisplayName("createPost는 작성자 이름이 비어있으면 400 Bad Request를 반환한다")
    void createPost_invalidInput_returnsBadRequest() throws Exception {
        // given
        // author 필드가 비어있는 잘못된 요청
        PostCreateRequest request = PostCreateRequest.builder()
                .author("").password("123456").title("New Title").content("New Content").build();

        // when & then
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("getPost는 존재하지 않는 ID로 조회 시 404 Not Found를 반환한다")
    void getPost_notFound() throws Exception {
        // given
        Long notExistId = 999L;

        // BDDMockito.given()을 사용한 행동 정의 (when과 동일)
        // postService.getPostById(999L)가 호출되면, PostNotFoundException 예외를 던지도록 설정
        given(postService.getPostById(notExistId))
                .willThrow(new PostNotFoundException("게시글을 찾을 수 없습니다. ID: " + notExistId));

        // when & then
        mockMvc.perform(get("/api/posts/{id}", notExistId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.POST_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.POST_NOT_FOUND.getMessage()));
    }
}
