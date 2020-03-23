package com.census_analyser;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

public class StateCensusAnalyser {

    List<StateCensusData> censusCSVList = null;

    public int readCSVFile(String CSV_PATH) throws StateCensusAnalyserException {
        int count = 0;
        String fileName = getFileExtension(CSV_PATH);
        if (!fileName.equals(".csv"))
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_SUCH_FILE,
                    "file does not exists");
        try (Reader reader = Files.newBufferedReader(Paths.get(CSV_PATH))
        ) {
            ICSVBuilder icsvBuilder = CSVBuilderFactory.createCSVBuilder();
            censusCSVList = icsvBuilder.getCsvFileList(reader, StateCensusData.class);
            return censusCSVList.size();
        }catch (NoSuchFileException e){
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.FILE_NOT_FOUND,
                    "file does not exists");
        } catch (RuntimeException e){
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.WRONG_DELIMITER,
                    "wrong delimiter has been added");
        } catch (IOException e){
            e.printStackTrace();
        } catch (CSVBuilderException e) {
            throw new StateCensusAnalyserException(e.exceptionType.name(),e.getMessage());
        }
        return count;
    }


    public int readStateCodeFile(String CSV_PATH) throws StateCensusAnalyserException {
        int count = 0;
        String fileName = getFileExtension(CSV_PATH);
        if(!fileName.equals(".csv")){
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_SUCH_FILE,
                    "wrong file type");
        }
        try(Reader reader = Files.newBufferedReader(Paths.get(CSV_PATH));
        ){
            ICSVBuilder icsvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<StateCodeData> stateCodeDataIterator = icsvBuilder.getCsvFileIterator(reader, StateCodeData.class);
            return getCount(stateCodeDataIterator);
        } catch (NoSuchFileException e){
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.FILE_NOT_FOUND,
                    "file does not exists");
        } catch (RuntimeException e){
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.WRONG_DELIMITER,
                    "wrong delimiter has been added");
        } catch (IOException e){
            e.printStackTrace();
        } catch (CSVBuilderException e) {
            throw new StateCensusAnalyserException(e.exceptionType.name(),e.getMessage());
        }
        return count;
    }

    private <E> int getCount(Iterator<E> iterator){
        Iterable<E> csvIterator = () -> iterator;
        int count= (int) StreamSupport.stream(csvIterator.spliterator(),false).count();
        return count;
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

    public String getStateWiseSortedCensusData() throws StateCensusAnalyserException {
            if(censusCSVList == null || censusCSVList.size() == 0 ){
                throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_DATA,
                        "no valid data");
            }
            Comparator<StateCensusData> censusDataComparator = Comparator.comparing(census -> census.state);
            this.sort(censusDataComparator);
            String sortedStateCensus = new Gson().toJson(censusCSVList);
            return sortedStateCensus;
    }

    private void sort(Comparator<StateCensusData> censusDataComparator) {
        for(int i = 0; i < censusCSVList.size() -1; i++){
            for(int j = 0; j < censusCSVList.size() - i - 1; j++){
                StateCensusData censusData1 = censusCSVList.get(j);
                StateCensusData censusData2 = censusCSVList.get(j+1);
                if(censusDataComparator.compare(censusData1,censusData2) > 0){
                    censusCSVList.set(j,censusData2);
                    censusCSVList.set(j+1,censusData1);
                }
            }
        }
    }
}
