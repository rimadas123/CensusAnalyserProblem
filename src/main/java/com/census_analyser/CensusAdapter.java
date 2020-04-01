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

public abstract class CensusAdapter {

    public abstract Map<String, CensusDAO> loadCensusData(String... csv_path) throws StateCensusAnalyserException;

    private <E> Map<String, CensusDAO> loadCensusData(Class<E> censusCSVClass, String... csvFilePath) throws StateCensusAnalyserException{
        Map<String, CensusDAO> censusDAOMap = new HashMap<>();
        String extension=getFileExtension(csvFilePath[0]);
        if(!Pattern.matches(PATTERN_FOR_CSV,extension))
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_SUCH_FILE,"No such file");
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath[0]))
        ) {
            ICSVBuilder icsvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<E> csvFileIterator = icsvBuilder.getCsvFileIterator(reader, censusCSVClass);
            Iterable<E> csvFileIterable = () -> csvFileIterator;
            if(censusCSVClass.getName().contains("StateCensusData")) {
                StreamSupport.stream(csvFileIterable.spliterator(), false)
                        .map(StateCensusData.class::cast)
                        .forEach(censusCSV -> censusDAOMap.put(censusCSV.state, new CensusDAO(censusCSV)));
            }
            else if(censusCSVClass.getName().contains("USCensusData")) {
                StreamSupport.stream(csvFileIterable.spliterator(), false)
                        .map(USCensusData.class::cast)
                        .forEach(censusCSV -> censusDAOMap.put(censusCSV.getState(), new CensusDAO(censusCSV)));
            }
            return censusDAOMap;
        } catch (NoSuchFileException e) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.FILE_NOT_FOUND,
                    "file does not exists");
        } catch (RuntimeException e) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.WRONG_DELIMITER,
                    "wrong delimiter added");
        } catch (IOException e) {
            throw new StateCensusAnalyserException(StateCensusAnalyserException.ExceptionType.NO_DATA,"no data");
        } catch (CSVBuilderException e) {
            e.printStackTrace();
        }
        return censusDAOMap;
    }
}
