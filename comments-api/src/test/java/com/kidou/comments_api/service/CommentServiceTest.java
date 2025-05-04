package com.kidou.comments_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

    @Test
    void testCreateComment_UserNotFound() {
        // Arrange
        CreateCommentDTO createCommentDTO = new CreateCommentDTO();
        createCommentDTO.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessException.class, () -> commentService.createComment(createCommentDTO));
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testCreateReply_Success() {
        // Arrange
        CreateReplyDTO createReplyDTO = new CreateReplyDTO();
        createReplyDTO.setParentId(1L);
        createReplyDTO.setUserId(2L);
        createReplyDTO.setContent("Test reply");

        Comment parentComment = new Comment();
        parentComment.setId(1L);

        User user = new User();
        user.setId(2L);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(parentComment));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        // Act
        String result = commentService.createReply(createReplyDTO);

        // Assert
        assertEquals("Resposta criada com sucesso!", result);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testCreateReply_ParentCommentNotFound() {
        // Arrange
        CreateReplyDTO createReplyDTO = new CreateReplyDTO();
        createReplyDTO.setParentId(1L);

        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessException.class, () -> commentService.createReply(createReplyDTO));
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testGetCommentById_Success() {
        // Arrange
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test comment");

        GetCommentsDTO getCommentsDTO = new GetCommentsDTO();
        getCommentsDTO.setId(1L);
        getCommentsDTO.setContent("Test comment");

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(modelMapper.map(comment, GetCommentsDTO.class)).thenReturn(getCommentsDTO);

        // Act
        GetCommentsDTO result = commentService.getCommentById(1L);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals("Test comment", result.getContent());
    }

    @Test
    void testGetCommentById_NotFound() {
        // Arrange
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessException.class, () -> commentService.getCommentById(1L));
    }

    @Test
    void testGetTopLevelComments() {
        // Arrange
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Top-level comment");

        Page<Comment> page = new PageImpl<>(List.of(comment));
        PageRequest pageable = PageRequest.of(0, 5);

        GetCommentsDTO getCommentsDTO = new GetCommentsDTO();
        getCommentsDTO.setId(1L);
        getCommentsDTO.setContent("Top-level comment");

        when(commentRepository.findByParentCommentIsNull(pageable)).thenReturn(page);
        when(modelMapper.map(comment, GetCommentsDTO.class)).thenReturn(getCommentsDTO);

        // Act
        Page<GetCommentsDTO> result = commentService.getTopLevelComments(pageable);

        // Assert
        assertEquals(1, result.getContent().size());
        assertEquals("Top-level comment", result.getContent().get(0).getContent());
    }
}