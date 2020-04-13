import com.census_analyser.*;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;

public class StateCensusAnalyserTest {

    private static final String STATE_CENSUS_CSV_PATH = "./src/test/resources/StateCensusData.csv";
    private static final String WRONG_DELIMITER_STATE_CENSUS_CSV_PATH = "./src/test/resources/WrongDelimiterStateCensusData.csv";
    private static final String STATE_CODE_CSV_PATH = "./src/test/resources/StateCode.csv";
    private static final String WRONG_DELIMITER_STATE_CODE_CSV_PATH = "./src/test/resources/WrongDelimiterStateCode.csv";
    private static final String US_CENSUS_CSV_PATH = "./src/test/resources/USCensusData.csv";


    @Test
    public void givenTheStatesCensusCSVFiles_CheckToEnsureTheNumber_OfRecordMatches() throws StateCensusAnalyserException{
        StateCensusAnalyser analyser = new StateCensusAnalyser(StateCensusAnalyser.COUNTRY.INDIA);
        int count = analyser.loadCensusData(STATE_CENSUS_CSV_PATH);
        Assert.assertEquals(29,count);
    }

    @Test
    public void givenTheStateCensus_CSVFile_IfIncorrectReturnsACustomException() {
        try {
            StateCensusAnalyser analyser = new StateCensusAnalyser(StateCensusAnalyser.COUNTRY.INDIA);
            int fileName = analyser.loadCensusData("./src/test/resources/StateCensus.csv");
            Assert.assertEquals(29, fileName);
        } catch (StateCensusAnalyserException e){
            Assert.assertEquals(StateCensusAnalyserException.ExceptionType.FILE_NOT_FOUND,e.exceptiontype);
        }
    }

    @Test
    public void givenTheStateCensus_CSVFileWhenCorrect_ButTypeIncorrect_ReturnsACustomException() {
        try {
            StateCensusAnalyser analyser = new StateCensusAnalyser(StateCensusAnalyser.COUNTRY.INDIA);
            int fileType = analyser.loadCensusData("./src/test/resources/StateCensusData.pdf");
            Assert.assertEquals(29, fileType);
        } catch (StateCensusAnalyserException e){
            Assert.assertEquals(StateCensusAnalyserException.ExceptionType.NO_SUCH_FILE,e.exceptiontype);
        }
    }

    @Test
    public void givenTheStateCensus_CSVFile_WhenCorrectButDelimiterIncorrect_ReturnsACustomException() {
        try{
            StateCensusAnalyser analyser = new StateCensusAnalyser(StateCensusAnalyser.COUNTRY.INDIA);
            int wrongDelimiter = analyser.loadCensusData(WRONG_DELIMITER_STATE_CENSUS_CSV_PATH);
            Assert.assertEquals(29,wrongDelimiter);
        } catch (StateCensusAnalyserException e){
            Assert.assertEquals(StateCensusAnalyserException.ExceptionType.WRONG_DELIMITER,e.exceptiontype);
        }
    }

    @Test
    public void givenTheStateCensus_CSVFile_WhenCorrectButCSVHeaderIncorrect_ReturnsACustomException() {
        try{
            StateCensusAnalyser analyser = new StateCensusAnalyser(StateCensusAnalyser.COUNTRY.INDIA);
            int wrongHeader = analyser.loadCensusData(WRONG_DELIMITER_STATE_CENSUS_CSV_PATH);
            Assert.assertEquals(29,wrongHeader);
        } catch (StateCensusAnalyserException e) {
            Assert.assertEquals(StateCensusAnalyserException.ExceptionType.WRONG_DELIMITER,e.exceptiontype);
        }
    }

    @Test
    public void givenStatesCode_CheckToEnsureTheNumber_OfRecordMatches() throws StateCensusAnalyserException {
        StateCensusAnalyser analyser = new StateCensusAnalyser(StateCensusAnalyser.COUNTRY.INDIA);
        int numberOfRecords = analyser.loadCensusData(STATE_CENSUS_CSV_PATH,STATE_CODE_CSV_PATH);
        Assert.assertEquals(29,numberOfRecords);
    }

