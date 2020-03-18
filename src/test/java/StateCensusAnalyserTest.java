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
    public void givenTheStateCensus_CSVFile_IfIncorrectReturnsACustomException() throws IOException, StateCensusAnalyserException {
        try {
            int fileName = analyser.readCSVFile("./src/test/resources/StateCensus.csv");
            Assert.assertEquals("29", fileName);
        } catch (StateCensusAnalyserException e){
            Assert.assertEquals(StateCensusAnalyserException.ExceptionType.FILE_NOT_FOUND,e.exceptiontype);
        }
    }
    
}
