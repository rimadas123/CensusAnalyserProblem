package com.census_analyser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

import static com.census_analyser.StateCensusAnalyser.PATTERN_FOR_CSV;
import static com.census_analyser.StateCensusAnalyser.getFileExtension;

public class IndiaCensusAdapter extends CensusAdapter {

    @Override
    public Map<String, CensusDAO> loadCensusData(String... csvPath) throws StateCensusAnalyserException{
        Map<String, CensusDAO> censusMap = super.loadCensusData(USCensusData.class, csvPath[0]);
        if( csvPath.length == 1)
            return censusMap;
        return this.loadStateCodeCensusData(censusMap, csvPath[1]);
    }

    private <E> Map<String, CensusDAO> loadStateCodeCensusData(Map<String, CensusDAO> censusMap, String csvFilePath) throws StateCensusAnalyserException {

        if (censusMap == null || censusMap.size() == 0) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.File_Empty,
                    "please load census map");
        }
        int count = 0;
        String fileName = getFileExtension(csvFilePath);
        if (!fileName.equals(".csv")) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_SUCH_FILE,
                    "wrong file type");
        }
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))
        ) {
            ICSVBuilder icsvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<StateCodeData> stateCodeDataIterator = icsvBuilder.getCsvFileIterator(reader, StateCodeData.class);
            Iterable<StateCodeData> csvIterable = () -> stateCodeDataIterator;
            StreamSupport.stream(csvIterable.spliterator(),false)
                    .filter(csvState -> censusMap.get(csvState.getStateName()) != null)
                    .forEach(csvState -> censusMap.get(csvState.getStateName()).stateCode = csvState.getStateCode());
            return censusMap;
        } catch (NoSuchFileException e) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.FILE_NOT_FOUND,
                    "file does not exists");
        } catch (RuntimeException e) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.WRONG_DELIMITER,
                    "wrong delimiter has been added");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CSVBuilderException e) {
            throw new StateCensusAnalyserException(e.exceptionType.name(), e.getMessage());
        }
        return censusMap;
    }
}
