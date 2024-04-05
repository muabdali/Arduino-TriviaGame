import org.firmata4j.Pin;
import org.firmata4j.ssd1306.SSD1306;

import java.util.Arrays;

public class displayQuestionsLoop {
    private int answerSelectedIndex = -1; // Default value to indicate no answer selected

    public displayQuestionsLoop(Pin buttonPin, Pin ledPin, SSD1306 display, Pin potentPin, String question, String[] answer, int answerIndex) {
        String answerSelected = null;
        display.clear();
        display.getCanvas().drawString(0, 0, question);
        display.getCanvas().drawString(0, 30, Arrays.toString(answer));
        display.display();
        while (true) {
            try {
                if (buttonPin.getValue() == 1) {
                    if (potentPin.getValue() < 341) {
                        System.out.println(potentPin.getValue());
                        answerSelected = answer[2];
                        answerSelectedIndex = 2;
                        System.out.println("Selected answer: " + answerSelected);
                        break;
                    } else if (potentPin.getValue() > 341 && potentPin.getValue() < 682) {
                        answerSelected = answer[1];
                        answerSelectedIndex = 1;
                        System.out.println("Selected answer: " + answerSelected);
                        break;
                    } else {
                        answerSelected = answer[0];
                        answerSelectedIndex = 0;
                        System.out.println("Selected answer: " + answerSelected);
                        break;
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int getAnswerSelectedIndex() {
        return answerSelectedIndex;
    }
}
