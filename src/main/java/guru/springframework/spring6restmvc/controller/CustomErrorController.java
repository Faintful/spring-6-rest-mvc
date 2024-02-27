package guru.springframework.spring6restmvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomErrorController {
    @ExceptionHandler(MethodArgumentNotValidException.class) // The type of exception to handle
    ResponseEntity handleBindErrors(MethodArgumentNotValidException exception){ // The exception
        List errorList =
        exception.getBindingResult().getFieldErrors().stream().map((thing -> {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put(thing.getField(), thing.getDefaultMessage());
            return errorMap;
        })).collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errorList); // Creating a new HTTP response. The binding result, which is either a successful or unsuccessful indication of the binding operation, is accessed to append errors to the body of the HTTP response.
    }

}
