package com.census_analyser;

import com.opencsv.bean.CsvBindByName;

public class StateCodeData {

    @CsvBindByName(column = "SrNo")
    public String SrNo;

    @CsvBindByName
    public String StateName;

    @CsvBindByName
    public String TIN;

    @CsvBindByName
    public String StateCode;
}
