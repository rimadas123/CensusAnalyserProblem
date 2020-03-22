package com.census_analyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public class OpenCSVBuilder<E> implements ICSVBuilder {

    @Override
    public Iterator <E> getCsvFileIterator(Reader reader, Class csvClass) throws CSVBuilderException {
            return this.getCSVBean(reader,csvClass).iterator();
    }

    @Override
    public List<E> getCsvFileList(Reader reader, Class csvClass) throws CSVBuilderException {
        return this.getCSVBean(reader,csvClass).parse();
    }

    private CsvToBean<E> getCSVBean(Reader reader, Class<E> csvClass) throws CSVBuilderException {
        try {
            CsvToBeanBuilder<E> csvToBeanBuilder = new CsvToBeanBuilder(reader);
            csvToBeanBuilder.withType(csvClass);
            csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
            CsvToBean<E> csvToBean = csvToBeanBuilder.build();
            return csvToBean;
        } catch (IllegalStateException e) {
            throw new CSVBuilderException(CSVBuilderException.ExceptionType.UNABLE_TO_PARSE,
                    "unable to parse csv file");
        }
    }
}
