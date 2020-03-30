package com.census_analyser;

import com.opencsv.bean.CsvBindByName;

public class StateCensusData {

    @CsvBindByName(column = "State")
    public String state;

    @CsvBindByName
    public int population;

    @CsvBindByName
    public int areaInSqKm;

    @CsvBindByName
    public int densityPerSqKm;

}
