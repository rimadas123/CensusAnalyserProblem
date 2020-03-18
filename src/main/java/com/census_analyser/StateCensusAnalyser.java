package com.census_analyser;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

public class StateCensusAnalyser {

    public int readCSVFile(String CSV_PATH) throws StateCensusAnalyserException {
        int count = 0;
        String fileName = getFileExtension(CSV_PATH);
        if(!fileName.equals(".csv"))
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_SUCH_FILE,"file does not exists");
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
        } catch (RuntimeException e){
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.WRONG_DELIMITER,"wrong delimiter used in file");
        }
        catch (NoSuchFileException e) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.FILE_NOT_FOUND,"File not found");
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return count;
    }

    public static String getFileExtension(String file){
        String fileName = "";
        try{
            if(file != null){
                fileName=file.substring(file.lastIndexOf("."));
            }
        } catch (Exception e){
            fileName="";
        }
        return fileName;
    }

    public static void main(String[] args){
        System.out.println("Welcome to Census Analyser Problem");
    }
}
