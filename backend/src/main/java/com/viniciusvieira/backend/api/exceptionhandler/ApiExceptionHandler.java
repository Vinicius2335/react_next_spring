package com.viniciusvieira.backend.api.exceptionhandler;

import com.viniciusvieira.backend.domain.exception.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(CategoriaNaoEncontradoException.class)
    public ResponseEntity<Object> handleCategoriaNaoEncontradoException(
            CategoriaNaoEncontradoException ex,
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

    private Problem createExceptionResponseBody(RuntimeException ex, Integer status){
        Problem problem = new Problem();
        problem.setTimestamp(OffsetDateTime.now());
        problem.setStatus(status);
        problem.setTitle(ex.getMessage());

        return problem;
    }
}
