package com.census_analyser;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public interface ICSVBuilder {
     <E> Iterator<E> getCsvFileIterator(Reader reader, Class<E> csvClass) throws CSVBuilderException;
     <E> List<E> getCsvFileList(Reader reader, Class<E> csvClass) throws CSVBuilderException;
}
