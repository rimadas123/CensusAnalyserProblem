package com.census_analyser;

import com.opencsv.bean.CsvBindByName;

public class StateCensusData {

    @CsvBindByName(column = "State")
    public String state;

    @CsvBindByName
    public String Population;

    @CsvBindByName
    public String AreaInSqKm;

    @CsvBindByName
    public String DensityPerSqKm;
}
