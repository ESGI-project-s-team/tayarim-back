package fr.esgi.al5_2.Tayarim.exceptions;



import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler{

    //...

    // @Validate For Validating Path Variables and Request Parameters
    @ExceptionHandler(ProprietaireNullException.class)
    public void constraintViolationException(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    // // error handle for @Valid
    // protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
    //                              HttpHeaders headers,
    //                              HttpStatus status, WebRequest request) {

    //     Map<String, Object> body = new LinkedHashMap<>();
    //     body.put("timestamp", new Date());
    //     body.put("status", status.value());

    //     //Get all fields errors
    //     List<String> errors = ex.getBindingResult()
    //             .getFieldErrors()
    //             .stream()
    //             .map(x -> x.getDefaultMessage())
    //             .collect(Collectors.toList());

    //     body.put("errors", errors);

    //     return new ResponseEntity<>(body, headers, status);

    // }

}
