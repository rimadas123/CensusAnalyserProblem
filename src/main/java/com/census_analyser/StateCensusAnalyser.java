package com.census_analyser;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class StateCensusAnalyser {

    List<StateCensusDAO> censusList = null;
    List<StateCodeData> codeList = null;
    Map<String, StateCensusDAO> stateCensusDAOMap = null;
    Map<String, StateCodeData> stateCodeMap = null;

    public StateCensusAnalyser() {
        this.stateCodeMap = new HashMap<>();
        this.stateCensusDAOMap = new HashMap<>();
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
                StateCensusData stateCensus = csvFileIterator.next();
                this.stateCensusDAOMap.put(stateCensus.state,new StateCensusDAO(stateCensus));
                censusList = stateCensusDAOMap.values().stream().collect(Collectors.toList());
            }
            return this.censusList.size();
        } catch (NoSuchFileException e) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.FILE_NOT_FOUND,
                    "file does not exists");
        } catch (RuntimeException e) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.WRONG_DELIMITER,
                    "wrong delimiter has been added");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CSVBuilderException e) {
            throw new StateCensusAnalyserException(e.exceptionType.name(), e.getMessage());
        }
        return count;
    }


    public int readStateCodeFile(String CSV_PATH) throws StateCensusAnalyserException {

        if (this.censusList == null || this.censusList.size() == 0) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.File_Empty,
                    "please load census map");
        }
        int count = 0;
        String fileName = getFileExtension(CSV_PATH);
        if (!fileName.equals(".csv")) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_SUCH_FILE,
                    "wrong file type");
        }
        try (Reader reader = Files.newBufferedReader(Paths.get(CSV_PATH))
        ) {
            ICSVBuilder icsvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<StateCodeData> stateCodeDataIterator = icsvBuilder.getCsvFileIterator(reader, StateCodeData.class);
            Iterable<StateCodeData> csvIterable = () -> stateCodeDataIterator;
            StreamSupport.stream(csvIterable.spliterator(),false)
                    .filter(csvState -> stateCensusDAOMap.get(csvState.StateName) != null)
                    .forEach(csvState -> stateCensusDAOMap.get(csvState.StateName).stateCode = csvState.StateCode);
            return stateCensusDAOMap.size();
        } catch (NoSuchFileException e) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.FILE_NOT_FOUND,
                    "file does not exists");
        } catch (RuntimeException e) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.WRONG_DELIMITER,
                    "wrong delimiter has been added");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CSVBuilderException e) {
            throw new StateCensusAnalyserException(e.exceptionType.name(), e.getMessage());
        }
        return count;
    }

    public int loadUSCensusData(String usCensusCsvPath) throws StateCensusAnalyserException {
        int count = 0;
        try (Reader reader = Files.newBufferedReader(Paths.get(usCensusCsvPath))) {
            ICSVBuilder icsvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<USCensusData> censusDataIterator = icsvBuilder.getCsvFileIterator(reader, USCensusData.class);
            Iterable<USCensusData> csvIterable = () -> censusDataIterator;
            count= (int) StreamSupport.stream(csvIterable.spliterator(),false).count();
            return count;
        } catch (NoSuchFileException e) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.FILE_NOT_FOUND,
                    "file does not exists");
        } catch (RuntimeException e) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.WRONG_DELIMITER,
                    "wrong delimiter has been added");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CSVBuilderException e) {
            throw new StateCensusAnalyserException(e.exceptionType.name(), e.getMessage());
        }
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

    private <E> int getCount(Iterator<E> iterator){
        Iterable<E> csvIterator = () -> iterator;
        int count= (int) StreamSupport.stream(csvIterator.spliterator(),false).count();
        return count;
    }

    public String getStateWiseSortedCensusData() throws StateCensusAnalyserException {
        if (censusList == null || censusList.size() == 0) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_DATA,
                    "no valid data");
        }
        Comparator<StateCensusDAO> censusDataComparator = Comparator.comparing(census -> census.state);
        this.sort(censusDataComparator, censusList);
        String sortedStateCensus = new Gson().toJson(this.censusList);
        return sortedStateCensus;
    }

    public String getStateCodeWiseSortedCensusData() throws StateCensusAnalyserException {
        if (censusList == null || censusList.size() == 0) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_DATA,
                    "no valid data");
        }
        Comparator<StateCensusDAO> stateCodeDAOComparator = Comparator.comparing(census -> census.stateCode);
        this.sort(stateCodeDAOComparator, censusList);
        String sortedStateCode = new Gson().toJson(this.censusList);
        return sortedStateCode;
    }

    private <E> void sort(Comparator<E> censusDataComparator, List list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                E censusData1 = (E) list.get(j);
                E censusData2 = (E) list.get(j + 1);
                if (censusDataComparator.compare(censusData1, censusData2) > 0) {
                    list.set(j, censusData2);
                    list.set(j + 1, censusData1);
                }
            }
        }
    }

    public String getStatePopulatedWiseSortedCensusData() throws StateCensusAnalyserException {
        if (censusList == null || censusList.size() == 0) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_DATA,
                    "no valid data");
        }
        Comparator<StateCensusDAO> censusDataComparator = Comparator.comparing(census -> census.population);
        this.sort(censusDataComparator, censusList);
        Collections.reverse(censusList);
        String sortedStateCensus = new Gson().toJson(this.censusList);
        return sortedStateCensus;
    }

    public String getStatePopulationDensityWiseSortedCensusData() throws StateCensusAnalyserException {
        if (censusList == null || censusList.size() == 0) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_DATA,
                    "no valid data");
        }
        Comparator<StateCensusDAO> censusDataComparator = Comparator.comparing(census -> census.densityPerSqKm);
        this.sort(censusDataComparator, censusList);
        Collections.reverse(censusList);
        String sortedStateCensus = new Gson().toJson(this.censusList);
        return sortedStateCensus;
    }

    public String getStateLargestAreaWiseSortedCensusData() throws StateCensusAnalyserException {
        if (censusList == null || censusList.size() == 0) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_DATA,
                    "no valid data");
        }
        Comparator<StateCensusDAO> censusDataComparator = Comparator.comparing(census -> census.areaInSqKm);
        this.sort(censusDataComparator, censusList);
        Collections.reverse(censusList);
        String sortedStateCensus = new Gson().toJson(this.censusList);
        return sortedStateCensus;
    }


}