/**
 * Project: Milk Weights Final Project
 * Files: RunFinalProject.java, YearData.java, MonthData.java,
 * DayData.java, FarmReportRow.java, TimeReportRow.java, Months.java,
 * cheeseLogo.jpg
 * 
 * Description: This is the final project for CS 400 Summer 2020. This program
 * is an interactive data visualizer that utilizes a GUI to display the data.
 * Through the GUI the user can add data from CSV files and display that data on
 * tables. The tables are interactive and give stats on the data.
 * 
 * Author: Alec Osmak
 * Email: osmak@wisc.edu
 */

package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Stores the data for a day in a month for a single farm. Gets all data from a
 * single line in the input CSV files.
 * 
 * @author Alec Osmak
 */
public class DayData {

   private final DateTimeFormatter FORMAT = // formats date string
         DateTimeFormatter.ofPattern("yyyy-M-d");
   private LocalDate date; // stores date
   private String farmID; // name of this farm
   private int weight; // weight sold on this day

   /**
    * Creates a new instance of a day's worth of data and initializes fields.
    * 
    * @param date   The String of the complete date.
    * @param farmID The farm that has this day's weight.
    * @param weight The weight that was sold on this day.
    */
   DayData(String date, String farmID, int weight) {
      this.date = LocalDate.parse(date, FORMAT); // converts string to date
      this.farmID = farmID;
      this.weight = weight;
   }

   /**
    * Gets the complete date for this day as a Date object.
    * 
    * @return The date field.
    */
   public LocalDate getDate() {
      return date;
   }

   /**
    * Gets the farm id for this day's data.
    * 
    * @return The farmID field.
    */
   public String getFarmID() {
      return farmID;
   }

   /**
    * Gets the weight this farm sold on this day.
    * 
    * @return The weight field.
    */
   public int getWeight() {
      return weight;
   }

   /**
    * Sets a new weight for this day.
    * 
    * @param newWeight The new weight to set.
    */
   void setWeight(int newWeight) {
      weight = newWeight;
   }

   /**
    * Compares this day of data to another based on farm id and date.
    * 
    * @param day The day to compare this day to.
    * @return True if the two days have the same farmID and date, false
    *         otherwise.
    */
   boolean isEqualTo(DayData day) {
      if (!farmID.equals(day.getFarmID()))
         return false;

      if (!date.equals(day.getDate()))
         return false;

      return true;
   }

}