    @Test
    public void givenStatesCode_IfIncorrectReturnsACustomException() {
        try {
            StateCensusAnalyser analyser = new StateCensusAnalyser(StateCensusAnalyser.COUNTRY.INDIA);
            int fileNotFound = analyser.loadCensusData("./src/test/resources/State.csv");
            Assert.assertEquals(37,fileNotFound);
        } catch (StateCensusAnalyserException e){
            Assert.assertEquals(StateCensusAnalyserException.ExceptionType.FILE_NOT_FOUND,e.exceptiontype);
        }
    }

    @Test
    public void givenStateCode_WhenCorrect_ButTypeIncorrect_ReturnsACustomException() {
        try{
            StateCensusAnalyser analyser = new StateCensusAnalyser(StateCensusAnalyser.COUNTRY.INDIA);
            int noSuchFile = analyser.loadCensusData("./src/test/resources/StateCode.xls");
            Assert.assertEquals(37,noSuchFile);
        } catch (StateCensusAnalyserException e){
            Assert.assertEquals(StateCensusAnalyserException.ExceptionType.NO_SUCH_FILE,e.exceptiontype);
        }
    }

    @Test
    public void givenStateCode_WhenCorrectButDelimiterIncorrect_ReturnsACustomException() {
        try{
            StateCensusAnalyser analyser = new StateCensusAnalyser(StateCensusAnalyser.COUNTRY.INDIA);
            int wrongDelimiter = analyser.loadCensusData(WRONG_DELIMITER_STATE_CODE_CSV_PATH);
            Assert.assertEquals(37,wrongDelimiter);
        } catch (StateCensusAnalyserException e){
            Assert.assertEquals(StateCensusAnalyserException.ExceptionType.WRONG_DELIMITER,e.exceptiontype);
        }
    }

    @Test
    public void givenStateCode_WhenCorrectButCSVHeaderIncorrect_ReturnsACustomException() {
        try{
            StateCensusAnalyser analyser = new StateCensusAnalyser(StateCensusAnalyser.COUNTRY.INDIA);
            int wrongHeader = analyser.loadCensusData(WRONG_DELIMITER_STATE_CODE_CSV_PATH);
            Assert.assertEquals(37,wrongHeader);
        } catch (StateCensusAnalyserException e){
            Assert.assertEquals(StateCensusAnalyserException.ExceptionType.WRONG_DELIMITER,e.exceptiontype);
        }
    }

    @Test
    public void givenStateCensusData_WhenSortedOnState_ShouldReturnSortedResult() throws StateCensusAnalyserException {
        StateCensusAnalyser analyser = new StateCensusAnalyser(StateCensusAnalyser.COUNTRY.INDIA);
        analyser.loadCensusData(STATE_CENSUS_CSV_PATH);
        String sortedCensusData = analyser.getSortCensusData(StateCensusAnalyser.SORTING_MODE.STATE);
        StateCensusData[] stateCensusData = new Gson().fromJson(sortedCensusData, StateCensusData[].class);
        Assert.assertEquals("Andhra Pradesh",stateCensusData[0].state);
    }

    @Test
    public void givenStateCodeData_WhenSortedOnState_ShouldReturnSortedResult() throws StateCensusAnalyserException {
        StateCensusAnalyser analyser = new StateCensusAnalyser(StateCensusAnalyser.COUNTRY.INDIA);
        analyser.loadCensusData(STATE_CENSUS_CSV_PATH,STATE_CODE_CSV_PATH);
        String sortedCensusData = analyser.getSortCensusData(StateCensusAnalyser.SORTING_MODE.STATECODE);
        StateCodeData[] stateCodeData = new Gson().fromJson(sortedCensusData,StateCodeData[].class);
        Assert.assertEquals("AD",stateCodeData[0].getStateCode());
    }

    @Test
    public void givenStateCodeData_WhenSortedOnMostPopulatedState_ShouldReturnSortedResult() throws StateCensusAnalyserException {
        StateCensusAnalyser analyser = new StateCensusAnalyser(StateCensusAnalyser.COUNTRY.INDIA);
        analyser.loadCensusData(STATE_CENSUS_CSV_PATH);
        String sortedStateData = analyser.getSortCensusData(StateCensusAnalyser.SORTING_MODE.POPULATION);
        CensusDAO[] stateCensusData = new Gson().fromJson(sortedStateData,CensusDAO[].class);
        Assert.assertEquals(607688,stateCensusData[0].population);
    }

