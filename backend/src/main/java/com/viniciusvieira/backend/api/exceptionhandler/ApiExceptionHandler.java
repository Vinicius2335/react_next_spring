package com.viniciusvieira.backend.api.exceptionhandler;

import com.viniciusvieira.backend.domain.exception.CreateTemplateException;
import com.viniciusvieira.backend.domain.exception.NegocioException;
import com.viniciusvieira.backend.domain.exception.ProdutoImagemNaoEncontradoException;
import com.viniciusvieira.backend.domain.exception.usuario.CpfAlreadyExistsException;
import com.viniciusvieira.backend.domain.exception.usuario.PermissaoAlreadyExistsException;
import com.viniciusvieira.backend.domain.exception.usuario.PermissaoNaoEncontradaException;
import com.viniciusvieira.backend.domain.exception.usuario.PessoaNaoEncontradaException;
import com.viniciusvieira.backend.domain.exception.venda.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NotNull HttpHeaders headers,
            HttpStatusCode status,
            @NotNull WebRequest request
    ) {
        List<Problem.Field> fields = new ArrayList<>();

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        fieldErrors.forEach(fieldError -> fields.add(
                new Problem.Field(fieldError.getField(), fieldError.getDefaultMessage())
        ));

        Problem problem = new Problem();
        problem.setTimestamp(OffsetDateTime.now());
        problem.setStatus(status.value());
        problem.setTitle("Um ou mais campos estão inválido. Faça o preenchimento correto e tente novamente.");

        problem.setFields(fields);

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @ExceptionHandler(CarrinhoDeCompraNaoEncontradoException.class)
    public ResponseEntity<Object> handleCarrinhoDeCompraNaoEncontradoException(
            CarrinhoDeCompraNaoEncontradoException ex,
            WebRequest request
    ){
        HttpStatus status = HttpStatus.NOT_FOUND;
        Problem body = createExceptionResponseBody(ex, status.value());

        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(CategoriaNaoEncontradaException.class)
    public ResponseEntity<Object> handleCategoriaNaoEncontradoException(
            CategoriaNaoEncontradaException ex,
            WebRequest request
    ){
        HttpStatus status = HttpStatus.NOT_FOUND;
        Problem body = createExceptionResponseBody(ex, status.value());

        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(CpfAlreadyExistsException.class)
    public ResponseEntity<Object> handleCpfAlreadyExistsException(
            CpfAlreadyExistsException ex,
            WebRequest request
    ){
        HttpStatus status = HttpStatus.CONFLICT;
        Problem body = createExceptionResponseBody(ex, status.value());

        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(MarcaNaoEncontradaException.class)
    public ResponseEntity<Object> handleMarcaNaoEncontradaException(
            MarcaNaoEncontradaException ex,
            WebRequest request
    ){
        HttpStatus status = HttpStatus.NOT_FOUND;
        Problem body = createExceptionResponseBody(ex, status.value());

        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<Object> handleNegocioException(
            NegocioException ex,
            WebRequest request
    ){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Problem body = createExceptionResponseBody(ex, status.value());

        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(PermissaoNaoEncontradaException.class)
    public ResponseEntity<Object> handlePermissaoNaoEncontradaException(
            PermissaoNaoEncontradaException ex,
            WebRequest request
    ){
        HttpStatus status = HttpStatus.NOT_FOUND;
        Problem body = createExceptionResponseBody(ex, status.value());

        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(PessoaNaoEncontradaException.class)
    public ResponseEntity<Object> handlePessoaNaoEncontradaException(
            PessoaNaoEncontradaException ex,
            WebRequest request
    ){
        HttpStatus status = HttpStatus.NOT_FOUND;
        Problem body = createExceptionResponseBody(ex, status.value());

        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(ProdutoImagemNaoEncontradoException.class)
    public ResponseEntity<Object> handleProdutoImagemNaoEncontradoException(
            ProdutoImagemNaoEncontradoException ex,
            WebRequest request
    ){
        HttpStatus status = HttpStatus.NOT_FOUND;
        Problem body = createExceptionResponseBody(ex, status.value());

        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public ResponseEntity<Object> handleProdutoNaoEncontradoException(
            ProdutoNaoEncontradoException ex,
            WebRequest request
    ){
        HttpStatus status = HttpStatus.NOT_FOUND;
        Problem body = createExceptionResponseBody(ex, status.value());

        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(PermissaoAlreadyExistsException.class)
    public ResponseEntity<Object> handlePermissaoAlreadyExistsException(
            PermissaoAlreadyExistsException ex,
            WebRequest request
    ){
        HttpStatus status = HttpStatus.CONFLICT;
        Problem body = createExceptionResponseBody(ex, status.value());

        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(MarcaAlreadyExistsException.class)
    public ResponseEntity<Object> handleMarcaAlreadyExistsException(
            MarcaAlreadyExistsException ex,
            WebRequest request
    ){
        HttpStatus status = HttpStatus.CONFLICT;
        Problem body = createExceptionResponseBody(ex, status.value());

        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(CategoriaAlreadyExistsException.class)
    public ResponseEntity<Object> handleCategoriaAlreadyExistsException(
            CategoriaAlreadyExistsException ex,
            WebRequest request
    ){
        HttpStatus status = HttpStatus.CONFLICT;
        Problem body = createExceptionResponseBody(ex, status.value());

        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(CreateTemplateException.class)
    public ResponseEntity<Object> handleCreateTemplateException(
            CreateTemplateException ex,
            WebRequest request
    ){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Problem body = createExceptionResponseBody(ex, status.value());

        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    private Problem createExceptionResponseBody(RuntimeException ex, Integer status){
        Problem problem = new Problem();
        problem.setTimestamp(OffsetDateTime.now());
        problem.setStatus(status);
        problem.setTitle(ex.getMessage());

        return problem;
    }
}
