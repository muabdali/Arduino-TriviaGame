import org.firmata4j.Pin;
import org.firmata4j.ssd1306.*;


public class MainMenu {
    private static SSD1306 display;
    private static int highScore = 0;
    private Pin potentiometer;
    private int potMidpoint = 375;
    private static String welcome = "Welcome to TriviaLite";

    public MainMenu(SSD1306 display, Pin potentiometer, int highScore) {
        MainMenu.display = display;
        this.potentiometer = potentiometer;
        this.highScore = highScore;
    }

        public static void displayMenu() {
            display.getCanvas().clear();
            display.getCanvas().setTextsize(1);
            display.getCanvas().drawString(0 ,0, welcome);

            display.getCanvas().drawString(20, 50, "PRESS TO START");
            display.getCanvas().drawString(10, 25, "HIGH SCORE: " + String.valueOf(highScore));
            System.out.println(highScore);

            display.display();

        }

    }
