package com.census_analyser;

import com.opencsv.bean.CsvBindByName;

public class StateCodeData {

    public void setSrNo(int srNo){
        this.SrNo = srNo;
    }

    public void setStateName(String stateName){
        this.StateName = stateName;
    }

    public void setStateCode(String stateCode){
        this.StateCode = stateCode;
    }

    public void setTIN(String tin){
        this.TIN = tin;
    }

    public int getSrNo(){
        return SrNo;
    }

    public String getStateName(){
        return StateName;
    }

    public String getStateCode(){
        return StateCode;
    }

    public String getTIN(){
        return TIN;
    }

    @CsvBindByName(column = "SrNo")
    private int SrNo;

    @CsvBindByName
    private String StateName;

    @CsvBindByName
    private String TIN;

    @CsvBindByName
    private String StateCode;
}
