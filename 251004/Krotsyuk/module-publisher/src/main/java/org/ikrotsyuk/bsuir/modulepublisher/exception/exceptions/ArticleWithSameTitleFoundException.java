package org.ikrotsyuk.bsuir.modulepublisher.exception.exceptions;

public class ArticleWithSameTitleFoundException extends RuntimeException {
    public ArticleWithSameTitleFoundException(String message) {
        super(message);
    }
}
