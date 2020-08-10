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

import java.io.File;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;

/**
 * Stores the data for a month. Can read data from a file to add days.
 * 
 * @author Alec Osmak
 */
class MonthData {

   private ObservableList<DayData> dayList; // list of days in the month
   private Months month; // name of month it is from enum
   private int numDays;
   private int totalMonthWeight;


   /**
    * Creates a new month of data from a file of days. Also initializes
    * variables.
    * 
    * @param file  A csv file that contains the data to store for this month.
    * @param month The months it is.
    */
   MonthData(File file, Months month) {
      dayList = FXCollections.observableArrayList(); // creates new list
      this.month = month;
      numDays = 0;
      totalMonthWeight = 0;

      try {
         Scanner reader = new Scanner(file);
         reader.nextLine();
         Boolean failed = false; // whether or not it failed to create a day
         int failedLines = 0; // how many lines failed

         while (reader.hasNextLine()) { // goes through the file
            String[] line = reader.nextLine().split(","); // separates file line
            String date = line[0]; // separates date

            try {
               // stores info from the line of the file
               String stringDay = date.substring(date.lastIndexOf("-"));
               int day = Integer.parseInt(stringDay);
               String farmID = line[1].trim();
               if(farmID.equals(""))
                  throw new Exception();
               int weight = Integer.parseInt(line[2]);

               addDay(date, day, farmID, weight);

            } catch (Exception e) { // catches when data is not right
               failed = true;
               failedLines++;
            }
         }

         reader.close();

         if (failed) { // shows error dialog
            Alert alert = new Alert(AlertType.ERROR, failedLines + " lines of "
                  + "data had errors and were skipped. All other lines were "
                  + "still added.");
            alert.setHeaderText("Error Loading " + file.getName());
            alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE,
                  Region.USE_PREF_SIZE);
            alert.show();
         }

      } catch (Exception e) {
         System.out.println("Error: Unknown\n");
         e.printStackTrace();
      }
   }


   /**
    * Returns the list of days for this month.
    * 
    * @return The list of days.
    */
   ObservableList<DayData> getDayList() {
      return dayList;
   }


   /**
    * Finds and returns a day of data for a farm.
    * 
    * @param day    The day in the month it is.
    * @param farmID The farm ID of the day data to get.
    * @return The day of data associated with the parameters, or null if the day
    *         doesn't exist.
    */
   DayData getDay(int day, String farmID) {
      for (DayData dayData : dayList) { // goes through everyday in the list
         if (dayData.getDay() == day && dayData.getFarmID().equals(farmID))
            return dayData;
      }

      return null;
   }


   /**
    * Returns what month it is for this month of data.
    * 
    * @return The month it is.
    */
   Months getMonth() {
      return month;
   }


   /**
    * Returns the total number of DayData instances this month stores.
    * 
    * @return The number of days in this list.
    */
   int getNumDays() {
      return numDays;
   }


   /**
    * Returns the sum of weights for all days in this month.
    * 
    * @return The total weight sold for this month.
    */
   int getTotalMonthWeight() {
      return totalMonthWeight;
   }

   
   /**
    * Searches for a day of data in this month.
    * 
    * @param day The day to search for.
    * @return True if this month contains the day, false otherwise.
    */
   boolean contains(DayData day) {
      for(DayData currentDay : dayList)
         if(currentDay.isEqualTo(day))
            return true;
      
      return false;
   }
   
   
   /**
    * Replaces the weight of the day in this list with the new one.
    * 
    * @param newDay The new day that should be in this month instead.
    */
   void replaceDay(DayData newDay) {
      for(DayData currentDay : dayList)
         if(currentDay.isEqualTo(newDay)) {
            int oldWeight = currentDay.getWeight();
            int newWeight = newDay.getWeight();
            
            totalMonthWeight -= oldWeight;
            totalMonthWeight += newWeight;
            
            currentDay.setWeight(newWeight);
            
            return;
         }
   }
   

   /**
    * Adds a day's worth of data to the list.
    * 
    * @param date   The String of the complete date.
    * @param day    The day in the month it is.
    * @param farmID The farm ID of the farm whose day data this is.
    * @param weight The weight sold for on this day.
    */
   void addDay(String date, int day, String farmID, int weight) {
      DayData newDay = new DayData(date, day, farmID, weight);
      
      if(contains(newDay)) {
         replaceDay(newDay);
         return;
      }
      
      dayList.add(new DayData(date, day, farmID, weight));

      totalMonthWeight += weight;
      numDays++;
   }


}
