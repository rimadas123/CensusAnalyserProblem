package com.census_analyser;

public class CensusDAO {
    public double TotalArea;
    public double PopulationDensity;
    public String StateId;
    public int densityPerSqKm;
    public int areaInSqKm;
    public int population;
    public String state;
    public String stateCode;
    public String tin;
    public int srNo;

    public CensusDAO(StateCensusData stateCensusData){
        this.state = stateCensusData.state;
        this.population = stateCensusData.population;
        this.areaInSqKm = stateCensusData.areaInSqKm;
        this.densityPerSqKm = stateCensusData.densityPerSqKm;
    }

    public CensusDAO(StateCodeData stateCodeData){
        this.srNo = stateCodeData.getSrNo();
        this.state = stateCodeData.getStateName();
        this.tin = stateCodeData.getTIN();
        this.stateCode = stateCodeData.getStateCode();
    }

    public CensusDAO(USCensusData censusCSV) {
        this.state = censusCSV.getState();
        this.stateCode = censusCSV.getStateId();
        this.population = censusCSV.getPopulation();
        this.PopulationDensity = censusCSV.getPopulationDensity();
        this.TotalArea = censusCSV.getTotalArea();
    }

}
