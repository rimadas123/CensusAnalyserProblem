package com.census_analyser;

import java.util.Map;

public class USCensusAdapter extends CensusAdapter {

    @Override
    public Map<String, CensusDAO> loadCensusData(String... csv_path) throws StateCensusAnalyserException {
        return super.loadCensusData(USCensusData.class, csv_path[0]);
    }
}
