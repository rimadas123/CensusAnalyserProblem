package com.census_analyser;

public class StateCensusAnalyserException extends Throwable {
    public enum ExceptionType {
        NO_SUCH_FILE,FILE_NOT_FOUND
    }

    public ExceptionType exceptiontype;

    public StateCensusAnalyserException(ExceptionType exceptionType, String message) {
        super(message);
        this.exceptiontype=exceptionType;
    }
}
