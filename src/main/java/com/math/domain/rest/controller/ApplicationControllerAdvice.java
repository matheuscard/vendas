package com.math.domain.rest.controller;

import com.math.domain.rest.ApiErros;
import com.math.exception.PedidoNaoEncontradoException;
import com.math.exception.RegraNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ApplicationControllerAdvice {
    @ExceptionHandler(RegraNegocioException.class)
//    @ResponseBody - Comentado por não necessitar uma vez que o RestControllerAdvice
    //Já possui a annotatio ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErros handleRegraNegocioException(RegraNegocioException ex){
        String mensagemErro = ex.getMessage();
        return new ApiErros(mensagemErro);
     }
     @ExceptionHandler(PedidoNaoEncontradoException.class)
     @ResponseStatus(HttpStatus.NOT_FOUND)
     public ApiErros handlePedidoNotFoundException(PedidoNaoEncontradoException ex){
        return new ApiErros(ex.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErros handleMethodNotValidException(MethodArgumentNotValidException ex){
        return new ApiErros(ex.getBindingResult().getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList()));
    }
}
