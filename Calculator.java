import java.util.Random;
/**
 * Class Calculator - behind all the calculations needed to be made.
 *
 * @author Archuthan Mohanathasan
 * @version 2016.02.29
 */
public class Calculator
{
    private int totalPenalty;
    private boolean wentLab;
    private boolean wentLecture;
    private boolean wentExam;
    private Random rand;


    /**
     * Constructor for Calculator class.
     */
    public Calculator()
    {
        wentLab = false;
        wentLecture = false;
        wentExam = false;
        totalPenalty = 0;
        rand = new Random();
    }
    
    /**
     * Information about entering an event (lab, lecture, exam).
     */
    public void lateInfo(int late, String event) {
        int hour = 0;
        int min = 0;
        int early = - late;

        if (event.equals("lab")) {
            if (!wentLab) {
                if (late > 0) {
                    hour = late/60;
                    min = late%60;
                    System.out.println("You are " + hour + " hours and " + min + " minutes late.");
                    if (late >= 120) {
                        System.out.println("Too late! Lab has finished...");
                    }
                    else {
                        System.out.println("Get started quickly...");
                        wentLab = true;
                    }
                }
                else if (late < 0) {
                    late = -late;
                    hour = late/60;
                    min = late%60;
                    System.out.println("You are " + hour + " hours and " + min + " minutes early.");
                    if (late > 20) {
                        System.out.println("You are too early... Go back and come closer to start.");
                    }
                    else {
                        System.out.println("Welcome to your lab session!");
                        System.out.println("Lab is 2 hours long");
                        wentLab = true;
                    }
                }
                else if (late == 0) {
                    System.out.println("You are on time. /n");
                    System.out.println("Welcome to your lab session!");
                    System.out.println("Lab is 2 hours long");
                    wentLab = true;
                }
            }
            else if (wentLab) {
                System.out.println("You have already attended your lab...");
            }
        }
        else if (event.equals("lecture")) {
            if (!wentLecture) {
                if (late > 0) {
                    hour = late/60;
                    min = late%60;
                    System.out.println("You are " + hour + " hours and " + min + " minutes late.");
                    if (late >= 120) {
                        System.out.println("Too late! Lecture has finished...");
                    }
                    else {
                        System.out.println("Find a seat quick!");
                        wentLecture = true;
                    }
                }
                else if (late < 0) {
                    hour = early/60;
                    min = early%60;
                    System.out.println("You are " + hour + " hours and " + min + " minutes early.");
                    if (early > 20) {
                        System.out.println("You are too early... Go back and come closer to start.");
                    }
                    else {
                        System.out.println("Welcome to your lecture!");
                        System.out.println("This lecture is 2 hours long");
                        wentLecture = true;
                    }
                }
                else if (late == 0) {
                    System.out.println("You are on time. /n");
                    System.out.println("Welcome to your Lecture!");
                    System.out.println("This lecture is 2 hours long");
                    wentLecture = true;
                }
            }
            else if (wentLecture) {
                System.out.println("You have already gone to your lecture...");
            }
        }
        else if (event.equals("exam")){
            if (late > 0){
                hour = late/60;
                min = late%60;
                System.out.println("You are " + hour + " hours and " + min + " minutes late.");
                if (late >= 120) {
                    System.out.println("Too late! Exam has finished...");
                    System.out.println("To get your results, use the results command");
                }
                else {
                    System.out.println("Get started quickly...");
                    System.out.println("**PENS DOWN, EXAM IS OVER**");
                    System.out.println("To get your results, use the results command");
                    wentExam = true;
                }
            }
            else if (late < 0) {
                hour = early/60;
                min = early%60;
                System.out.println("You are " + hour + " hours and " + min + " minutes early.");
                if (early > 20) {
                    System.out.println("You are too early... Go back and come closer to start.");
                }
                else {
                    System.out.println("This exam is 2 hours long.");
                    System.out.println("Good luck, you may begin.");
                    System.out.println("**PENS DOWN, EXAM IS OVER**");
                    System.out.println("To get your results, use the results command");
                    wentExam = true;
                }
            }
            else if (late == 0) {
                System.out.println("You are on time. /n");
                System.out.println("This exam is 2 hours long.");
                System.out.println("Good luck, you may begin.");
                System.out.println("**PENS DOWN, EXAM IS OVER**");
                System.out.println("To get your results, use the results command");
                wentExam = true;
            }
        }
    }
    
    /**
     * Accessor method to return total penalty.
     */
    public int returnPenaltyTotal()
    {
        return totalPenalty;
    }
    
    /**
     * Adds penalty to total penalty.
     */
    public void addPenalty(int penalty)
    {
        totalPenalty += penalty;
    }
    
    /**
     * Reduction of penalty from total penalty.
     */
    public void removePenalty(int points)
    {
        totalPenalty -= points;
    }
    
    /**
     * Calculation of final exam results based on penalty points.
     */
    public int calcResults()
    {   
        int score = 0;
        
        if (!wentLab) {
            totalPenalty += 20;
        }
        
        if (!wentLecture) {
            totalPenalty += 18;
        }
        
        
        
        if (totalPenalty <= 18) {
            score = 100 - rand.nextInt(26);
        }
        else if ((totalPenalty >= 19) && (totalPenalty <= 47 )) {
            score = 74 - rand.nextInt(35);
        }
        else if (totalPenalty >=48 ) {
            score = rand.nextInt(40);
        }
        
        
        if (!wentExam) {
            score = 0; // if player doen't attend exam, score = 0 regardless of other factors.
        }
        
        return score;
    }
    
    /**
     * Determines penalty for being late.
     * @param late Minutes late.
     * @param event Event being attended by player.
     * @return points Penalty points to be added to total penalty.
     */
    public int calcPoints(int late, String event)
    {
        int points = 0;
        if (event.equals("lab")) {
            if (late > 0 && late <= 10) {
                points = 3;
            }
            else if (late > 10 && late <= 45) {
                points = 7;
            }
            else if (late > 45 && late <= 75) {
                points = 11;
            }
            else if (late > 75) {
                points = 19;
            }
        }
        else if (event.equals("lecture")) {
            if (late > 0 && late <= 10) {
                points = 2;
            }
            else if (late > 10 && late <= 45) {
                points = 9;
            }
            else if (late > 45 && late <= 75) {
                points = 11;
            }
            else if (late > 75) {
                points = 20;
            }
        }
        else if (event.equals("exam")) {
            if (late > 0 && late <= 10) {
                points = 5;
            }
            else if (late > 10 && late <= 45) {
                points = 10;
            }
            else if (late > 45 && late <= 75) {
                points = 25;
            }
            else if (late > 75) {
                points = 50;
            }
        }
        return points;
    }
}
