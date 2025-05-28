package project.spring_basic.controller.BoardControllerTest.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.FlashMap;

import project.spring_basic.controller.BoardControllerTest.BoardControllerUnitTestSupport;
import project.spring_basic.data.dto.Response.ModelAttribute.PostUpdateDTO;
import project.spring_basic.exception.MemberNotFoundException;

@Tag("unit")
@Tag("controller")
@Tag("controller-unit")
public class EditTest extends BoardControllerUnitTestSupport {
  
    @Test
    @DisplayName("게시글 정보와 내용을 담아 update_post 템플릿과 함께 응답한다.")
    public void edit() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        PostUpdateDTO postUpdateDTO = new PostUpdateDTO(
                "1", "1", "tttttttt",
                "테스트용 임시 계정", "2025-01-01 00:00:00"
            );

        when(boardService.getUpdatePost(1L)).thenReturn(postUpdateDTO);
        when(sessionService.getTemplateOrDefault(session, "update_post")).thenReturn("update_post");


        // when & then
        mockMvc.perform(get("/board/edit/1").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("update_post"))
                .andExpect(model().attribute("post", postUpdateDTO));
    }


    @Test
    @DisplayName("처리 중 예외가 발생한 경우 에러 원인을 담아 error 페이지로 리다이렉트 시킨다.")
    public void editWhenExceptionOccurs() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        when(boardService.getUpdatePost(1L))
                .thenThrow(new MemberNotFoundException("해당 회원은 존재하지 않습니다."));

        // when & then
        MvcResult result = mockMvc.perform(get("/board/edit/1").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"))
                .andReturn();

        FlashMap flashMap = result.getFlashMap();
        assertThat(flashMap.get("error")).isEqualTo("해당 회원은 존재하지 않습니다.");
    }

}
