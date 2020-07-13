package application;

import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class YearData {

   private ObservableList<MonthData> monthList;
   private int year;
   private int numMonths;
   private int totalYearWeight;


   public int getNumMonths() {
      return numMonths;
   }

   public ObservableList<MonthData> getMonthList() {
      return monthList;
   }

   public YearData(File file, int year) {
      monthList = FXCollections.observableArrayList();
      this.year = year;
      numMonths = 0;
      totalYearWeight = 0;
      
      addMonthData(file);
   }

   public int getYear() {
      return year;
   }

   public int getTotalYearWeight() {
      return totalYearWeight;
   }

   public void addMonthData(File file) {
      String name = file.getName();
      name = name.substring(0, name.lastIndexOf(".")); // removes .csv
      String[] splitName = name.split("-");
      
      Months month = Months.values()[Integer.parseInt(splitName[1])];

      monthList.add(new MonthData(file, month));
      
      numMonths++;
   }


   public MonthData getMonthData(Months month) {
      for (MonthData monthData : monthList) {
         if (monthData.getMonth() == month)
            return monthData;
      }

      return null;
   }


}
