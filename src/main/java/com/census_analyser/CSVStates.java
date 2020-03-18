package com.census_analyser;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CSVStates {
    public int readStateCodeFile(String CSV_PATH) {
        int count = 0;
        try(Reader reader = Files.newBufferedReader(Paths.get(CSV_PATH));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
        ){
            String[] records;
            while((records = csvReader.readNext()) !=null) {
                count++;
                System.out.println("Sr no : "+records[0]);
                System.out.println("StateName : "+records[1]);
                System.out.println("TIN : "+records[2]);
                System.out.println("StateCode : "+records[3]);
                System.out.println("==================");
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return count;
    }

}
