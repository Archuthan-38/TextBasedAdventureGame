import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.HashMap;

/**
 * Class Time - keeps track of time
 * 
 * Includes GUI for a clock, that changes.
 * 
 * Provides hour and minutes, to be used elsewhere.
 *
 * @author Michael Kölling, David J. Barnes and Archuthan Mohanathasan
 * @version 2016.02.29
 */
public class Time
{
    private JFrame frame;
    private JLabel label;
    private int hour;
    private int minute;
    private int travelTime;
    
    private HashMap<Room, Integer> time;
    

    /**
     * Constructor for objects of class Time
     * Start of the game is at 08:45.
     */
    public Time()
    {
        makeFrame();
        hour = 8;
        minute = 45;
        label.setText("08:45");
    }
    
    /**
     * Updates clock display.
     * @param timeTaken Change in time.
     */
    public void updateDisplay(int timeTaken)
    {
        minute += timeTaken;
        hour += minute/60;
        minute = minute % 60;
        
        // add 0 before any single digit number
        
        if ((minute<10) && (hour<10)){
             String display = "0" + hour + ":" + "0" + minute;
             label.setText(display);
        }
        if ((minute<10) && (hour>=10)){
            String display =  hour + ":" + "0" + minute;
            label.setText(display);
        }
        if ((minute>=10) && (hour<10)){
            String display = "0" + hour + ":" + minute;
            label.setText(display);
        }
        if ((minute>=10) && (hour>=10)) {
            String display = hour + ":" + minute;
            label.setText(display);
        }
        
    }
    
    public Integer travel(Room neighbor)
    {
        travelTime = time.get(neighbor);
        return travelTime;
    }
    
    /**
     * Calculates how late the player was to a specific session (lab, lecture, exam).
     * @param event The session being attended.
     */
    public int calcLate(String event)
    {
        int currentMins = 60*hour + minute;
        int correctMins = 0;
        
        if (event.equals("lab")) {
            correctMins = 540; // (09:00 so, 9*60 + 0)
        }
        else if (event.equals("lecture")) {
            correctMins = 780; // (13:00 so, 13*60 + 0)
        }
        else if (event.equals("exam")) {
            correctMins = 1080; // (18:00 so, 18*60 + 0)
        }
        
        int difference = currentMins - correctMins;
        
        if (difference > -20 && difference < 120) { 
            updateDisplay(120 - difference);
        }
        // Time on clock only updates if player is not too early (within 20 mins early)
        // and if session hasn't already finished
        
        return (difference);
        
    }
    
    /**
     * Checks whether or not game should end.
     * @return true if time is past 20:00, else false.
     */
    public boolean end()
    {
        if (hour >= 20) {
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * Create the Swing frame and its content for clock.
     * Taken and adapted from @author Michael Kölling clock gui.
     */
    private void makeFrame()
    {
        frame = new JFrame("Clock");
        JPanel contentPane = (JPanel)frame.getContentPane();
        contentPane.setBorder(new EmptyBorder(1, 60, 1, 60));

        
        // Specify the layout manager with nice spacing
        contentPane.setLayout(new BorderLayout(12, 12));
        
        // Create the image pane in the center
        label = new JLabel("00:00" , SwingConstants.CENTER);
        Font displayFont = label.getFont().deriveFont(96.0f);
        label.setFont(displayFont);
        //imagePanel.setBorder(new EtchedBorder());
        contentPane.add(label, BorderLayout.CENTER);

        // Create the toolbar with the buttons
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new GridLayout(1, 0));
        
        // Add toolbar into panel with flow layout for spacing
        JPanel flow = new JPanel();
        flow.add(toolbar);
        
        contentPane.add(flow, BorderLayout.SOUTH);
        
        // building is done - arrange the components      
        frame.pack();
        
        // place the frame above the terminal window of game and show
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(3*(d.width/4) - frame.getWidth()/2, 2*(d.height/5) - frame.getHeight()/2);
        frame.setVisible(true);
    }
}
