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
        this.srNo = stateCodeData.SrNo;
        this.state = stateCodeData.StateName;
        this.tin = stateCodeData.TIN;
        this.stateCode = stateCodeData.StateCode;
    }

    public CensusDAO(USCensusData censusCSV) {
        this.state = censusCSV.State;
        this.stateCode = censusCSV.StateId;
        this.population = censusCSV.population;
        this.PopulationDensity = censusCSV.PopulationDensity;
        this.TotalArea = censusCSV.TotalArea;
    }

}
