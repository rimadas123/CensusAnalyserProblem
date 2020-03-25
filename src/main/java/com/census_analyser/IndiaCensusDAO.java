package com.census_analyser;

public class IndiaCensusDAO {

    public int densityPerSqKm;
    public int areaInSqKm;
    public int population;
    public String state;

    public IndiaCensusDAO(StateCensusData stateCensusData){
        state = stateCensusData.state;
        population = stateCensusData.Population;
        areaInSqKm = stateCensusData.AreaInSqKm;
        densityPerSqKm = stateCensusData.DensityPerSqKm;
    }
}
