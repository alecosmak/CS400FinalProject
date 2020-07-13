package application;

import java.io.File;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MonthData {
   private ObservableList<DayData> dayList;
   private Months month;
   private int numDays;
   private int totalMonthWeight;

   public int getNumDays() {
      return numDays;
   }

   public ObservableList<DayData> getDayList() {
      return dayList;
   }

   public MonthData(File file, Months month) {
      dayList = FXCollections.observableArrayList();
      this.month = month;
      numDays = 0;
      totalMonthWeight = 0;

      try {
         Scanner reader = new Scanner(file);
         reader.nextLine();

         while (reader.hasNextLine()) { // loops through all the lines of the
                                        // file
            String[] line = reader.nextLine().split(",");

            String[] date = line[0].split("-");

            int day = Integer.parseInt(date[2]);
            String farmID = line[1];
            int weight = Integer.parseInt(line[2]);

            addDay(day, farmID, weight);
         }

         reader.close();

      } catch (Exception e) {
         System.out.println("Error: Unknown\n");
         e.printStackTrace();
      }
   }



   public void addDay(int day, String farmID, int weight) {

      dayList.add(new DayData(day, farmID, weight));

      numDays++;
   }



   public Months getMonth() {
      return month;
   }

   public int getTotalMonthWeight() {
      return totalMonthWeight;
   }


   public DayData getDay(int day, String farmID) {
      for (DayData dayData : dayList) {
         if (dayData.getDay() == day && dayData.getFarmID().equals(farmID))
            return dayData;
      }

      return null;
   }



}
