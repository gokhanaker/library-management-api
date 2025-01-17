package com.applab.library_management.exception;

public class BookExceptions {

    public static class ForbiddenBookException extends RuntimeException {
        public ForbiddenBookException(String message) { super (message);}
    }

    public static class BookNotFoundException extends RuntimeException {
        public BookNotFoundException(String message) { super (message);}
    }

    public static class BookAlreadyBorrowedByUserException extends RuntimeException {
        public BookAlreadyBorrowedByUserException(String message) {
            super(message);
        }
    }

    public static class BookNotAvailableToBorrowException extends RuntimeException {
        public BookNotAvailableToBorrowException(String message) {
            super(message);
        }
    }
}
