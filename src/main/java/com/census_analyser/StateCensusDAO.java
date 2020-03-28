package com.census_analyser;

public class StateCensusDAO {
    public int densityPerSqKm;
    public int areaInSqKm;
    public int population;
    public String state;
    public String stateCode;

    public StateCensusDAO(StateCensusData stateCensusData){
        state = stateCensusData.state;
        population = stateCensusData.Population;
        areaInSqKm = stateCensusData.AreaInSqKm;
        densityPerSqKm = stateCensusData.DensityPerSqKm;
    }

}
