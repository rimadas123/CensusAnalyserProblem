package com.census_analyser;

import java.util.Comparator;

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

    public static Comparator<CensusDAO> getSortComparator(StateCensusAnalyser.SORTING_MODE mode) {
        if (mode.equals(StateCensusAnalyser.SORTING_MODE.STATE))
            return Comparator.comparing(census -> census.state);
        if (mode.equals(StateCensusAnalyser.SORTING_MODE.POPULATION))
            return Comparator.comparing(census -> census.population);
        if (mode.equals(StateCensusAnalyser.SORTING_MODE.DENSITY))
            return Comparator.comparing(census -> census.densityPerSqKm);
        if (mode.equals(StateCensusAnalyser.SORTING_MODE.AREA))
            return Comparator.comparing(census -> census.areaInSqKm);
        if (mode.equals(StateCensusAnalyser.SORTING_MODE.STATECODE))
            return Comparator.comparing(census -> census.stateCode);
        return null;
    }

    public Object getCensusDTO(StateCensusAnalyser.COUNTRY country) {
        if (country.equals(StateCensusAnalyser.COUNTRY.INDIA))
            return new StateCensusData(state, stateCode, population, areaInSqKm, densityPerSqKm);
        return new USCensusData(stateCode, state, population, areaInSqKm, population);
    }
}
