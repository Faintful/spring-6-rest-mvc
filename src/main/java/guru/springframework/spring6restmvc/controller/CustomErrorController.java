package guru.springframework.spring6restmvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(MethodArgumentNotValidException.class) // The type of exception to handle
    ResponseEntity handleBindErrors(MethodArgumentNotValidException exception){ // The exception
        return ResponseEntity.badRequest().body(exception.getBindingResult().getFieldErrors()); // Creating a new HTTP response. The binding result, which is either a successful or unsuccessful indication of the binding operation, is accessed to append errors to the body of the HTTP response.
    }

}
