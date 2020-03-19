package com.census_analyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.StreamSupport;

public class StateCensusAnalyser {

    public int readCSVFile(String CSV_PATH) throws StateCensusAnalyserException {
        int count = 0;
        String fileName = getFileExtension(CSV_PATH);
        if (!fileName.equals(".csv"))
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_SUCH_FILE, "file does not exists");
        try (Reader reader = Files.newBufferedReader(Paths.get(CSV_PATH))
        ) {
            Iterator<StateCensusData> censusDataIterator = this.getCsvFileIterator(reader, StateCensusData.class);
            Iterable<StateCensusData> iterable = () -> censusDataIterator;
            count= (int) StreamSupport.stream(iterable.spliterator(),false).count();
            return count;

        }catch (NoSuchFileException e){
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.FILE_NOT_FOUND,"file does not exists");
        } catch (RuntimeException e){
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.WRONG_DELIMITER,"wrong delimiter has been added");
        } catch (IOException e){
            e.printStackTrace();
        }
        return count;
    }


    public int readStateCodeFile(String CSV_PATH) throws StateCensusAnalyserException {
        int count = 0;
        String fileName = getFileExtension(CSV_PATH);
        if(!fileName.equals(".csv")){
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_SUCH_FILE,"wrong file type");
        }
        try(Reader reader = Files.newBufferedReader(Paths.get(CSV_PATH));
        ){

            Iterator<StateCodeData> stateCodeDataIterator = this.getCsvFileIterator(reader,StateCodeData.class);
            Iterable<StateCodeData> iterator = () -> stateCodeDataIterator;
            count= (int) StreamSupport.stream(iterator.spliterator(),false).count();
            return count;
        } catch (NoSuchFileException e){
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.FILE_NOT_FOUND,"file does not exists");
        } catch (RuntimeException e){
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.WRONG_DELIMITER,"wrong delimiter has been added");
        } catch (IOException e){
            e.printStackTrace();
        }
        return count;
    }

    private <E> Iterator<E> getCsvFileIterator(Reader reader, Class<E> csvClass) {
        CsvToBeanBuilder<E> csvToBeanBuilder = new CsvToBeanBuilder(reader);
        csvToBeanBuilder.withType(csvClass);
        csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
        CsvToBean<E> csvToBean = csvToBeanBuilder.build();
        return csvToBean.iterator();
    }

    public static String getFileExtension(String file) {
        String fileName = "";
        try {
            if (file != null) {
                fileName = file.substring(file.lastIndexOf("."));
            }
        } catch (Exception e) {
            fileName = "";
        }
        return fileName;
    }

}
