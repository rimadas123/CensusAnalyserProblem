package com.census_analyser;

import com.opencsv.bean.CsvBindByName;

public class StateCensusData {

    public StateCensusData(){}

    public StateCensusData(String state, String stateCode, int population, int areaInSqKm, int densityPerSqKm){
        this.state = state;
        this.stateCode = stateCode;
        this.population = population;
        this.areaInSqKm = areaInSqKm;
        this.densityPerSqKm = densityPerSqKm;
    }

    //setter
    public void setState(String state){
        this.state = state;
    }

    public void setPopulation(int population){
        this.population = population;
    }

    public void setAreaInSqKm(int areaInSqKm){
        this.areaInSqKm = areaInSqKm;
    }

    public void setDensityPerSqKm(int densityPerSqKm){
        this.densityPerSqKm = densityPerSqKm;
    }

    //getter
    public String getState(){
        return state;
    }

    public int getPopulation(){
        return population;
    }

    public int getDensityPerSqKm(){
        return densityPerSqKm;
    }

    public int getAreaInSqKm(){
        return areaInSqKm;
    }

    @CsvBindByName(column = "State", required = true)
    public String state;

    @CsvBindByName(column = "Population", required = true)
    public int population;

    @CsvBindByName(column = "AreaInSqKm", required = true)
    public int areaInSqKm;

    @CsvBindByName(column = "DensityPerSqKm", required = true)
    public int densityPerSqKm;

    private String stateCode = new StateCodeData().getStateCode();

}
