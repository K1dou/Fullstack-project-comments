package com.kidou.comments_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.kidou.comments_api.exceptions.BusinessException;
import com.kidou.comments_api.model.Comment;
import com.kidou.comments_api.model.User;
import com.kidou.comments_api.model.dto.CreateCommentDTO;
import com.kidou.comments_api.model.dto.CreateReplyDTO;
import com.kidou.comments_api.model.dto.GetCommentsDTO;
import com.kidou.comments_api.repository.CommentRepository;
import com.kidou.comments_api.repository.UserRepository;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateComment_Success() {
        // Arrange
        CreateCommentDTO createCommentDTO = new CreateCommentDTO();
        createCommentDTO.setUserId(1L);
        createCommentDTO.setContent("Test comment");

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        String result = commentService.createComment(createCommentDTO);

        // Assert
        assertEquals("Comentário criado com sucesso!", result);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

}