package cz.cvut.fit.tjv.social_network.server.exceptions;

public class Exceptions {

    //RATING
    public static class RatingAlreadyExistsException extends RuntimeException {
        public RatingAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class RatingNotInScopeException extends RuntimeException {
        public RatingNotInScopeException(String message) {
            super(message);
        }
    }

    public static class RatingNotFoundException extends RuntimeException {
        public RatingNotFoundException(String message) {
            super(message);
        }
    }

    //BOOK
    public static class BookAlreadyExistsException extends RuntimeException {
        public BookAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class BookNotFoundException extends RuntimeException {
        public BookNotFoundException(String message) {
            super(message);
        }
    }

    public static class BookAlreadyBorrowedException extends RuntimeException {
        public BookAlreadyBorrowedException(String message) {
            super(message);
        }
    }

    public static class OwnerNotFoundException extends RuntimeException {
        public OwnerNotFoundException(String message) {
            super(message);
        }
    }

    public static class BorrowerNotFoundException extends RuntimeException {
        public BorrowerNotFoundException(String message) {
            super(message);
        }
    }

    public static class LenderNotFoundException extends RuntimeException {
        public LenderNotFoundException(String message) {
            super(message);
        }
    }

    public static class OwnerRequiredException extends RuntimeException {
        public OwnerRequiredException(String message) {
            super(message);
        }
    }

    public static class CantRemoveBorrowedBookException extends RuntimeException {
        public CantRemoveBorrowedBookException(String message) {
            super(message);
        }
    }

    //TRANSACTION
    public static class TransactionNotFoundException extends RuntimeException {
        public TransactionNotFoundException(String message) {
            super(message);
        }
    }

    //USER

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public static class UserEmailAlreadyExistsException extends RuntimeException {
        public UserEmailAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class UserUsernameAlreadyExistsException extends RuntimeException {
        public UserUsernameAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class InvalidRequestException extends RuntimeException {
        public InvalidRequestException(String message) {
            super(message);
        }
    }
}
