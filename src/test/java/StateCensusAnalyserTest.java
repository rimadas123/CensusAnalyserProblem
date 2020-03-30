import com.census_analyser.*;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class StateCensusAnalyserTest {

    private static final String STATE_CENSUS_CSV_PATH = "./src/test/resources/StateCensusData.csv";
    private static final String WRONG_DELIMITER_STATE_CENSUS_CSV_PATH = "./src/test/resources/WrongDelimiterStateCensusData.csv";
    private static final String STATE_CODE_CSV_PATH = "./src/test/resources/StateCode.csv";
    private static final String WRONG_DELIMITER_STATE_CODE_CSV_PATH = "./src/test/resources/WrongDelimiterStateCode.csv";
    private static final String US_CENSUS_CSV_PATH = "./src/test/resources/USCensusData.csv";

    //Object creation
    StateCensusAnalyser analyser = new StateCensusAnalyser();
    CSVStates csvStates = new CSVStates();

    @Test
    public void givenTheStatesCensusCSVFiles_CheckToEnsureTheNumber_OfRecordMatches() throws StateCensusAnalyserException{
        int count = analyser.readCSVFile(STATE_CENSUS_CSV_PATH);
        Assert.assertEquals(29,count);
    }

    @Test
    public void givenTheStateCensus_CSVFile_IfIncorrectReturnsACustomException() throws IOException {
        try {
            int fileName = analyser.readCSVFile("./src/test/resources/StateCensus.csv");
            Assert.assertEquals(29, fileName);
        } catch (StateCensusAnalyserException e){
            Assert.assertEquals(StateCensusAnalyserException.ExceptionType.FILE_NOT_FOUND,e.exceptiontype);
        }
    }

    @Test
    public void givenTheStateCensus_CSVFileWhenCorrect_ButTypeIncorrect_ReturnsACustomException() {
        try {
            int fileType = analyser.readCSVFile("./src/test/resources/StateCensusData.pdf");
            Assert.assertEquals(29, fileType);
        } catch (StateCensusAnalyserException e){
            Assert.assertEquals(StateCensusAnalyserException.ExceptionType.NO_SUCH_FILE,e.exceptiontype);
        }
    }

    @Test
    public void givenTheStateCensus_CSVFile_WhenCorrectButDelimiterIncorrect_ReturnsACustomException() {
        try{
            int wrongDelimiter = analyser.readCSVFile(WRONG_DELIMITER_STATE_CENSUS_CSV_PATH);
            Assert.assertEquals(29,wrongDelimiter);
        } catch (StateCensusAnalyserException e){
            Assert.assertEquals(StateCensusAnalyserException.ExceptionType.WRONG_DELIMITER,e.exceptiontype);
        }
    }

    @Test
    public void givenTheStateCensus_CSVFile_WhenCorrectButCSVHeaderIncorrect_ReturnsACustomException() {
        try{
            int wrongHeader = analyser.readCSVFile(WRONG_DELIMITER_STATE_CENSUS_CSV_PATH);
            Assert.assertEquals(29,wrongHeader);
        } catch (StateCensusAnalyserException e) {
            Assert.assertEquals(StateCensusAnalyserException.ExceptionType.WRONG_DELIMITER,e.exceptiontype);
        }
    }

    @Test
    public void givenStatesCode_CheckToEnsureTheNumber_OfRecordMatches() throws StateCensusAnalyserException {
        analyser.readCSVFile(STATE_CENSUS_CSV_PATH);
        int numberOfRecords = analyser.readStateCodeFile(STATE_CODE_CSV_PATH);
        Assert.assertEquals(29,numberOfRecords);
    }

    @Test
    public void givenStatesCode_IfIncorrectReturnsACustomException() {
        try {
            analyser.readCSVFile(STATE_CENSUS_CSV_PATH);
            int fileNotFound = analyser.readStateCodeFile("./src/test/resources/State.csv");
            Assert.assertEquals(37,fileNotFound);
        } catch (StateCensusAnalyserException e){
            Assert.assertEquals(StateCensusAnalyserException.ExceptionType.FILE_NOT_FOUND,e.exceptiontype);
        }
    }

    @Test
    public void givenStateCode_WhenCorrect_ButTypeIncorrect_ReturnsACustomException() {
        try{
            analyser.readCSVFile(STATE_CENSUS_CSV_PATH);
            int noSuchFile = analyser.readStateCodeFile("./src/test/resources/StateCode.xls");
            Assert.assertEquals(37,noSuchFile);
        } catch (StateCensusAnalyserException e){
            Assert.assertEquals(StateCensusAnalyserException.ExceptionType.NO_SUCH_FILE,e.exceptiontype);
        }
    }

    @Test
    public void givenStateCode_WhenCorrectButDelimiterIncorrect_ReturnsACustomException() {
        try{
            analyser.readCSVFile(STATE_CENSUS_CSV_PATH);
            int wrongDelimiter = analyser.readStateCodeFile(WRONG_DELIMITER_STATE_CODE_CSV_PATH);
            Assert.assertEquals(37,wrongDelimiter);
        } catch (StateCensusAnalyserException e){
            Assert.assertEquals(StateCensusAnalyserException.ExceptionType.WRONG_DELIMITER,e.exceptiontype);
        }
    }

    @Test
    public void givenStateCode_WhenCorrectButCSVHeaderIncorrect_ReturnsACustomException() {
        try{
            analyser.readCSVFile(STATE_CENSUS_CSV_PATH);
            int wrongHeader = analyser.readStateCodeFile(WRONG_DELIMITER_STATE_CODE_CSV_PATH);
            Assert.assertEquals(37,wrongHeader);
        } catch (StateCensusAnalyserException e){
            Assert.assertEquals(StateCensusAnalyserException.ExceptionType.WRONG_DELIMITER,e.exceptiontype);
        }
    }

    @Test
    public void givenStateCensusData_WhenSortedOnState_ShouldReturnSortedResult() throws StateCensusAnalyserException {
        analyser.readCSVFile(STATE_CENSUS_CSV_PATH);
        String sortedCensusData =analyser.getStateWiseSortedCensusData();
        StateCensusData[] stateCensusData = new Gson().fromJson(sortedCensusData, StateCensusData[].class);
        Assert.assertEquals("Andhra Pradesh",stateCensusData[0].state);
    }

    @Test
    public void givenStateCodeData_WhenSortedOnState_ShouldReturnSortedResult() throws StateCensusAnalyserException {
        analyser.readCSVFile(STATE_CENSUS_CSV_PATH);
        analyser.readStateCodeFile(STATE_CODE_CSV_PATH);
        String sortedCensusData = analyser.getStateCodeWiseSortedCensusData();
        CensusDAO[] stateCodeData = new Gson().fromJson(sortedCensusData,CensusDAO[].class);
        Assert.assertEquals("AD",stateCodeData[0].stateCode);
    }

    @Test
    public void givenStateCodeData_WhenSortedOnMostPopulatedState_ShouldReturnSortedResult() throws StateCensusAnalyserException {
        analyser.readCSVFile(STATE_CENSUS_CSV_PATH);
        String sortedStateData = analyser.getStatePopulatedWiseSortedCensusData();
        CensusDAO[] stateCensusData = new Gson().fromJson(sortedStateData,CensusDAO[].class);
        Assert.assertEquals(199812341,stateCensusData[0].population);
    }

    @Test
    public void givenStateCensusData_WhenSortedOnMostPopulateDensityState_ShouldReturnSortedResult() throws StateCensusAnalyserException {
        analyser.readCSVFile(STATE_CENSUS_CSV_PATH);
        String sortedStateData = analyser.getStatePopulationDensityWiseSortedCensusData();
        CensusDAO[] stateCensusDAOS = new Gson().fromJson(sortedStateData,CensusDAO[].class);
        Assert.assertEquals(1102,stateCensusDAOS[0].densityPerSqKm);
    }

    @Test
    public void givenStateCensusData_WhenSortedOnLargestArea_ShouldReturnSortedResult() throws StateCensusAnalyserException{
        analyser.readCSVFile(STATE_CENSUS_CSV_PATH);
        String sortedStateData = analyser.getStateLargestAreaWiseSortedCensusData();
        CensusDAO[] stateCensusDAOS = new Gson().fromJson(sortedStateData,CensusDAO[].class);
        Assert.assertEquals(342239,stateCensusDAOS[0].areaInSqKm);
    }

    @Test
    public void givenUSCensusData_CheckToEnsure_NumberOfRecords() throws StateCensusAnalyserException {
        int numberOfRecords= analyser.loadUSCensusData(US_CENSUS_CSV_PATH);
        Assert.assertEquals(51,numberOfRecords);
    }

    @Test
    public void givenUSCensusData_WhenSortedOnMostPopulousState_ShouldReturnSortedResult() throws StateCensusAnalyserException {
        analyser.loadUSCensusData(US_CENSUS_CSV_PATH);
        String sortedUSStateData = analyser.getStatePopulatedWiseSortedCensusData();
        CensusDAO[] censusDAOS = new Gson().fromJson(sortedUSStateData,CensusDAO[].class);
        Assert.assertEquals(37253956,censusDAOS[0].population);
    }
}
