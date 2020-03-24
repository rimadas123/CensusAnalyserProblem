package com.census_analyser;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

public class StateCensusAnalyser {

    List<IndiaCensusDAO> censusList = null;
    List<StateCodeDAO> codeDAOList = null;

    public StateCensusAnalyser() {
        this.censusList = new ArrayList<>();
        this.codeDAOList = new ArrayList<>();
    }

    public int readCSVFile(String CSV_PATH) throws StateCensusAnalyserException {
        int count = 0;
        String fileName = getFileExtension(CSV_PATH);
        if (!fileName.equals(".csv"))
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_SUCH_FILE,
                    "file does not exists");
        try (Reader reader = Files.newBufferedReader(Paths.get(CSV_PATH))
        ) {
            ICSVBuilder icsvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<StateCensusData> csvFileIterator = icsvBuilder.getCsvFileIterator(reader, StateCensusData.class);
            while(csvFileIterator.hasNext()){
                this.censusList.add(new IndiaCensusDAO(csvFileIterator.next()));
            }
            return this.censusList.size();
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
            while(stateCodeDataIterator.hasNext()){
                this.codeDAOList.add(new StateCodeDAO(stateCodeDataIterator.next()));
            }
            return this.codeDAOList.size();
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
            if(censusList == null || censusList.size() == 0 ){
                throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_DATA,
                        "no valid data");
            }
            Comparator<IndiaCensusDAO> censusDataComparator = Comparator.comparing(census -> census.state);
            this.sort(censusDataComparator,censusList);
            String sortedStateCensus = new Gson().toJson(this.censusList);
            return sortedStateCensus;
    }

    public String getStateCodeWiseSortedCensusData() throws StateCensusAnalyserException {
            if(codeDAOList == null || codeDAOList.size() == 0){
                throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_DATA,
                        "no valid data");
            }
            Comparator<StateCodeDAO> stateCodeDAOComparator = Comparator.comparing(census -> census.StateCode);
            this.sort(stateCodeDAOComparator, codeDAOList);
            String sortedStateCode = new Gson().toJson(this.codeDAOList);
            return sortedStateCode;
    }

    private <E> void sort(Comparator<E> censusDataComparator,List list) {
        for(int i = 0; i < list.size() -1; i++){
            for(int j = 0; j < list.size() - i - 1; j++){
                E censusData1 =(E) list.get(j);
                E censusData2 = (E)list.get(j+1);
                if(censusDataComparator.compare(censusData1,censusData2) > 0){
                    list.set(j,censusData2);
                    list.set(j+1,censusData1);
                }
            }
        }
    }
}
