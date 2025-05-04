package com.kidou.comments_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

        when(commentService.createReply(any(CreateReplyDTO.class))).thenReturn("Resposta criada com sucesso!");

        // Act & Assert
        mockMvc.perform(post("/api/v1/comments/reply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReplyDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Resposta criada com sucesso!"));

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

}