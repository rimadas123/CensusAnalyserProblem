import com.census_analyser.*;
import com.google.gson.Gson;
import org.ietf.jgss.GSSContext;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class StateCensusAnalyserTest {

    private static final String STATE_CENSUS_CSV_PATH = "./src/test/resources/StateCensusData.csv";
    private static final String WRONG_DELIMITER_STATE_CENSUS_CSV_PATH = "./src/test/resources/WrongDelimiterStateCensusData.csv";
    private static final String STATE_CODE_CSV_PATH = "./src/test/resources/StateCode.csv";
    private static final String WRONG_DELIMITER_STATE_CODE_CSV_PATH = "./src/test/resources/WrongDelimiterStateCode.csv";

    //Object creation
    StateCensusAnalyser analyser = new StateCensusAnalyser();
    CSVStates csvStates = new CSVStates();

    @Test
    public void givenTheStatesCensusCSVFiles_CheckToEnsureTheNumber_OfRecordMatches() throws StateCensusAnalyserException, IOException {
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
    public void givenTheStateCensus_CSVFileWhenCorrect_ButTypeIncorrect_ReturnsACustomException() throws IOException {
        try {
            int fileType = analyser.readCSVFile("./src/test/resources/StateCensusData.pdf");
            Assert.assertEquals(29, fileType);
        } catch (StateCensusAnalyserException e){
            Assert.assertEquals(StateCensusAnalyserException.ExceptionType.NO_SUCH_FILE,e.exceptiontype);
        }
    }

    @Test
    public void givenTheStateCensus_CSVFile_WhenCorrectButDelimiterIncorrect_ReturnsACustomException() throws IOException {
        try{
            int wrongDelimiter = analyser.readCSVFile(WRONG_DELIMITER_STATE_CENSUS_CSV_PATH);
            Assert.assertEquals(29,wrongDelimiter);
        } catch (StateCensusAnalyserException e){
            Assert.assertEquals(StateCensusAnalyserException.ExceptionType.WRONG_DELIMITER,e.exceptiontype);
        }
    }

    @Test
    public void givenTheStateCensus_CSVFile_WhenCorrectButCSVHeaderIncorrect_ReturnsACustomException() throws IOException {
        try{
            int wrongHeader = analyser.readCSVFile(WRONG_DELIMITER_STATE_CENSUS_CSV_PATH);
            Assert.assertEquals(29,wrongHeader);
        } catch (StateCensusAnalyserException e) {
            Assert.assertEquals(StateCensusAnalyserException.ExceptionType.WRONG_DELIMITER,e.exceptiontype);
        }
    }

    @Test
    public void givenStatesCode_CheckToEnsureTheNumber_OfRecordMatches() throws StateCensusAnalyserException {
        int numberOfRecords = analyser.readStateCodeFile(STATE_CODE_CSV_PATH);
        Assert.assertEquals(37,numberOfRecords);
    }

    @Test
    public void givenStatesCode_IfIncorrectReturnsACustomException() {
        try {
            int fileNotFound = analyser.readStateCodeFile("./src/test/resources/State.csv");
            Assert.assertEquals(37,fileNotFound);
        } catch (StateCensusAnalyserException e){
            Assert.assertEquals(StateCensusAnalyserException.ExceptionType.FILE_NOT_FOUND,e.exceptiontype);
        }
    }

    @Test
    public void givenStateCode_WhenCorrect_ButTypeIncorrect_ReturnsACustomException() {
        try{
            int noSuchFile = analyser.readStateCodeFile("./src/test/resources/StateCode.xls");
            Assert.assertEquals(37,noSuchFile);
        } catch (StateCensusAnalyserException e){
            Assert.assertEquals(StateCensusAnalyserException.ExceptionType.NO_SUCH_FILE,e.exceptiontype);
        }
    }

    @Test
    public void givenStateCode_WhenCorrectButDelimiterIncorrect_ReturnsACustomException() {
        try{
            int wrongDelimiter = analyser.readStateCodeFile(WRONG_DELIMITER_STATE_CODE_CSV_PATH);
            Assert.assertEquals(37,wrongDelimiter);
        } catch (StateCensusAnalyserException e){
            Assert.assertEquals(StateCensusAnalyserException.ExceptionType.WRONG_DELIMITER,e.exceptiontype);
        }
    }

    @Test
    public void givenStateCode_WhenCorrectButCSVHeaderIncorrect_ReturnsACustomException() {
        try{
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
        analyser.readStateCodeFile(STATE_CODE_CSV_PATH);
        String sortedCensusData = analyser.getStateCodeWiseSortedCensusData();
        StateCodeData[] stateCodeData = new Gson().fromJson(sortedCensusData,StateCodeData[].class);
        Assert.assertEquals("AD",stateCodeData[0].StateCode);
    }
}
