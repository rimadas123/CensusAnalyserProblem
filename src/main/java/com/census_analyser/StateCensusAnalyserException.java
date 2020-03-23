package com.census_analyser;

public class StateCensusAnalyserException extends Throwable {
    public enum ExceptionType {
        NO_SUCH_FILE,FILE_NOT_FOUND,WRONG_DELIMITER,UNABLE_TO_PARSE,NO_DATA
    }

    public ExceptionType exceptiontype;

    public StateCensusAnalyserException(String name,String message) {
        super(message);
        this.exceptiontype = ExceptionType.valueOf(name);
    }

    public StateCensusAnalyserException(ExceptionType exceptionType, String message) {
        super(message);
        this.exceptiontype=exceptionType;
    }
}
