package com.census_analyser;

import com.opencsv.bean.CsvBindByName;

public class USCensusData {

    public USCensusData(){}

    public USCensusData(String stateId, String state, int populationDensity, int population, int totalArea){
        this.StateId = stateId;
        this.State = state;
        this.PopulationDensity = populationDensity;
        this.population = population;
        this.TotalArea = totalArea;
    }

    public void setStateId(String stateId){
        this.StateId = stateId;
    }

    public void setPopulation(int population){
        this.population = population;
    }

    public void setState(String state){
        this.State = state;
    }

    public void setTotalArea(double totalArea){
        this.TotalArea = totalArea;
    }

    public void setPopulationDensity(double populationDensity){
        this.PopulationDensity = populationDensity;
    }

    public String getStateId(){
        return StateId;
    }

    public String getState(){
        return State;
    }

    public int getPopulation(){
        return population;
    }

    public double getTotalArea(){
        return TotalArea;
    }

    public double getPopulationDensity(){
        return PopulationDensity;
    }

    @CsvBindByName(column = "StateId",required = true)
    private String StateId;

    @CsvBindByName(column ="State",required = true)
    private String State;

    @CsvBindByName(column ="Population",required = true)
    private int population;

    @CsvBindByName(column = "TotalArea",required = true)
    private double TotalArea;

    @CsvBindByName(column="PopulationDensity",required = true)
    private double PopulationDensity;
}
