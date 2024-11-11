package cz.cvut.fit.tjv.social_network.server.exceptions;

public class Exceptions {

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

}
