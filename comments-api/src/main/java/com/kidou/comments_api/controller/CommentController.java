package com.kidou.comments_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kidou.comments_api.model.dto.CreateCommentDTO;
import com.kidou.comments_api.model.dto.CreateReplyDTO;
import com.kidou.comments_api.model.dto.GetCommentsDTO;
import com.kidou.comments_api.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Cria um novo comentário", responses = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @PostMapping("/create")
    public ResponseEntity<String> createComment(@RequestBody @Valid CreateCommentDTO createCommentDTO) {
        return ResponseEntity.ok(commentService.createComment(createCommentDTO));
    }

    @PostMapping("/reply")
    public ResponseEntity<String> createReply(@RequestBody @Valid CreateReplyDTO createReplyDTO) {
        return ResponseEntity.ok(commentService.createReply(createReplyDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetCommentsDTO> getCommentById(@PathVariable Long id) {

        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @GetMapping()
    public ResponseEntity<List<GetCommentsDTO>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }
}
