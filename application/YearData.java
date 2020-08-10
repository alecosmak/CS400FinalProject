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
 * Stores the data for a year. Can adds months from a file.
 * 
 * @author Alec Osmak
 */
class YearData {

   private ObservableList<MonthData> monthList; // list of months in the year
   private int year;
   private int numMonths;
   private int totalYearWeight;


   /**
    * Creates a new year from a file for a month. Also initializes fields.
    * 
    * @param file      The file for a month that is part of a new year.
    * @param yearMonth The year and month of the file.
    */
   YearData(File file, String[] yearMonth) {
      monthList = FXCollections.observableArrayList(); // creates new list
      year = Integer.parseInt(yearMonth[0]);
      numMonths = 0;
      totalYearWeight = 0;

      addMonthData(file, yearMonth[1]);
   }


   /**
    * Returns the list of months for this year.
    * 
    * @return The list of months.
    */
   ObservableList<MonthData> getMonthList() {
      return monthList;
   }


   /**
    * Finds and returns a month of data from its month name in the enum.
    * 
    * @param month The name of the month.
    * @return The month of data found, or null if the month doesn't exist.
    */
   MonthData getMonthData(Months month) {
      for (MonthData monthData : monthList) {
         if (monthData.getMonth() == month)
            return monthData;
      }

      return null;
   }


   /**
    * Returns the year that this is.
    * 
    * @return The year it is.
    */
   int getYear() {
      return year;
   }


   /**
    * Returns how many months this year currently has listed.
    * 
    * @return The number of months in this year.
    */
   int getNumMonths() {
      return numMonths;
   }


   /**
    * Returns the sum of all weights sold during this year
    * 
    * @return The total weight for this year.
    */
   int getTotalYearWeight() {
      return totalYearWeight;
   }


   /**
    * Adds a month to this year from a file and the month.
    * 
    * @param file     The file that contains a month of data.
    * @param numMonth A string value for the numerical month.
    */
   void addMonthData(File file, String numMonth) {
      // gets the name of the month from its integer value
      Months month = Months.values()[Integer.parseInt(numMonth) - 1];

      for (MonthData existingMonth : monthList) {
         if (existingMonth.getMonth() == month) {
            addToMonth(existingMonth, file);
            return;
         }
      }

      MonthData newMonth = new MonthData(file, month);
      monthList.add(newMonth);

      totalYearWeight += newMonth.getTotalMonthWeight();
      numMonths++;
   }


   /**
    * Adds data from a file to an already existing month in this year. Will
    * overwrite a day if the new day has the same date and farm id.
    * 
    * @param month The existing month.
    * @param file  The file of data.
    */
   void addToMonth(MonthData month, File file) {
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
               String farmID = line[1];
               int weight = Integer.parseInt(line[2]);

               month.addDay(date, day, farmID, weight);

            } catch (Exception e) { // catches when data is not right
               failed = true;
               failedLines++;
            }
         }

         reader.close();

         if (failed) { // shows error dialog
            Alert alert = new Alert(AlertType.ERROR, failedLines + " lines of "
                  + "data had errors and were skipped. All other lines have "
                  + "still been added.");
            alert.setHeaderText("Error Loading " + file.getName());
            alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE,
                  Region.USE_PREF_SIZE);
            alert.show();
         }

      } catch (Exception e) {
         System.out.println("Error: Unknown\n");
         e.printStackTrace();
      }
      
      // recalculates total weight for the year
      totalYearWeight = 0;
      for(MonthData currentMonth : monthList)
         totalYearWeight += currentMonth.getTotalMonthWeight();
   }


}
