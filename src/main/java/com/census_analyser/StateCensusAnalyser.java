package com.census_analyser;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class StateCensusAnalyser {
    private static final String PATTERN_FOR_CSV="^[a-zA-Z0-9./_@]*[.]+[c][s][v]$";

    Map<String, CensusDAO> stateCensusDAOMap = null;
    Map<String, StateCodeData> stateCodeMap = null;

    public StateCensusAnalyser() {
        this.stateCodeMap = new HashMap<>();
        this.stateCensusDAOMap = new HashMap<>();
    }

    public int readCSVFile(String CSV_PATH) throws StateCensusAnalyserException {
        return loadCensusData(CSV_PATH,StateCensusData.class);
    }

    private <E> int loadCensusData(String csvFilePath, Class<E> censusCSVClass) throws StateCensusAnalyserException{
       String extension=getFileExtension(csvFilePath);
       int numberOfRecords=0;
       if(!Pattern.matches(PATTERN_FOR_CSV,extension))
           throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_SUCH_FILE,"No such file");
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))
        ) {
            ICSVBuilder icsvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<E> csvFileIterator = icsvBuilder.getCsvFileIterator(reader, censusCSVClass);
            Iterable<E> csvFileIterable = () -> csvFileIterator;
                if(censusCSVClass.getName().contains("StateCensusData")) {
                    StreamSupport.stream(csvFileIterable.spliterator(), false)
                    .map(StateCensusData.class::cast)
                    .forEach(censusCSV -> stateCensusDAOMap.put(censusCSV.state, new CensusDAO(censusCSV)));
                    return stateCensusDAOMap.size();
                }
                if(censusCSVClass.getName().contains("USCensusData")) {
                    StreamSupport.stream(csvFileIterable.spliterator(), false)
                    .map(StateCensusData.class::cast)
                    .forEach(censusCSV -> stateCensusDAOMap.put(censusCSV.state, new CensusDAO(censusCSV)));
                    return stateCensusDAOMap.size();
                }

        } catch (NoSuchFileException e) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.FILE_NOT_FOUND,
                    "file does not exists");
        } catch (RuntimeException e) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.WRONG_DELIMITER,
                    "wrong delimiter added");
        } catch (IOException e) {
            //e.printStackTrace();
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_DATA,"no data");
        } catch (CSVBuilderException e) {
            //throw new StateCensusAnalyserException(e.exceptionType.name(), e.getMessage());
            e.printStackTrace();
        }
        //return stateCensusDAOMap.size();
        return numberOfRecords;
    }

    public int readStateCodeFile(String CSV_PATH) throws StateCensusAnalyserException {

        if (this.stateCensusDAOMap == null || this.stateCensusDAOMap.size() == 0) {
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

    public int loadUSCensusData(String csvFilePath) throws StateCensusAnalyserException {
        return this.loadCensusData(csvFilePath,USCensusData.class);
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
        if (stateCensusDAOMap == null || stateCensusDAOMap.size() == 0) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_DATA,
                    "no valid data");
        }
        Comparator<CensusDAO> censusDataComparator = Comparator.comparing(censusDAO -> censusDAO.state);
        List<CensusDAO> censusList = stateCensusDAOMap.values().stream().collect(Collectors.toList());
        this.sort(censusDataComparator, censusList);
        String sortedStateCensus = new Gson().toJson(censusList);
        return sortedStateCensus;
    }

    public String getStateCodeWiseSortedCensusData() throws StateCensusAnalyserException {
        if (stateCensusDAOMap == null || stateCensusDAOMap.size() == 0) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_DATA,
                    "no valid data");
        }
        Comparator<CensusDAO> stateCodeDAOComparator = Comparator.comparing(census -> census.stateCode);
        List<CensusDAO> censusList = stateCensusDAOMap.values().stream().collect(Collectors.toList());
        this.sort(stateCodeDAOComparator, censusList);
        String sortedStateCode = new Gson().toJson(censusList);
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
        if (stateCensusDAOMap == null || stateCensusDAOMap.size() == 0) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_DATA,
                    "no valid data");
        }
        Comparator<CensusDAO> censusDataComparator = Comparator.comparing(census -> census.population);
        List<CensusDAO> censusList = stateCensusDAOMap.values().stream().collect(Collectors.toList());
        this.sort(censusDataComparator, censusList);
        Collections.reverse(censusList);
        String sortedStateCensus = new Gson().toJson(censusList);
        return sortedStateCensus;
    }

    public String getStatePopulationDensityWiseSortedCensusData() throws StateCensusAnalyserException {
        if (stateCensusDAOMap == null || stateCensusDAOMap.size() == 0) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_DATA,
                    "no valid data");
        }
        Comparator<CensusDAO> censusDataComparator = Comparator.comparing(census -> census.densityPerSqKm);
        List<CensusDAO> censusList = stateCensusDAOMap.values().stream().collect(Collectors.toList());
        this.sort(censusDataComparator, censusList);
        Collections.reverse(censusList);
        String sortedStateCensus = new Gson().toJson(censusList);
        return sortedStateCensus;
    }

    public String getStateLargestAreaWiseSortedCensusData() throws StateCensusAnalyserException {
        if (stateCensusDAOMap == null || stateCensusDAOMap.size() == 0) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_DATA,
                    "no valid data");
        }
        Comparator<CensusDAO> censusDataComparator = Comparator.comparing(census -> census.areaInSqKm);
        List<CensusDAO> censusList = stateCensusDAOMap.values().stream().collect(Collectors.toList());
        this.sort(censusDataComparator, censusList);
        Collections.reverse(censusList);
        String sortedStateCensus = new Gson().toJson(censusList);
        return sortedStateCensus;
    }
}