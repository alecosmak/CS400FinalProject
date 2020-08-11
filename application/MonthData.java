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

import java.io.File;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;

/**
 * Stores the data for a month in a year. Can read data from a file to add days.
 * Gets month from file name.
 * 
 * @author Alec Osmak
 */
class MonthData {

   private ObservableList<DayData> dayList; // list of days in this month
   private Months month; // name of month it is from enum
   private int totalMonthWeight; // total weight for month


   /**
    * Creates a new month of data from a CSV file of days and initializes
    * variables. Handles errors in the input file.
    * 
    * @param file  A CSV file that contains the data to store for this month.
    * @param month The months it is.
    */
   MonthData(File file, Months month) {
      dayList = FXCollections.observableArrayList(); // creates new list
      this.month = month;
      totalMonthWeight = 0;

      try { // tries to load file and read its lines
         Scanner reader = new Scanner(file);
         reader.nextLine(); // skips first line
         Boolean failed = false; // whether or not it failed to create a day
         int failedLines = 0; // how many lines failed

         while (reader.hasNextLine()) { // goes through the file
            String[] line = reader.nextLine().split(","); // separates file line
            String date = line[0]; // gets date value

            try { // tries to add day
               // stores info from the line of the file
               String farmID = line[1].trim();
               int weight = Integer.parseInt(line[2]);

               if (farmID.equals("")) // filters out empty string
                  throw new Exception();

               addDay(date, farmID, weight);

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

      } catch (Exception e) { // catches rest of exceptions (shouldn't happen)
         System.out.println("Error: Unknown\n");
         e.printStackTrace();
      }
   }


   /**
    * Returns the data for all days in this month.
    * 
    * @return The list of days.
    */
   ObservableList<DayData> getDayList() {
      return dayList;
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
    * Returns the sum of weights for all days in this month.
    * 
    * @return The total weight in this month.
    */
   int getTotalMonthWeight() {
      return totalMonthWeight;
   }


   /**
    * Adds a day's worth of data to the list.
    * 
    * @param date   The String of the complete date.
    * @param farmID The farm ID of the farm whose day data this is.
    * @param weight The weight sold on this day.
    */
   void addDay(String date, String farmID, int weight) {
      DayData newDay = new DayData(date, farmID, weight);

      if (contains(newDay)) { // checks if day already exists
         replaceDay(newDay);
         return;
      }

      dayList.add(new DayData(date, farmID, weight));
      totalMonthWeight += weight;
   }


   /**
    * Replaces the weight of the day in this list with the new one.
    * 
    * @param newDay The new day that should be in this month instead.
    */
   void replaceDay(DayData newDay) {
      for (DayData currentDay : dayList) // goes through all days
         if (currentDay.isEqualTo(newDay)) {
            int oldWeight = currentDay.getWeight();
            int newWeight = newDay.getWeight();

            totalMonthWeight -= oldWeight; // fixes total weight for month
            totalMonthWeight += newWeight;

            currentDay.setWeight(newWeight);
            return;
         }
   }


   /**
    * Searches for a day of data in this month.
    * 
    * @param day The day to search for.
    * @return True if this month contains the day, false otherwise.
    */
   boolean contains(DayData day) {
      for (DayData currentDay : dayList) // goes through all days
         if (currentDay.isEqualTo(day))
            return true;

      return false;
   }


}