    @Test
    public void givenStateCensusData_WhenSortedOnMostPopulateDensityState_ShouldReturnSortedResult() throws StateCensusAnalyserException {
        StateCensusAnalyser analyser = new StateCensusAnalyser(StateCensusAnalyser.COUNTRY.INDIA);
        analyser.loadCensusData(STATE_CENSUS_CSV_PATH);
        String sortedStateData = analyser.getSortCensusData(StateCensusAnalyser.SORTING_MODE.DENSITY);
        CensusDAO[] stateCensusDAOS = new Gson().fromJson(sortedStateData,CensusDAO[].class);
        Assert.assertEquals(52,stateCensusDAOS[0].densityPerSqKm);
    }

    @Test
    public void givenStateCensusData_WhenSortedOnLargestArea_ShouldReturnSortedResult() throws StateCensusAnalyserException{
        StateCensusAnalyser analyser = new StateCensusAnalyser(StateCensusAnalyser.COUNTRY.INDIA);
        analyser.loadCensusData(STATE_CENSUS_CSV_PATH);
        String sortedStateData = analyser.getSortCensusData(StateCensusAnalyser.SORTING_MODE.AREA);
        CensusDAO[] stateCensusDAOS = new Gson().fromJson(sortedStateData,CensusDAO[].class);
        Assert.assertEquals(3702,stateCensusDAOS[0].areaInSqKm);
    }

    @Test
    public void givenUSCensusData_CheckToEnsure_NumberOfRecords() throws StateCensusAnalyserException {
        StateCensusAnalyser analyser = new StateCensusAnalyser(StateCensusAnalyser.COUNTRY.US);
        int numberOfRecords = analyser.loadCensusData(US_CENSUS_CSV_PATH);
        Assert.assertEquals(51,numberOfRecords);
    }

    @Test
    public void givenUSCensusData_WhenSortedOnMostPopulousState_ShouldReturnSortedResult() throws StateCensusAnalyserException {
        StateCensusAnalyser analyser = new StateCensusAnalyser(StateCensusAnalyser.COUNTRY.US);
        analyser.loadCensusData(US_CENSUS_CSV_PATH);
        String sortedUSStateData = analyser.getSortCensusData(StateCensusAnalyser.SORTING_MODE.POPULATION);
        CensusDAO[] censusDAOS = new Gson().fromJson(sortedUSStateData,CensusDAO[].class);
        Assert.assertEquals("California",censusDAOS[0].state);
    }

    @Test
    public void givenUSAndIndiaStateCensusData_WhenSortedPopulationAndDensity_ShouldReturnSortedList_2() throws StateCensusAnalyserException {
        try {
            StateCensusAnalyser usCensusAnalyserProblem = new StateCensusAnalyser(StateCensusAnalyser.COUNTRY.US);
            usCensusAnalyserProblem.loadCensusData(US_CENSUS_CSV_PATH);
            String sortedUSCensusData = usCensusAnalyserProblem.getSortCensusData(StateCensusAnalyser.SORTING_MODE.POPULATION);
            CensusDAO[] USCensusCSV = new Gson().fromJson(sortedUSCensusData, CensusDAO[].class);
            StateCensusAnalyser censusAnalyserProblem = new StateCensusAnalyser(StateCensusAnalyser.COUNTRY.INDIA);
            censusAnalyserProblem.loadCensusData(STATE_CENSUS_CSV_PATH);
            String sortedCensusData = censusAnalyserProblem.getSortCensusData(StateCensusAnalyser.SORTING_MODE.POPULATION);
            CensusDAO[] censusCSV = new Gson().fromJson(sortedCensusData, CensusDAO[].class);
            Assert.assertEquals(true, (String.valueOf(censusCSV[0].densityPerSqKm)).compareToIgnoreCase(String.valueOf(USCensusCSV[0].densityPerSqKm)) > 0);
        } catch (StateCensusAnalyserException e) {
            e.printStackTrace();
        }
    }
}
