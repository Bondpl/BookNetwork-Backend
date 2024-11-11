package cz.cvut.fit.tjv.social_network.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExeptionHandler {

    //RATING
    @ExceptionHandler(Exceptions.RatingAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleRatingAlreadyExistsException(Exceptions.RatingAlreadyExistsException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(LocalDateTime.now(), e.getMessage(), request.getDescription(false)));
    }

    @ExceptionHandler(Exceptions.RatingNotInScopeException.class)
    public ResponseEntity<ErrorResponse> handleRatingNotInScopeException(Exceptions.RatingNotInScopeException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(LocalDateTime.now(), e.getMessage(), request.getDescription(false)));
    }

    @ExceptionHandler(Exceptions.RatingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRatingNotFoundException(Exceptions.RatingNotFoundException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(LocalDateTime.now(), e.getMessage(), request.getDescription(false)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(LocalDateTime.now(), e.getMessage(), request.getDescription(false)));
    }
}
