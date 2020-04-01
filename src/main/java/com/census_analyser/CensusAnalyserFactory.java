package com.census_analyser;

import java.util.Map;

public class CensusAnalyserFactory {
    public static Map<String, CensusDAO> getCensusData(StateCensusAnalyser.COUNTRY country, String... csvPath) throws StateCensusAnalyserException {
        if (country.equals(StateCensusAnalyser.COUNTRY.INDIA)) {
            return new IndiaCensusAdapter().loadCensusData(csvPath);
        }
        if (country.equals(StateCensusAnalyser.COUNTRY.US)) {
            return new USCensusAdapter().loadCensusData(csvPath);
        }
        return null;
    }
}
