package com.kidou.comments_api.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kidou.comments_api.model.dto.CreateCommentDTO;
import com.kidou.comments_api.model.dto.CreateReplyDTO;
import com.kidou.comments_api.model.dto.GetCommentsDTO;
import com.kidou.comments_api.model.dto.UpdateCommentDTO;
import com.kidou.comments_api.service.CommentService;
import com.kidou.comments_api.service.RedisLikeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;
    private final RedisLikeService redisLikeService;

    public CommentController(CommentService commentService, RedisLikeService redisLikeService) {
        this.redisLikeService = redisLikeService;
        this.commentService = commentService;
    }

    @GetMapping("/hello")
    @Operation(summary = "Endpoint de teste", description = "Este endpoint é usado para verificar se a API está funcionando corretamente.")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello, World!");
    }

    @Operation(summary = "Adiciona um like ao comentário", description = "Este endpoint incrementa o contador de likes de um comentário específico.", responses = {
            @ApiResponse(responseCode = "200", description = "Like adicionado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Comentário não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @PostMapping("/{idComment}/{idUser}/like")
    public ResponseEntity<Void> like(@PathVariable Long idComment, @PathVariable Long idUser) {
        redisLikeService.likePost(idComment, idUser);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove um like do comentário", description = "Este endpoint decrementa o contador de likes de um comentário específico.", responses = {
            @ApiResponse(responseCode = "200", description = "Like removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Comentário não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @PostMapping("/{idComment}/{idUser}/unlike")
    public ResponseEntity<Void> unlike(@PathVariable Long idComment, @PathVariable Long idUser) {
        redisLikeService.unlikePost(idComment, idUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reset")
    @Operation(summary = "Reseta o contador de likes de um comentário", description = "Este endpoint reseta o contador de likes de um comentário específico.", responses = {
            @ApiResponse(responseCode = "200", description = "Contador de likes resetado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Comentário não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Void> resetLikeCount(@PathVariable Long id) {
        redisLikeService.resetLikeCount(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Consulta o número de likes de um comentário", description = "Retorna a contagem atual de likes de um comentário específico.", responses = {
            @ApiResponse(responseCode = "200", description = "Contagem de likes retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "404", description = "Comentário não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @GetMapping("/{id}/likes")
    public ResponseEntity<Integer> getLikes(@PathVariable Long id) {
        return ResponseEntity.ok(redisLikeService.getLikeCount(id));
    }

    @Operation(summary = "Cria um novo comentário", description = "Este endpoint permite criar um novo comentário fornecendo os dados necessários no corpo da requisição.", responses = {
            @ApiResponse(responseCode = "200", description = "Comentário criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @PostMapping("/create")
    public ResponseEntity<String> createComment(@RequestBody @Valid CreateCommentDTO createCommentDTO) {
        return ResponseEntity.ok(commentService.createComment(createCommentDTO));
    }

    @Operation(summary = "Cria uma resposta para um comentário", description = "Este endpoint permite criar uma resposta para um comentário existente fornecendo os dados necessários no corpo da requisição.", responses = {
            @ApiResponse(responseCode = "200", description = "Resposta criada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Comentário não encontrado")
    })
    @PostMapping("/reply")
    public ResponseEntity<GetCommentsDTO> createReply(@RequestBody @Valid CreateReplyDTO createReplyDTO) {
        return ResponseEntity.ok(commentService.createReply(createReplyDTO));
    }

    @Operation(summary = "Obtém um comentário pelo ID", description = "Este endpoint retorna os detalhes de um comentário específico com base no ID fornecido.", responses = {
            @ApiResponse(responseCode = "200", description = "Comentário encontrado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetCommentsDTO.class))),
            @ApiResponse(responseCode = "404", description = "Comentário não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<GetCommentsDTO> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @Operation(summary = "Obtém comentários de nível superior (sem pai)", description = "Retorna uma página de comentários principais (aqueles que não são respostas).", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de comentários principais retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetCommentsDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @GetMapping("/comments")
    public ResponseEntity<Page<GetCommentsDTO>> getTopLevelComments(Pageable pageable) {

        Page<GetCommentsDTO> comments = commentService.getTopLevelComments(pageable);

        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Obtém todos os comentários", description = "Este endpoint retorna uma lista de todos os comentários disponíveis.", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de comentários retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetCommentsDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @GetMapping()
    public ResponseEntity<List<GetCommentsDTO>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um comentário", description = "Este endpoint permite atualizar um comentário existente com base no ID fornecido.", responses = {
            @ApiResponse(responseCode = "200", description = "Comentário atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Comentário não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<String> updateComment(@PathVariable Long id,
            @RequestBody @Valid UpdateCommentDTO updateCommentDTO) {
        return ResponseEntity.ok(commentService.updateComment(id, updateCommentDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um comentário", description = "Este endpoint permite excluir um comentário existente com base no ID fornecido.", responses = {
            @ApiResponse(responseCode = "200", description = "Comentário excluído com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Comentário não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.deleteComment(id));
    }
}