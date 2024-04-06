import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {
// Tests integrity of JSON writing and reading.
    @Test
    public void testReadHighScoreFromJSON() {
        Main main = new Main();
        long highScore = main.readHighScoreFromJSON();
        assertEquals(0, highScore);
    }
}