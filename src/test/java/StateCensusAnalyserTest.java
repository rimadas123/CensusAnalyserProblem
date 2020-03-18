import com.census_analyser.StateCensusAnalyser;
import com.census_analyser.StateCensusAnalyserException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.NoSuchFileException;

public class StateCensusAnalyserTest {

    //Object creation
    StateCensusAnalyser analyser=new StateCensusAnalyser();

    @Test
    public void givenTheStatesCensusCSVFiles_CheckToEnsureTheNumber_OfRecordMatches() throws IOException, StateCensusAnalyserException {
        int count = analyser.readCSVFile("./src/test/resources/StateCensusData.csv");
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
    public void givenTheStateCensus_CSVFile_WhenCorrectButDelimiterIncorrect_ReturnsACustomException() {
        try{
            int wrongDelimiter = analyser.readCSVFile("./src/test/resources/WrongDelimiterStateCensusData.csv");
            Assert.assertEquals(29,wrongDelimiter);
        } catch (StateCensusAnalyserException e){
            Assert.assertEquals(StateCensusAnalyserException.ExceptionType.WRONG_DELIMITER,e.exceptiontype);
        }
    }

    @Test
    public void givenTheStateCensus_CSVFile_WhenCorrectButCSVHeaderIncorrect_ReturnsACustomException() {
        try{
            int wrongHeader = analyser.readCSVFile("./src/test/resources/WrongDelimiterStateCensusData.csv");
            Assert.assertEquals(29,wrongHeader);
        } catch (StateCensusAnalyserException e) {
            Assert.assertEquals(StateCensusAnalyserException.ExceptionType.WRONG_DELIMITER,e.exceptiontype);
        }
    }
}
