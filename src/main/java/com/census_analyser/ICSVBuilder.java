package com.census_analyser;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public interface ICSVBuilder {
    public <E> Iterator<E> getCsvFileIterator(Reader reader, Class<E> csvClass) throws CSVBuilderException;
    public <E> List<E> getCsvFileList(Reader reader, Class<E> csvClass) throws CSVBuilderException;

}
