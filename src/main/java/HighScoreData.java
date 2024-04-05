import com.google.gson.annotations.SerializedName;

public class HighScoreData {
    @SerializedName("score")
    private long highScore;

    public long getHighScore() {
        return highScore;
    }
}