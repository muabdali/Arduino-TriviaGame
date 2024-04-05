import org.firmata4j.I2CDevice;
import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.ssd1306.SSD1306;
import org.firmata4j.Pin;
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Random;

// where the magic happens, all processes begin and end here.
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Main main = new Main();
        main.start();
    }

    public void start() throws IOException, InterruptedException {
        // declaring scores, useful when game ends, and updated after every correct answer.
        // saved to JSON only once, when user selects wrong answer.
        long highScore = readHighScoreFromJSON();
        long currentScore = 0;
        System.out.println(highScore);

        // mastersheet for questions, answers and the index for the correct answer. standard json file.
        String questionsFilePath = "src/mastersheet.json";

        // grove board setup.
        var myGroveBoard = "/dev/cu.usbserial-0001";
        var device = new FirmataDevice(myGroveBoard);
        device.start();
        device.ensureInitializationIsDone();

        // OLED display setup.
        I2CDevice i2cObject = device.getI2CDevice((byte) 0x3C);
        SSD1306 display = new SSD1306(i2cObject, SSD1306.Size.SSD1306_128_64);
        display.init();

        // Declare Pins
        var myLED = device.getPin(4);
        myLED.setMode(Pin.Mode.OUTPUT);
        var buttonPin = device.getPin(6);
        buttonPin.setMode(Pin.Mode.INPUT);
        var potPin = device.getPin(14);
        var buzzerPin = device.getPin(5);
        buzzerPin.setMode(Pin.Mode.OUTPUT);

        // Instantiate MainMenu with display and potPin
        MainMenu mainMenu = new MainMenu(display, potPin, (int) highScore);

        // Display the menu initially
        mainMenu.displayMenu();

        // listen for button press to start game.
        while (true) {
            try {
                int buttonState = (int) buttonPin.getValue();
                if (buttonState == 1) {
                    display.clear();
                    display.display();
                    break;
                }
                // delay for a short period to avoid high CPU usage
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // rules page, poorly done but only needs to be shown once so not very time efficient to "fix"
            display.clear();
            display.getCanvas().drawString(0, 0, "RULES: \n 1. You get 15 seconds for each question. " +
                    "2. To turn off the game, plug out the USB. " +
                    "3. To answer a question, turn the potentiometer \n towards the answer, and click the button.");
            display.display();
            Thread.sleep(5000);
            display.clear();

            loadQuestions.Question[] questions = loadQuestions.loadQuestions(questionsFilePath);
            String question;
            String[] ansa;
            int ansaIndex;


        Random random = new Random();

        // main question and answer selection process.
        while(true){
            display.clear();

            // selecting question, assigning answers and answerindex to variables. if confused, look at JSON file's format.
            int randomNumber = random.nextInt(questions.length);
            System.out.println("Selected Question " + randomNumber);
            question = questions[randomNumber].question;
            ansa = questions[randomNumber].answers;
            ansaIndex = questions[randomNumber].correctAnswerIndex;
            displayQuestionsLoop displayQuestion = new displayQuestionsLoop(buttonPin, myLED, display, potPin, question ,ansa ,ansaIndex);

            // check if user's answer and the real answer match up.
            int selectedAnswerIndex = displayQuestion.getAnswerSelectedIndex();
            afterAnswerDisplay afterAnswer = new afterAnswerDisplay(ansaIndex, selectedAnswerIndex, display, myLED);
            boolean answerState = afterAnswer.afterAnswerDisplayReturn(ansaIndex, selectedAnswerIndex, display, myLED, (int) currentScore, (int) highScore);
            // answer was true, add 10 to the current score and repeat while loop.
            if (answerState){
                currentScore = currentScore + 10;

            }else{
                // answer was false, exiting program.
                System.exit(0);
            }


        }



    }
    private long readHighScoreFromJSON() {
        try (FileReader reader = new FileReader("src/details.json")) {
            // Create Gson instance
            Gson gson = new Gson();

            // Deserialize JSON into HighScoreData object
            HighScoreData highScoreData = gson.fromJson(reader, HighScoreData.class);

            // Return the high score value
            return highScoreData != null ? highScoreData.getHighScore() : 0;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

    }

}
