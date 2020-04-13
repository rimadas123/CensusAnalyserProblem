package com.census_analyser;

import java.util.Map;

public class CensusAnalyserFactory {
    public static CensusAdapter getCensusData(StateCensusAnalyser.COUNTRY country) {
        if (country.equals(StateCensusAnalyser.COUNTRY.INDIA))
            return new IndiaCensusAdapter();
        if (country.equals(StateCensusAnalyser.COUNTRY.US))
            return new USCensusAdapter();
        return null;
    }
}
