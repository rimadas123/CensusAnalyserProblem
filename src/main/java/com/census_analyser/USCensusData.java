package com.census_analyser;

import com.opencsv.bean.CsvBindByName;

public class USCensusData {

    @CsvBindByName(column = "StateId",required = true)
    public String StateId;

    @CsvBindByName(column ="State",required = true)
    public String state;

    @CsvBindByName(column ="Population",required = true)
    public int population;

    @CsvBindByName(column = "TotalArea",required = true)
    public double TotalArea;

    @CsvBindByName(column="PopulationDensity",required = true)
    public double PopulationDensity;
}
