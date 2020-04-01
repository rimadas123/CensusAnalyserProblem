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
    public static final String PATTERN_FOR_CSV="^[a-zA-Z0-9./_@]*[.]+[c][s][v]$";

    Map<String, CensusDAO> stateCensusDAOMap = null;
    COUNTRY country;

    public enum COUNTRY { INDIA, US }

    public StateCensusAnalyser() { }

    public StateCensusAnalyser(COUNTRY country) {
        this.country=country;
    }

    public int loadCensusData(COUNTRY country,String... CSV_PATH) throws StateCensusAnalyserException {
        stateCensusDAOMap = new CensusLoader().loadCensusData(country,CSV_PATH);
         return stateCensusDAOMap.size();
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
        Comparator<CensusDAO> stateCodeDAOComparator = Comparator.comparing(censusDAO -> censusDAO.stateCode);
        List<CensusDAO> censusList = stateCensusDAOMap.values().stream().collect(Collectors.toList());
        this.sort(stateCodeDAOComparator, censusList);
        String sortedStateCode = new Gson().toJson(censusList);
        return sortedStateCode;
    }

    private void sort(Comparator<CensusDAO> censusDataComparator, List<CensusDAO> censusList) {
        for (int i = 0; i < censusList.size() - 1; i++) {
            for (int j = 0; j < censusList.size() - i - 1; j++) {
                CensusDAO censusData1 = censusList.get(j);
                CensusDAO censusData2 = censusList.get(j + 1);
                if (censusDataComparator.compare(censusData1, censusData2) > 0) {
                    censusList.set(j, censusData2);
                    censusList.set(j + 1, censusData1);
                }
            }
        }
    }

    public String getStatePopulatedWiseSortedCensusData() throws StateCensusAnalyserException {
        if (stateCensusDAOMap == null || stateCensusDAOMap.size() == 0) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_DATA,
                    "no valid data");
        }
        Comparator<CensusDAO> censusDataComparator = Comparator.comparing(censusDAO -> censusDAO.population);
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
        Comparator<CensusDAO> censusDataComparator = Comparator.comparing(censusDAO -> censusDAO.densityPerSqKm);
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
        Comparator<CensusDAO> censusDataComparator = Comparator.comparing(censusDAO -> censusDAO.areaInSqKm);
        List<CensusDAO> censusList = stateCensusDAOMap.values().stream().collect(Collectors.toList());
        this.sort(censusDataComparator, censusList);
        Collections.reverse(censusList);
        String sortedStateCensus = new Gson().toJson(censusList);
        return sortedStateCensus;
    }
}