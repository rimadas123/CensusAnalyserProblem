package com.census_analyser;

public class StateCodeDAO {

    public String stateCode;
    public String TIN;
    public String stateName;
    public int srNo;

    public StateCodeDAO(StateCodeData stateCodeData) {
        srNo = stateCodeData.SrNo;
        stateName = stateCodeData.StateName;
        TIN = stateCodeData.TIN;
        stateCode = stateCodeData.StateCode;
    }
}
