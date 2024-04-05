import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;

// Loop responsible for loading questions.
public class loadQuestions {

    public loadQuestions(String questionsFilePath) {
    }

    public static class Question {
        String question;
        String[] answers;
        int correctAnswerIndex;
    }

    public static Question[] loadQuestions(String filePath) throws IOException {
        Gson gson = new Gson();
        FileReader reader = new FileReader(filePath);
        Question[] questions = gson.fromJson(reader, Question[].class);
        reader.close();
        return questions;
    }


}
