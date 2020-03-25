package com.census_analyser;

import com.opencsv.bean.CsvBindByName;

public class StateCensusData {

    @CsvBindByName(column = "State")
    public String state;

    @CsvBindByName
    public int Population;

    @CsvBindByName
    public int AreaInSqKm;

    @CsvBindByName
    public int DensityPerSqKm;
}
