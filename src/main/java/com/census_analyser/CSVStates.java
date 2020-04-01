package com.census_analyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Iterator;

public class CSVStates {
    public int readStateCodeFile(String CSV_PATH) throws StateCensusAnalyserException {
        int count = 0;
        String fileName = getFileExtension(CSV_PATH);
        if(!fileName.equals(".csv")){
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_SUCH_FILE,"wrong file type");
        }
        try(Reader reader = Files.newBufferedReader(Paths.get(CSV_PATH));
        ){
            CsvToBean<StateCodeData> csvToBean = new CsvToBeanBuilder<StateCodeData>(reader)
                    .withType(StateCodeData.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            Iterator<StateCodeData> censusDataIterator = csvToBean.iterator();

            while (censusDataIterator.hasNext()){
                count++;
                StateCodeData csvData = censusDataIterator.next();
                System.out.println("Sr no : " +csvData.getSrNo());
                System.out.println("State Name : "+csvData.getStateName());
                System.out.println("TIN : "+csvData.getTIN());
                System.out.println("State Code : "+csvData.getStateCode());
                System.out.println("===============");
            }
        } catch (NoSuchFileException e){
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.FILE_NOT_FOUND,"file does not exists");
        } catch (RuntimeException e){
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.WRONG_DELIMITER,"wrong delimiter has been added");
        } catch (IOException e){
            e.printStackTrace();
        }
        return count;
    }

    private static String getFileExtension(String file){
        String fileName = "";
        try{
            if(file != null){
                fileName=file.substring(file.lastIndexOf("."));
            }
        } catch (Exception e){
            fileName="";
        }
        return fileName;
    }

}
