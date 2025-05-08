package com.kidou.comments_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kidou.comments_api.model.dto.CreateCommentDTO;
import com.kidou.comments_api.model.dto.CreateReplyDTO;
import com.kidou.comments_api.model.dto.GetCommentsDTO;
import com.kidou.comments_api.model.dto.UpdateCommentDTO;
import com.kidou.comments_api.service.CommentService;

class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateComment() throws Exception {
        // Arrange
        CreateCommentDTO createCommentDTO = new CreateCommentDTO();
        createCommentDTO.setUserId(1L);
        createCommentDTO.setContent("Test comment");

        when(commentService.createComment(any(CreateCommentDTO.class))).thenReturn("Comentário criado com sucesso!");

        // Act & Assert
        mockMvc.perform(post("/api/v1/comments/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCommentDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Comentário criado com sucesso!"));

        verify(commentService, times(1)).createComment(any(CreateCommentDTO.class));
    }

    @Test
    void testCreateReply() throws Exception {
        // Arrange
        CreateReplyDTO createReplyDTO = new CreateReplyDTO();
        createReplyDTO.setParentId(1L);
        createReplyDTO.setUserId(2L);
        createReplyDTO.setContent("Test reply");

        GetCommentsDTO replyResponse = new GetCommentsDTO();
        replyResponse.setId(2L);
        replyResponse.setContent("Test reply");

        when(commentService.createReply(any(CreateReplyDTO.class))).thenReturn(replyResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/comments/reply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReplyDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.content").value("Test reply"));

        verify(commentService, times(1)).createReply(any(CreateReplyDTO.class));
    }

    @Test
    void testGetCommentById() throws Exception {
        // Arrange
        GetCommentsDTO getCommentsDTO = new GetCommentsDTO();
        getCommentsDTO.setId(1L);
        getCommentsDTO.setContent("Test comment");

        when(commentService.getCommentById(1L)).thenReturn(getCommentsDTO);

        // Act & Assert
        mockMvc.perform(get("/api/v1/comments/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Test comment"));

        verify(commentService, times(1)).getCommentById(1L);
    }

    @Test
    void testDeleteComment() throws Exception {
        // Arrange
        when(commentService.deleteComment(1L)).thenReturn("Comentário excluído com sucesso!");

        // Act & Assert
        mockMvc.perform(delete("/api/v1/comments/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Comentário excluído com sucesso!"));

        verify(commentService, times(1)).deleteComment(1L);
    }

    @Test
    void testUpdateComment() throws Exception {
        // Arrange
        UpdateCommentDTO updateCommentDTO = new UpdateCommentDTO();
        updateCommentDTO.setContent("Updated comment");

        when(commentService.updateComment(eq(1L), any(UpdateCommentDTO.class)))
                .thenReturn("Comentário atualizado com sucesso!");

        // Act & Assert
        mockMvc.perform(put("/api/v1/comments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCommentDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Comentário atualizado com sucesso!"));

        verify(commentService, times(1)).updateComment(eq(1L), any(UpdateCommentDTO.class));
    }

    @Test
    void testGetAllComments() throws Exception {
        // Arrange
        GetCommentsDTO comment1 = new GetCommentsDTO();
        comment1.setId(1L);
        comment1.setContent("Comment 1");

        GetCommentsDTO comment2 = new GetCommentsDTO();
        comment2.setId(2L);
        comment2.setContent("Comment 2");

        when(commentService.getAllComments()).thenReturn(List.of(comment1, comment2));

        // Act & Assert
        mockMvc.perform(get("/api/v1/comments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].content").value("Comment 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].content").value("Comment 2"));

        verify(commentService, times(1)).getAllComments();
    }
}