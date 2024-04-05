import org.firmata4j.Pin;
import org.firmata4j.ssd1306.SSD1306;

import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.IOException;
public class afterAnswerDisplay {

    public afterAnswerDisplay(int ansaIndex, int selectedAnswerIndex, SSD1306 display, Pin myLED) {
    }


    public boolean afterAnswerDisplayReturn(int correctAnswerIndex, int selectedAnswerIndex, SSD1306 display, Pin myLED, int currentScore) throws InterruptedException, IOException {
        if (correctAnswerIndex == selectedAnswerIndex){
            currentScore = currentScore + 10;
            display.clear();
            display.getCanvas().drawString(0,0,String.valueOf(selectedAnswerIndex) + ": Correct Answer!");
            display.getCanvas().drawString(0,30,"Current Score: " + currentScore);
            display.display();
            Thread.sleep(2500);
            return true;
        }
        else{
            display.clear();
            display.getCanvas().drawString(0,0, "Incorrect, Game Over! \n Final Score: " + currentScore);
            display.display();
            myLED.setValue(1);
            writeScoreToFile(currentScore);
            Thread.sleep(5000);
            myLED.setValue(0);
            display.clear();

            return false;
        }
    }
    private void writeScoreToFile(int score) {
        try (FileWriter writer = new FileWriter("src/details.json")) {
            Gson gson = new Gson();
            ScoreData scoreData = new ScoreData(score);
            gson.toJson(scoreData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


