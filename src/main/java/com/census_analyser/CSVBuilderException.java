package com.census_analyser;

public class CSVBuilderException extends Exception {
    public enum ExceptionType {
        NO_SUCH_FILE,FILE_NOT_FOUND,WRONG_DELIMITER,UNABLE_TO_PARSE
    }

    ExceptionType exceptionType;

    public CSVBuilderException(ExceptionType exceptionType, String message){
        super(message);
        this.exceptionType = exceptionType;
    }
}
