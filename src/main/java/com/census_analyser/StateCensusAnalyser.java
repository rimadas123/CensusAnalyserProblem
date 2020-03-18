package com.census_analyser;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StateCensusAnalyser {

    public int readCSVFile(String CSV_PATH) throws IOException {
        int count = 0;
        try(Reader reader = Files.newBufferedReader(Paths.get(CSV_PATH));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
        ){
            String[] records;
            while((records = csvReader.readNext()) !=null) {
                count++;
                System.out.println("State : "+records[0]);
                System.out.println("Population : "+records[1]);
                System.out.println("AreaInSqKm : "+records[2]);
                System.out.println("DensityPerSqKm : "+records[3]);
                System.out.println("==================");
            }
        }
        return count;
    }

    public static void main(String[] args){
        System.out.println("Welcome to Census Analyser Problem");
    }
}
