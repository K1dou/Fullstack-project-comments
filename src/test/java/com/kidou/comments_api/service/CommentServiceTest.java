package com.kidou.comments_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

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
import com.kidou.comments_api.model.dto.UpdateCommentDTO;
import com.kidou.comments_api.repository.CommentRepository;
import com.kidou.comments_api.repository.UserRepository;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private RedisLikeService redisLikeService;

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

        GetCommentsDTO replyDTO = new GetCommentsDTO();
        replyDTO.setId(2L);
        replyDTO.setContent("Test reply");

        when(commentRepository.findById(1L)).thenReturn(Optional.of(parentComment));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> {
            Comment saved = invocation.getArgument(0);
            saved.setId(2L); // simula ID gerado
            return saved;
        });
        when(modelMapper.map(any(Comment.class), eq(GetCommentsDTO.class))).thenReturn(replyDTO);

        // Act
        GetCommentsDTO result = commentService.createReply(createReplyDTO);

        // Assert
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("Test reply", result.getContent());
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

    @Test
    void testDeleteComment_Success() {
        // Arrange
        Comment comment = new Comment();
        comment.setId(1L);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        // Act
        String result = commentService.deleteComment(1L);

        // Assert
        assertEquals("Comentário excluído com sucesso!", result);
        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    void testDeleteComment_NotFound() {
        // Arrange
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessException.class, () -> commentService.deleteComment(1L));
        verify(commentRepository, never()).delete(any(Comment.class));
    }

    @Test
    void testUpdateComment_Success() {
        // Arrange
        UpdateCommentDTO updateCommentDTO = new UpdateCommentDTO();
        updateCommentDTO.setContent("Updated comment");

        Comment comment = new Comment();
        comment.setId(1L);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        // Act
        String result = commentService.updateComment(1L, updateCommentDTO);

        // Assert
        assertEquals("Comentário atualizado com sucesso!", result);
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void testUpdateComment_NotFound() {
        // Arrange
        UpdateCommentDTO updateCommentDTO = new UpdateCommentDTO();
        updateCommentDTO.setContent("Updated comment");

        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessException.class, () -> commentService.updateComment(1L, updateCommentDTO));
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testGetAllComments() {
        // Arrange
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setContent("Comment 1");

        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setContent("Comment 2");

        when(commentRepository.findAll()).thenReturn(List.of(comment1, comment2));
        when(redisLikeService.getLikeCount(1L)).thenReturn(0);
        when(redisLikeService.getLikeCount(2L)).thenReturn(0);
        when(modelMapper.map(any(Comment.class), eq(GetCommentsDTO.class)))
                .thenAnswer(invocation -> {
                    Comment c = invocation.getArgument(0);
                    GetCommentsDTO dto = new GetCommentsDTO();
                    dto.setId(c.getId());
                    dto.setContent(c.getContent());
                    return dto;
                });

        // Act
        List<GetCommentsDTO> result = commentService.getAllComments();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Comment 1", result.get(0).getContent());
        assertEquals("Comment 2", result.get(1).getContent());
        verify(commentRepository, times(1)).findAll();
        verify(redisLikeService, times(1)).getLikeCount(1L);
        verify(redisLikeService, times(1)).getLikeCount(2L);
    }
}