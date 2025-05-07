package com.kidou.comments_api.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kidou.comments_api.exceptions.BusinessException;
import com.kidou.comments_api.model.Comment;
import com.kidou.comments_api.model.User;
import com.kidou.comments_api.model.dto.AuthorDTO;
import com.kidou.comments_api.model.dto.CreateCommentDTO;
import com.kidou.comments_api.model.dto.CreateReplyDTO;
import com.kidou.comments_api.model.dto.GetCommentsDTO;
import com.kidou.comments_api.model.dto.UpdateCommentDTO;
import com.kidou.comments_api.repository.CommentRepository;
import com.kidou.comments_api.repository.UserRepository;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private ModelMapper modelMapper;
    private final RedisLikeService redisLikeService;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, ModelMapper modelMapper,
            RedisLikeService redisLikeService) {
        this.redisLikeService = redisLikeService;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public String createComment(CreateCommentDTO createCommentDTO) {

        User user = userRepository.findById(createCommentDTO.getUserId())
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        Comment comment = new Comment();
        comment.setContent(createCommentDTO.getContent());
        comment.setAuthor(user);

        commentRepository.save(comment);

        return "Comentário criado com sucesso!";
    }

    public String createReply(CreateReplyDTO createReplyDTO) {
        Comment parentComment = commentRepository.findById(createReplyDTO.getParentId())
                .orElseThrow(() -> new BusinessException("Comentário pai não encontrado"));
        User user = userRepository.findById(createReplyDTO.getUserId())
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        Comment reply = new Comment();
        reply.setContent(createReplyDTO.getContent());
        reply.setAuthor(user);
        reply.setParentComment(parentComment);

        commentRepository.save(reply);

        return "Resposta criada com sucesso!";
    }

    public GetCommentsDTO getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Comentário não encontrado"));

        GetCommentsDTO getCommentsDTO = new GetCommentsDTO();
        getCommentsDTO.setId(comment.getId());
        getCommentsDTO.setContent(comment.getContent());
        getCommentsDTO.setCreatedAt(comment.getCreatedAt());
        getCommentsDTO.setAuthor(modelMapper.map(comment.getAuthor(), AuthorDTO.class));
        getCommentsDTO.setReplies(comment.getReplies().stream()
                .map(reply -> modelMapper.map(reply, GetCommentsDTO.class))
                .toList());

        return getCommentsDTO;
    }

    public Page<GetCommentsDTO> getTopLevelComments(Pageable pageable) {
        Page<Comment> page = commentRepository.findByParentCommentIsNull(pageable);
        return page.map(this::convertCommentWithLikes);
    }

    private GetCommentsDTO convertCommentWithLikes(Comment comment) {
        GetCommentsDTO dto = modelMapper.map(comment, GetCommentsDTO.class);
        dto.setLikeCount(redisLikeService.getLikeCount(comment.getId()));

        // Processa os replies recursivamente
        if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
            dto.setReplies(
                    comment.getReplies().stream()
                            .map(this::convertCommentWithLikes)
                            .toList());
        }

        return dto;
    }

    public List<GetCommentsDTO> getAllComments() {
        List<Comment> comments = commentRepository.findAll();

        return comments.stream()
                .map(comment -> {
                    GetCommentsDTO dto = modelMapper.map(comment, GetCommentsDTO.class);
                    dto.setLikeCount(redisLikeService.getLikeCount(comment.getId()));
                    return dto;
                })
                .toList();
    }

    public String deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Comentário não encontrado"));
        commentRepository.delete(comment);
        return "Comentário excluído com sucesso!";
    }

    public String updateComment(Long id, UpdateCommentDTO updateCommentDTO) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Comentário não encontrado"));
        comment.setContent(updateCommentDTO.getContent());
        commentRepository.save(comment);
        return "Comentário atualizado com sucesso!";
    }

}
