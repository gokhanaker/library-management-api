package com.applab.library_management.exception;

public class BookExceptions {

    public static class ForbiddenToAddBookException extends RuntimeException {
        public ForbiddenToAddBookException(String message) { super (message);}
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
