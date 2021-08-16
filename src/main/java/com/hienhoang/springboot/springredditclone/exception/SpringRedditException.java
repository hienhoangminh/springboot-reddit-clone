package com.hienhoang.springboot.springredditclone.exception;

// We don't want to express the technical exception to the user
// Instead we should represent it under understanding format
public class SpringRedditException extends RuntimeException{
    public SpringRedditException(String exception){
        super(exception);
    }
}
