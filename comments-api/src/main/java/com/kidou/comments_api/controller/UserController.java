package com.kidou.comments_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kidou.comments_api.model.User;
import com.kidou.comments_api.model.dto.LoginUserDTO;
import com.kidou.comments_api.model.dto.UserCreateDTO;
import com.kidou.comments_api.service.AuthService;
import com.kidou.comments_api.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.authService = authService;
        this.userService = userService;
    }

    @Operation(summary = "Cria um novo usuário", description = "Este endpoint permite criar um novo usuário fornecendo os dados necessários no corpo da requisição.", responses = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(
            @RequestPart("usuario") @Valid UserCreateDTO userCreateDTO,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar) {

        User novoUsuario = userService.createUser(userCreateDTO, avatar);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @Operation(summary = "Autentica um usuário", description = "Este endpoint permite que um usuário faça login fornecendo suas credenciais (email e senha). Retorna um token JWT em caso de sucesso.", responses = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso. Retorna o token JWT.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas ou não autorizadas.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Requisição inválida.", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginUserDTO loginUserDto) {
        String token = authService.login(loginUserDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
