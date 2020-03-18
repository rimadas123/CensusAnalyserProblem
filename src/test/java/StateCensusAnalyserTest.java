import com.census_analyser.StateCensusAnalyser;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class StateCensusAnalyserTest {

    //Object creation
    StateCensusAnalyser analyser=new StateCensusAnalyser();

    @Test
    public void givenTheStatesCensusCSVFiles_CheckToEnsureTheNumber_OfRecordMatches() throws IOException {
        int count = analyser.readCSVFile("./src/test/resources/StateCensusData.csv");
        Assert.assertEquals(29,count);
    }
    
}
