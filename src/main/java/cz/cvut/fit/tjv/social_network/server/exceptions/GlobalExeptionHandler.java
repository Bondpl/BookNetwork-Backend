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

    //BOOK
    @ExceptionHandler(Exceptions.BookAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleBookAlreadyExistsException(Exceptions.BookAlreadyExistsException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(LocalDateTime.now(), e.getMessage(), request.getDescription(false)));
    }

    @ExceptionHandler(Exceptions.BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookNotFoundException(Exceptions.BookNotFoundException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(LocalDateTime.now(), e.getMessage(), request.getDescription(false)));
    }

    @ExceptionHandler(Exceptions.BookAlreadyBorrowedException.class)
    public ResponseEntity<ErrorResponse> handleBookAlreadyBorrowedException(Exceptions.BookAlreadyBorrowedException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(LocalDateTime.now(), e.getMessage(), request.getDescription(false)));
    }

    @ExceptionHandler(Exceptions.OwnerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOwnerNotFoundException(Exceptions.OwnerNotFoundException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(LocalDateTime.now(), e.getMessage(), request.getDescription(false)));
    }

    @ExceptionHandler(Exceptions.BorrowerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBorrowerNotFoundException(Exceptions.BorrowerNotFoundException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(LocalDateTime.now(), e.getMessage(), request.getDescription(false)));
    }

    @ExceptionHandler(Exceptions.LenderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLenderNotFoundException(Exceptions.LenderNotFoundException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(LocalDateTime.now(), e.getMessage(), request.getDescription(false)));
    }

    @ExceptionHandler(Exceptions.OwnerRequiredException.class)
    public ResponseEntity<ErrorResponse> handleOwnerRequiredException(Exceptions.OwnerRequiredException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(LocalDateTime.now(), e.getMessage(), request.getDescription(false)));
    }

    @ExceptionHandler(Exceptions.CantRemoveBorrowedBookException.class)
    public ResponseEntity<ErrorResponse> handleCannotRemoveBorrowedBookException(Exceptions.CantRemoveBorrowedBookException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(LocalDateTime.now(), e.getMessage(), request.getDescription(false)));
    }

    //TRANSACTION

    @ExceptionHandler(Exceptions.TransactionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTransactionNotFoundException(Exceptions.TransactionNotFoundException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(LocalDateTime.now(), e.getMessage(), request.getDescription(false)));
    }

    //USER

    @ExceptionHandler(Exceptions.UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(Exceptions.UserNotFoundException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(LocalDateTime.now(), e.getMessage(), request.getDescription(false)));
    }


    @ExceptionHandler(Exceptions.UserEmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserEmailAlreadyExistsException(Exceptions.UserEmailAlreadyExistsException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(LocalDateTime.now(), e.getMessage(), request.getDescription(false)));
    }

    @ExceptionHandler(Exceptions.UserUsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserUsernameAlreadyExistsException(Exceptions.UserUsernameAlreadyExistsException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(LocalDateTime.now(), e.getMessage(), request.getDescription(false)));
    }

    @ExceptionHandler(Exceptions.InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestException(Exceptions.InvalidRequestException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(LocalDateTime.now(), e.getMessage(), request.getDescription(false)));
    }

    @ExceptionHandler(Exceptions.BookAlreadyReturnedException.class)
    public ResponseEntity<ErrorResponse> handleBookAlreadyReturnedException(Exceptions.BookAlreadyReturnedException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(LocalDateTime.now(), e.getMessage(), request.getDescription(false)));
    }
}
