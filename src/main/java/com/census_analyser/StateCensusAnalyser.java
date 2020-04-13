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

    Map<String, CensusDAO> stateCensusDAOMap = new HashMap<>();
    COUNTRY country;

    public enum COUNTRY { INDIA, US }

    public enum SORTING_MODE { STATE, POPULATION, DENSITY, AREA, STATECODE }

    public StateCensusAnalyser() { }

    public StateCensusAnalyser(COUNTRY country) {
        this.country=country;
    }

    public int loadCensusData(String... CSV_PATH) throws StateCensusAnalyserException {
        CensusAdapter censusData = CensusAnalyserFactory.getCensusData(country);
        stateCensusDAOMap = censusData.loadCensusData(CSV_PATH);
        return stateCensusDAOMap.size();
    }

    public String getSortCensusData(SORTING_MODE mode) throws StateCensusAnalyserException {
        if (stateCensusDAOMap == null || stateCensusDAOMap.size() == 0) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_DATA, "Census data not found");
        }
        ArrayList censusList = stateCensusDAOMap.values().stream()
                .sorted(CensusDAO.getSortComparator(mode))
                .map(censusDAO -> censusDAO.getCensusDTO(country))
                .collect(Collectors.toCollection(ArrayList::new));
        return new Gson().toJson(censusList);
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