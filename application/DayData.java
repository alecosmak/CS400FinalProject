/**
 * Project: Milk Weights Final Project
 * Files: project.zip (RunFinalProject.java, YearData.java, MonthData.java,
 * DayData.java, Months.java, cheeseLogo.jpg, README.txt)
 * 
 * Description: This is the final project for CS 400 Summer 2020. This program
 * is an interactive data visualizer that utilizes a GUI to display the data.
 * Through the GUI the user can add, copy, and change data.
 * 
 * Author: Alec Osmak
 * Email: osmak@wisc.edu
 */

package application;

/**
 * Stores the data for a day in a month.
 * 
 * @author Alec Osmak
 */
public class DayData {

   private int day;
   private String farmID;
   private int weight;


   /**
    * Creates a new instance of a day's worth of data and initialize the fields.
    * 
    * @param day    The day in the month it is.
    * @param farmID The id of what farm has this day's weight.
    * @param weight The weight that was sold on this day.
    */
   public DayData(int day, String farmID, int weight) {
      this.day = day;
      this.farmID = farmID;
      this.weight = weight;
   }


   /**
    * Returns the day in the month it is.
    * 
    * @return The day field.
    */
   public int getDay() {
      return day;
   }


   /**
    * Returns the farm ID for this day's data.
    * 
    * @return The farmID field.
    */
   public String getFarmID() {
      return farmID;
   }


   /**
    * The weight this farm sold on this day.
    * 
    * @return The weight field.
    */
   public int getWeight() {
      return weight;
   }


}
