package com.census_analyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Iterator;

public class StateCensusAnalyser {

    public static String getFileExtension(String file) {
        String fileName = "";
        try {
            if (file != null) {
                fileName = file.substring(file.lastIndexOf("."));
            }
        } catch (Exception e) {
            fileName = "";
        }
        return fileName;
    }

    private <E> Iterator<E> getCsvFileIterator(Reader reader, Class<E> csvClass) {
        CsvToBeanBuilder<E> csvToBeanBuilder = new CsvToBeanBuilder(reader);
        csvToBeanBuilder.withType(csvClass);
        csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
        CsvToBean<E> csvToBean = csvToBeanBuilder.build();
        return csvToBean.iterator();
    }

    public int readCSVFile(String CSV_PATH) throws StateCensusAnalyserException, IOException {
        int count = 0;
        String fileName = getFileExtension(CSV_PATH);
        if (!fileName.equals(".csv"))
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_SUCH_FILE, "file does not exists");
        try (Reader reader = Files.newBufferedReader(Paths.get(CSV_PATH))
        ) {
            Iterator<StateCensusData> censusDataIterator = this.getCsvFileIterator(reader, StateCensusData.class);

            while (censusDataIterator.hasNext()) {
                count++;
                StateCensusData csvData = censusDataIterator.next();
                System.out.println("State : " + csvData.State);
                System.out.println("Population : " + csvData.Population);
                System.out.println("AreaInSqKm : " + csvData.AreaInSqKm);
                System.out.println("DensityPerSqKm : " + csvData.DensityPerSqKm);
                System.out.println("===============");
            }

            try (Reader reader1 = Files.newBufferedReader(Paths.get(CSV_PATH));
            ) {
                CsvToBean<StateCodeData> csvToBean = new CsvToBeanBuilder<StateCodeData>(reader1)
                        .withType(StateCodeData.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();
                Iterator<StateCodeData> codeDataIterator = csvToBean.iterator();

                while (censusDataIterator.hasNext()) {
                    count++;
                    StateCodeData csvData = codeDataIterator.next();
                    System.out.println("Sr no : " + csvData.SrNo);
                    System.out.println("State Name : " + csvData.StateName);
                    System.out.println("TIN : " + csvData.TIN);
                    System.out.println("State Code : " + csvData.StateCode);
                    System.out.println("===============");
                }
            } catch (RuntimeException e) {
                throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.WRONG_DELIMITER, "wrong delimiter used in file");
            } catch (NoSuchFileException e) {
                throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.FILE_NOT_FOUND, "File not found");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return count;
        }
    }
}
