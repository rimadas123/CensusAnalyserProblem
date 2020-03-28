package com.census_analyser;

import com.opencsv.bean.CsvBindByName;

public class USCensusData {

    @CsvBindByName(column = "StateId",required = true)
    public String StateId;

    @CsvBindByName
    public String state;

    @CsvBindByName
    public int population;

    @CsvBindByName
    public double TotalArea;

    @CsvBindByName
    public double PopulationDensity;
}
