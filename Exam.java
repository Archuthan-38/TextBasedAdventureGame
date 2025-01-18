/**
 * Class Exam - used to generate results information.
 * Tells player if they have won the game, or not
 *
 * @author Archuthan Mohanathasan
 * @version 2016.02.29
 */
public class Exam
{
    /**
     * Constructor for objects of class Exam
     */
    public Exam()
    {
        // nothing to do at the moment...
    }

    /**
     * Prints short description of what each score means.
     * @param score Player's test results.
     */
    public void calcGrade(int score)
    {
        String results = "";
        
        
        if (score >= 75) {
            results = "Very good! You have passed... **CONGRATULATIONS YOU HAVE WON THE GAME**  ";
        }
        else if ((score >= 40) && (score <= 74 )) {
            results = "Good effort, but not enough to win the game...";
        }
        else if (score <= 39 && score > 0) {
            results = "Poor mark... do better next time.";
        }
        
        // if player didn't show up to the exam, score = 0 regardless
        
        if (score == 0) {
            results = "You did not show up to the exam...";
        }
        
        System.out.println(results + " Score: " + score);
    }
}
