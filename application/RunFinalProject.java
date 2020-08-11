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
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Runs the program. Creates the GUI and shows it to the user. Is able to read
 * CSV files and gather the data from them.
 * 
 * @author Alec Osmak
 */
public class RunFinalProject extends Application {

   private Stage mainStage; // stage everything is shown in
   private TableView<DayData> mainTable; // the table displayed
   private ObservableList<YearData> yearList; // list of all years
   private List<File> inputFiles; // the files input into the ds
   private Font header; // font used for header text
   private double comboWidth; // width of combo boxes
   // combo boxes that are updated as data is added
   private ComboBox<String> tableFarms;
   private ComboBox<String> tableYears;
   private ComboBox<String> tableMonths;
   private ComboBox<String> farmReportFarms;
   private ComboBox<String> farmReportYears;
   private ComboBox<String> annualReportYears;
   private ComboBox<String> monthlyReportYears;
   private ComboBox<String> monthlyReportMonths;
   // text fields that change depending on user input
   private TextField totalTableField;
   private TextField percentTableField;
   private TextField monthMin;
   private TextField monthMax;
   private TextField farmWeightMin;
   private TextField farmWeightMax;
   private TextField farmWeightAvg;
   private TextField farmPercentMin;
   private TextField farmPercentMax;
   private TextField idMin;
   private TextField idMax;
   private TextField timeWeightMin;
   private TextField timeWeightMax;
   private TextField timeWeightAvg;
   private TextField timePercentMin;
   private TextField timePercentMax;


   /**
    * Finds and returns a year of data from its integer value.
    * 
    * @param year The year to get.
    * @return The year of data found, or null if it doesn't exist.
    */
   private YearData getYear(int year) {
      for (YearData yearData : yearList) {
         if (yearData.getYear() == year)
            return yearData;
      }

      return null;
   }


   /**
    * Adds a year to the list of years from a file of a month in that year.
    * 
    * @param file      The data for a month.
    * @param yearMonth The year and month the file is as String array.
    */
   private void addYear(File file, String[] yearMonth) {
      if (yearList == null) // creates new list
         yearList = FXCollections.observableArrayList();

      yearList.add(new YearData(file, yearMonth)); // adds year to list
   }


   /**
    * Updates the combo boxes to have all options available from the data.
    */
   private void updateComboBoxes() {
      // resets combo boxes to be empty
      tableFarms.getItems().clear();
      tableYears.getItems().clear();
      tableMonths.getItems().clear();
      farmReportFarms.getItems().clear();
      farmReportYears.getItems().clear();
      annualReportYears.getItems().clear();
      monthlyReportYears.getItems().clear();
      monthlyReportMonths.getItems().clear();

      // adds a null option for any
      tableFarms.getItems().add(null);
      tableYears.getItems().add(null);
      tableMonths.getItems().add(null);

      // creates list of years
      ObservableList<String> years = FXCollections.observableArrayList();
      for (YearData year : yearList) // goes through all years
         if (!years.contains(String.valueOf(year.getYear())))
            years.add(String.valueOf(year.getYear()));
      Collections.sort(years); // sorts years

      // adds years to the year combo boxes
      tableYears.getItems().addAll(years);
      farmReportYears.getItems().addAll(years);
      annualReportYears.getItems().addAll(years);
      monthlyReportYears.getItems().addAll(years);

      // creates list of months
      ObservableList<Months> months = FXCollections.observableArrayList();
      for (YearData year : yearList) // goes through every month
         for (MonthData month : year.getMonthList())
            if (!months.contains(month.getMonth()))
               months.add(month.getMonth());
      Collections.sort(months); // sorts months

      // adds months to the month combo boxes
      for (Months month : months) {
         tableMonths.getItems().add(String.valueOf(month));
         monthlyReportMonths.getItems().add(String.valueOf(month));
      }

      // creates list of farms
      ObservableList<String> farms = FXCollections.observableArrayList();
      for (YearData year : yearList) // goes through every day
         for (MonthData month : year.getMonthList())
            for (DayData day : month.getDayList())
               if (!farms.contains(day.getFarmID()))
                  farms.add(day.getFarmID());
      Collections.sort(farms); // sorts farms

      // adds farms to the farm id combo boxes
      tableFarms.getItems().addAll(farms);
      farmReportFarms.getItems().addAll(farms);
   }


   /**
    * Creates and formats the combo boxes used in this GUI.
    */
   private void createComboBoxes() {
      // combo boxes for table tab
      tableFarms = new ComboBox<>();
      tableFarms.setPrefWidth(comboWidth);
      tableFarms.setOnAction(a -> displayMainTableData());

      tableYears = new ComboBox<>();
      tableYears.setPrefWidth(comboWidth);
      tableYears.setOnAction(a -> displayMainTableData());

      tableMonths = new ComboBox<>();
      tableMonths.setPrefWidth(comboWidth);
      tableMonths.setOnAction(a -> displayMainTableData());

      // combo boxes for report tabs
      farmReportFarms = new ComboBox<>();
      farmReportYears = new ComboBox<>();
      farmReportFarms.setPrefWidth(comboWidth);
      farmReportYears.setPrefWidth(comboWidth);

      annualReportYears = new ComboBox<>();
      annualReportYears.setPrefWidth(comboWidth);

      monthlyReportYears = new ComboBox<>();
      monthlyReportMonths = new ComboBox<>();
      monthlyReportYears.setPrefWidth(comboWidth);
      monthlyReportMonths.setPrefWidth(comboWidth);
   }


   /**
    * Creates and formats the text fields used in this GUI.
    * 
    * @param fieldWidth The width of the text fields.
    */
   private void createTextFields(double fieldWidth) {
      // text fields for table tab
      totalTableField = new TextField("0");
      percentTableField = new TextField("0.0%");
      totalTableField.setPrefWidth(fieldWidth);
      percentTableField.setPrefWidth(fieldWidth);

      // text fields for farm report tab
      monthMin = new TextField();
      monthMax = new TextField();
      farmWeightMin = new TextField();
      farmWeightMax = new TextField();
      farmWeightAvg = new TextField();
      farmPercentMin = new TextField();
      farmPercentMax = new TextField();
      monthMin.setPrefWidth(fieldWidth);
      monthMax.setPrefWidth(fieldWidth);
      farmWeightMin.setPrefWidth(fieldWidth);
      farmWeightMax.setPrefWidth(fieldWidth);
      farmWeightAvg.setPrefWidth(fieldWidth);
      farmPercentMin.setPrefWidth(fieldWidth);
      farmPercentMax.setPrefWidth(fieldWidth);

      // text fields for monthly report tab
      idMin = new TextField();
      idMax = new TextField();
      timeWeightMin = new TextField();
      timeWeightMax = new TextField();
      timeWeightAvg = new TextField();
      timePercentMin = new TextField();
      timePercentMax = new TextField();
      idMin.setPrefWidth(fieldWidth);
      idMax.setPrefWidth(fieldWidth);
      timeWeightMin.setPrefWidth(fieldWidth);
      timeWeightMax.setPrefWidth(fieldWidth);
      timeWeightAvg.setPrefWidth(fieldWidth);
      timePercentMin.setPrefWidth(fieldWidth);
      timePercentMax.setPrefWidth(fieldWidth);
   }


   /**
    * Downloads all available data to a single CSV file.
    * 
    * @param fileName The name of the new CSV file.
    */
   private void downloadData(String fileName) {
      if (!fileName.endsWith(".csv")) // makes sure the file name ends in .csv
         fileName = fileName.concat(".csv");

      try { // tries to create file and print to it
         File file = new File(fileName);
         PrintWriter writer = new PrintWriter(file);

         // adds all data to the file
         writer.println("date,farm_id,weight");
         for (YearData year : yearList) { // goes through every day
            for (MonthData month : year.getMonthList()) {
               for (DayData day : month.getDayList()) {
                  writer.print(day.getDate() + ",");
                  writer.print(day.getFarmID() + ",");
                  writer.println(day.getWeight());
               }
            }
         }

         writer.flush();
         writer.close();

      } catch (Exception e) { // shows an error dialog for bad file name
         Alert alert = new Alert(AlertType.ERROR,
               "File name not allowed. File creation canceled.");
         alert.setHeaderText("Error With File Name " + fileName);
         alert.show();
      }
   }


   /**
    * Creates the pane for the Home tab including all information and controls.
    * 
    * @return The pane for the home tab.
    */
   private Pane createHomePane() {
      BorderPane homePane = new BorderPane();

      // button to stop program
      Button exitButton = new Button("EXIT Program");
      exitButton.setOnAction(a -> {
         mainStage.close();
         Platform.exit();
      });
      homePane.setTop(exitButton);
      BorderPane.setMargin(exitButton, new Insets(20, 0, 0, 30));

      // creates and formats the center displayed text
      Text welcome = new Text("Welcome to the Milk Weights GUI!");
      String homeString = "Please select a tab to access milk weight data.\n -"
            + " Table: displays a data table of all current data\n - Reports: "
            + "shows tables with stats\n - Input Files: allows you to add more"
            + " data";
      Text tabInfo = new Text(homeString);
      welcome.setFont(new Font(25));
      tabInfo.setFont(header);

      // vbox for center text
      VBox homeText = new VBox(25, welcome, tabInfo);
      homeText.setAlignment(Pos.TOP_CENTER);
      homeText.setPadding(new Insets(50, 0, 0, 0));
      homePane.setCenter(homeText);

      // vbox for the bottom elements
      VBox bottom = new VBox();
      bottom.setAlignment(Pos.CENTER);
      bottom.setPadding(new Insets(0, 0, 2, 0));

      try { // tries to load in an image to the home tab
         FileInputStream inputImage = new FileInputStream("banner.jpg");
         Image image =
               new Image(inputImage, Region.USE_PREF_SIZE, 150, true, true);
         ImageView imageView = new ImageView(image);
         bottom.getChildren().add(imageView);

      } catch (Exception e) {
      }

      // footer text
      Text infoText = new Text("Designed by Alec Osmak for a final project.  "
            + "UW-Madison CS400 Summer 2020");
      bottom.getChildren().add(infoText);
      homePane.setBottom(bottom);

      return homePane;
   }


   /**
    * Updates the sum of the weights that the table currently has displayed.
    */
   private void updateTableTotalWeight() {
      int tableTotal = 0; // gets the total weight of rows being shown
      for (DayData day : mainTable.getItems())
         tableTotal += day.getWeight();

      totalTableField.setText(String.valueOf(tableTotal));

      int dataTotal = 0;
      for (YearData year : yearList) // gets the total weight for all data
         dataTotal += year.getTotalYearWeight();

      // percent of total
      double percent = ((double) tableTotal / dataTotal) * 100;

      DecimalFormat df = new DecimalFormat("###.##"); // 2 decimal digits
      percentTableField.setText(df.format(percent) + "%");
   }


   /**
    * Adds the data to the table depending on what the user wants to be shown.
    * It starts with all data and removes data based on the combo boxes above
    * the table.
    */
   private void displayMainTableData() {
      mainTable.getItems().clear(); // clears table

      if (yearList != null) {
         ArrayList<DayData> tableData = new ArrayList<>(); // stores all data

         for (YearData yearData : yearList) // goes through all days
            for (MonthData monthData : yearData.getMonthList())
               for (DayData dayData : monthData.getDayList())
                  tableData.add(dayData); // adds all data to tableData

         // list of data to remove from tableData
         ArrayList<DayData> removeList = new ArrayList<>();

         // adds data to remove list by farm id
         if (tableFarms.getValue() != null) {
            for (int i = 0; i < tableData.size(); i++) {
               DayData dayData = tableData.get(i);

               if (!dayData.getFarmID().equals(tableFarms.getValue()))
                  removeList.add(dayData);
            }
         }

         // adds data to removes list by year
         if (tableYears.getValue() != null) {
            for (int i = 0; i < tableData.size(); i++) {
               DayData dayData = tableData.get(i);
               int year = dayData.getDate().getYear();

               if (year != Integer.valueOf(tableYears.getValue()))
                  removeList.add(dayData);
            }
         }

         // adds data to removes list by month
         if (tableMonths.getValue() != null) {
            String value = tableMonths.getValue();
            Months month = Months.valueOf(value);

            for (int i = 0; i < tableData.size(); i++) {
               DayData dayData = tableData.get(i);
               int dateMonth = dayData.getDate().getMonthValue();

               if (month != Months.values()[dateMonth - 1])
                  removeList.add(dayData);
            }
         }

         // removes data from all data list
         for (DayData dayData : removeList)
            tableData.remove(dayData);

         // shows remaining data to table
         for (DayData dayData : tableData)
            mainTable.getItems().add(dayData);
      }

      updateTableTotalWeight();
   }


   /**
    * Creates the main table that is displayed in the table tab.
    */
   @SuppressWarnings("unchecked") // for columns of different types
   private void createMainTable() {
      // creates and formats table columns
      TableColumn<DayData, LocalDate> dateCol = new TableColumn<>("Date");
      TableColumn<DayData, String> farmCol = new TableColumn<>("Farm ID");
      TableColumn<DayData, Integer> weightCol = new TableColumn<>("Weight");
      dateCol.setPrefWidth(comboWidth);
      farmCol.setPrefWidth(comboWidth);
      weightCol.setPrefWidth(comboWidth);

      // sets the data for each column from the DayData class
      dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
      farmCol.setCellValueFactory(new PropertyValueFactory<>("farmID"));
      weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));

      // creates and formats table
      mainTable = new TableView<>();
      mainTable.getColumns().addAll(dateCol, farmCol, weightCol);
      mainTable.setMaxWidth(comboWidth * 3 + 15);
   }


   /**
    * Creates the pane for the table tab including all information and controls.
    * 
    * @return The pane for the table tab.
    */
   private Pane createMainTablePane() {
      // creates labels for options
      Label farmLabel = new Label("Farm:");
      Label yearLabel = new Label("Year:");
      Label monthLabel = new Label("Month:");

      // button to download all available data
      Button downloadButton = new Button("Download All Data");
      downloadButton.setOnAction(a -> {
         if (yearList != null) {

            // creates dialog for file name input
            TextInputDialog inputDialog = new TextInputDialog("fileName");
            inputDialog.setTitle("Enter File Name");
            inputDialog.setHeaderText("Please enter name of new .csv file ("
                  + "exclude file extension).");
            inputDialog.getEditor().setMaxWidth(150);

            Optional<String> result = inputDialog.showAndWait();
            if (result.isPresent()) // gets name entered writes data to file
               downloadData(result.get());
         }
      });

      // creates and formats top options
      GridPane topOptions = new GridPane();
      topOptions.addRow(0, farmLabel, yearLabel, monthLabel);
      topOptions.addRow(1, tableFarms, tableYears, tableMonths, downloadButton);
      topOptions.setHgap(20);
      topOptions.setPadding(new Insets(0, 0, 5, 0));
      topOptions.setAlignment(Pos.CENTER);

      // total weight section under table
      Label totalLabel = new Label("Total Weight:");
      Label percentTableLabel = new Label("Pecent of Total:");
      HBox totalWeight = new HBox(10, totalLabel, totalTableField,
            percentTableLabel, percentTableField);
      totalWeight.setPadding(new Insets(5, 0, 0, 0));
      totalWeight.setAlignment(Pos.CENTER);

      // creates and formats tablePane
      createMainTable();
      VBox tablePane = new VBox(topOptions, mainTable, totalWeight);
      tablePane.setPadding(new Insets(25, 10, 10, 10));
      tablePane.setAlignment(Pos.TOP_CENTER);

      return tablePane;
   }


   /**
    * Creates the table that is displayed on the farm report tab.
    * 
    * @return The table to be displayed.
    */
   @SuppressWarnings("unchecked") // for columns of different types
   private TableView<FarmReportRow> createFarmReportTable() {
      // creates and formats table columns
      TableColumn<FarmReportRow, Months> monthCol = new TableColumn<>("Month");
      TableColumn<FarmReportRow, Integer> weightCol =
            new TableColumn<>("Total Weight");
      TableColumn<FarmReportRow, Double> percentCol =
            new TableColumn<>("% of Month");
      monthCol.setPrefWidth(comboWidth);
      weightCol.setPrefWidth(comboWidth);
      percentCol.setPrefWidth(comboWidth);

      // sets the data for each column from the FarmReportRow class
      monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
      weightCol.setCellValueFactory(new PropertyValueFactory<>("totalWeight"));
      percentCol
            .setCellValueFactory(new PropertyValueFactory<>("percentTotal"));

      // creates and formats table
      TableView<FarmReportRow> table = new TableView<>();
      table.getColumns().addAll(monthCol, weightCol, percentCol);
      table.setMaxWidth(comboWidth * 3 + 15);

      return table;
   }


   /**
    * Creates a table for the annual or monthly report tabs. They are different
    * only in the column header for percent.
    * 
    * @param type The type of table to create, either "Year" or "Month".
    * @return The table to be displayed
    */
   @SuppressWarnings("unchecked") // for columns of different types
   private TableView<TimeReportRow> createTimeReportTable(String type) {
      // creates and formats table columns
      TableColumn<TimeReportRow, String> farmCol = new TableColumn<>("Farm ID");
      TableColumn<TimeReportRow, Integer> weightCol =
            new TableColumn<>("Total Weight");
      TableColumn<TimeReportRow, Double> percentCol =
            new TableColumn<>("% of " + type);
      farmCol.setPrefWidth(comboWidth);
      weightCol.setPrefWidth(comboWidth);
      percentCol.setPrefWidth(comboWidth);

      // sets the data for each column from the TimeReportRow class
      farmCol.setCellValueFactory(new PropertyValueFactory<>("farmID"));
      weightCol.setCellValueFactory(new PropertyValueFactory<>("totalWeight"));
      percentCol
            .setCellValueFactory(new PropertyValueFactory<>("percentTotal"));

      // creates and formats table
      TableView<TimeReportRow> table = new TableView<>();
      table.getColumns().addAll(farmCol, weightCol, percentCol);
      table.setMaxWidth(comboWidth * 3 + 15);

      return table;
   }


   /**
    * Creates the pane for the farm report tab including all information and
    * controls.
    * 
    * @return The pane for the farm report tab.
    */
   private Pane createFarmReportPane() {
      // creates labels and table
      Label farmLabel = new Label("Farm:");
      Label yearLabel = new Label("Year:");
      farmLabel.setFont(header);
      yearLabel.setFont(header);
      TableView<FarmReportRow> farmTable = createFarmReportTable();

      // button to show table data
      Button farmEnter = new Button("Enter");
      farmEnter.setOnAction(a -> {
         farmTable.getItems().clear();

         if (farmReportFarms.getValue() != null // if both options are selected
               && farmReportYears.getValue() != null) {
            // stores farm and year
            String farmID = farmReportFarms.getValue();
            YearData yearData =
                  getYear(Integer.valueOf(farmReportYears.getValue()));

            // list of table rows
            ArrayList<FarmReportRow> monthList = new ArrayList<>();
            for (MonthData month : yearData.getMonthList()) {
               // creates new row for every month
               FarmReportRow farm = new FarmReportRow(month.getMonth(),
                     month.getTotalMonthWeight());

               for (DayData day : month.getDayList()) // adds weight to month
                  if (day.getFarmID().equals(farmID))
                     farm.addWeight(day.getWeight());

               monthList.add(farm);
            }

            farmTable.getItems().addAll(monthList); // adds data to table

            // calculates stats
            int sumWeights = 0;
            FarmReportRow minRow = null;
            FarmReportRow maxRow = null;
            int minWeight = Integer.MAX_VALUE;
            int maxWeight = -1;

            // goes through every row in table
            for (FarmReportRow row : farmTable.getItems()) {
               int weight = row.getTotalWeight();
               sumWeights += weight; // finds total

               if (weight < minWeight) { // finds min
                  minRow = row;
                  minWeight = weight;
               }

               if (weight > maxWeight) { // finds max
                  maxRow = row;
                  maxWeight = weight;
               }
            }

            // sets text on stats text fields
            monthMin.setText(minRow.getMonth().toString());
            farmWeightMin.setText(String.valueOf(minRow.getTotalWeight()));
            farmPercentMin.setText(String.valueOf(minRow.getPercentTotal()));
            monthMax.setText(maxRow.getMonth().toString());
            farmWeightMax.setText(String.valueOf(maxRow.getTotalWeight()));
            farmPercentMax.setText(String.valueOf(maxRow.getPercentTotal()));
            farmWeightAvg.setText(
                  String.valueOf(sumWeights / farmTable.getItems().size()));
         }
      });

      // creates hbox for top options
      HBox farmBox = new HBox(10, farmLabel, farmReportFarms, yearLabel,
            farmReportYears, farmEnter);
      farmBox.setAlignment(Pos.CENTER);

      // creates and formats grid of stats
      GridPane farmStats = new GridPane();
      Label farmMin = new Label("Min:");
      Label farmMax = new Label("Max:");
      Label farmAvg = new Label("Avg:");
      farmStats.addColumn(0, farmMin, farmMax, farmAvg);
      farmStats.addColumn(1, monthMin, monthMax);
      farmStats.addColumn(2, farmWeightMin, farmWeightMax, farmWeightAvg);
      farmStats.addColumn(3, farmPercentMin, farmPercentMax);
      farmStats.setHgap(5);
      farmStats.setVgap(5);
      farmStats.setAlignment(Pos.CENTER);

      // creates and formats farm report pane
      VBox farmReportPane = new VBox(10, farmBox, farmTable, farmStats);
      farmReportPane.setPadding(new Insets(10, 10, 10, 10));
      farmReportPane.setAlignment(Pos.CENTER);

      return farmReportPane;
   }


   /**
    * Creates the pane for the annual report tab including all information and
    * controls.
    * 
    * @return The pane for the annual report tab.
    */
   private Pane createAnnualReportPane() {
      // creates label and table
      Label yearAnnualLabel = new Label("Year:");
      yearAnnualLabel.setFont(header);
      TableView<TimeReportRow> annualTable = createTimeReportTable("Year");

      // button to show table data
      Button annualEnter = new Button("Enter");
      annualEnter.setOnAction(a -> {
         annualTable.getItems().clear();

         if (annualReportYears.getValue() != null) {
            YearData yearData = // stores year of data
                  getYear(Integer.valueOf(annualReportYears.getValue()));

            // all farms to show
            ObservableList<String> farmIDs = farmReportFarms.getItems();

            // list of table rows
            ArrayList<TimeReportRow> farmList = new ArrayList<>();
            for (String farmID : farmIDs) {
               TimeReportRow farm = // creates row for every farm
                     new TimeReportRow(farmID, yearData.getTotalYearWeight());

               farmList.add(farm);
            }

            // goes through every day
            for (MonthData month : yearData.getMonthList())
               for (DayData day : month.getDayList())
                  for (TimeReportRow farm : farmList)
                     if (day.getFarmID().equals(farm.getFarmID()))
                        farm.addWeight(day.getWeight()); // adds weight to farm

            annualTable.getItems().addAll(farmList); // adds data to table
         }
      });

      // creates hbox for top options
      HBox annualBox =
            new HBox(10, yearAnnualLabel, annualReportYears, annualEnter);
      annualBox.setAlignment(Pos.CENTER);

      // creates and formats annual report tab
      VBox annualReportPane = new VBox(10, annualBox, annualTable);
      annualReportPane.setPadding(new Insets(10, 10, 10, 10));
      annualReportPane.setAlignment(Pos.CENTER);

      return annualReportPane;
   }


   /**
    * Creates the pane for the monthly report tab including all information and
    * controls.
    * 
    * @return The pane for the monthly report tab.
    */
   private Pane createMonthlyReportPane() {
      // creates labels and table
      Label yearMonthlyLabel = new Label("Year:");
      Label monthMonthlyLabel = new Label("Month:");
      yearMonthlyLabel.setFont(header);
      monthMonthlyLabel.setFont(header);
      TableView<TimeReportRow> monthlyTable = createTimeReportTable("Month");

      // button to show table data
      Button monthlyEnter = new Button("Enter");
      monthlyEnter.setOnAction(a -> {
         monthlyTable.getItems().clear();

         // if both values are selected
         if (monthlyReportYears.getValue() != null
               && monthlyReportMonths.getValue() != null) {
            // stores year and month
            YearData yearData =
                  getYear(Integer.valueOf(monthlyReportYears.getValue()));
            MonthData monthData = yearData
                  .getMonthData(Months.valueOf(monthlyReportMonths.getValue()));

            // all farms to show
            ObservableList<String> farmIDs = farmReportFarms.getItems();

            // list of table rows
            ArrayList<TimeReportRow> farmList = new ArrayList<>();
            for (String farmID : farmIDs) {
               TimeReportRow farm = // creates row for every farm
                     new TimeReportRow(farmID, yearData.getTotalYearWeight());

               farmList.add(farm);
            }

            // goes through every day
            for (DayData day : monthData.getDayList())
               for (TimeReportRow farm : farmList)
                  if (day.getFarmID().equals(farm.getFarmID()))
                     farm.addWeight(day.getWeight()); // adds weight to rows

            monthlyTable.getItems().addAll(farmList);

            // calculates stats
            int sumWeights = 0;
            TimeReportRow minRow = null;
            TimeReportRow maxRow = null;
            int minWeight = Integer.MAX_VALUE;
            int maxWeight = -1;

            // goes through every row
            for (TimeReportRow row : monthlyTable.getItems()) {
               int weight = row.getTotalWeight();
               sumWeights += weight; // finds total

               if (weight < minWeight) { // finds min
                  minRow = row;
                  minWeight = weight;
               }

               if (weight > maxWeight) { // finds max
                  maxRow = row;
                  maxWeight = weight;
               }
            }

            // sets text on stats fields
            idMin.setText(minRow.getFarmID());
            timeWeightMin.setText(String.valueOf(minRow.getTotalWeight()));
            timePercentMin.setText(String.valueOf(minRow.getPercentTotal()));
            idMax.setText(maxRow.getFarmID());
            timeWeightMax.setText(String.valueOf(maxRow.getTotalWeight()));
            timePercentMax.setText(String.valueOf(maxRow.getPercentTotal()));
            timeWeightAvg.setText(
                  String.valueOf(sumWeights / monthlyTable.getItems().size()));
         }
      });

      // creates hbox for top options
      HBox monthlyBox = new HBox(10, yearMonthlyLabel, monthlyReportYears,
            monthMonthlyLabel, monthlyReportMonths, monthlyEnter);
      monthlyBox.setAlignment(Pos.CENTER);

      // creates and formats grid of stats
      GridPane monthlyStats = new GridPane();
      Label monthlyMin = new Label("Min:");
      Label monthlyMax = new Label("Max:");
      Label monthlyAvg = new Label("Avg:");
      monthlyStats.addColumn(0, monthlyMin, monthlyMax, monthlyAvg);
      monthlyStats.addColumn(1, idMin, idMax);
      monthlyStats.addColumn(2, timeWeightMin, timeWeightMax, timeWeightAvg);
      monthlyStats.addColumn(3, timePercentMin, timePercentMax);
      monthlyStats.setHgap(5);
      monthlyStats.setVgap(5);
      monthlyStats.setAlignment(Pos.CENTER);

      // creates and formats monthly report pane
      VBox monthlyReportPane =
            new VBox(10, monthlyBox, monthlyTable, monthlyStats);
      monthlyReportPane.setPadding(new Insets(10, 10, 10, 10));
      monthlyReportPane.setAlignment(Pos.CENTER);

      return monthlyReportPane;
   }


   /**
    * Creates the pane for the Reports tab including all information and
    * controls.
    * 
    * @return The pane for the Reports tab.
    */
   private TabPane createReportsPane() {
      // creates report tabs
      Tab farmTab = new Tab("Farm Report");
      Tab annualTab = new Tab("Annual Report");
      Tab monthlyTab = new Tab("Montly Report");
      Tab rangeTab = new Tab("Date Range Report");

      // sets the panes for each tab
      farmTab.setContent(createFarmReportPane());
      annualTab.setContent(createAnnualReportPane());
      monthlyTab.setContent(createMonthlyReportPane());
      
      // sets contents for date range report
      Text rangeText = new Text("The date range report is currently "
            + "unimplemented.\nSorry for the inconvenience.");
      rangeText.setFont(header);
      rangeText.setTextAlignment(TextAlignment.CENTER);
      BorderPane rangePane = new BorderPane(rangeText);
      rangeTab.setContent(rangePane);

      // adds tabs to the TabPane
      TabPane reportPane =
            new TabPane(farmTab, annualTab, monthlyTab, rangeTab);
      reportPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

      return reportPane;
   }


   /**
    * Adds new data from a CSV file.
    * 
    * @param file The file to get data from.
    */
   private void addFile(File file) {
      String name = file.getName(); // gets year and month from file name
      name = name.substring(0, name.lastIndexOf(".")); // removes ".csv"

      String[] yearMonth = name.split("-"); // splits date
      int year = Integer.parseInt(yearMonth[0]);

      // determines if it should add or create a year
      if (yearList == null) { // no data yet
         addYear(file, yearMonth);

      } else {
         YearData yearData = getYear(year); // checks if year already exists

         if (yearData == null) { // create new year
            addYear(file, yearMonth);

         } else { // add to existing year
            yearData.addMonthData(file, yearMonth[1]);
         }
      }
   }


   /**
    * Creates the pane for the Input Files tab including all information and
    * controls.
    * 
    * @return The pane for the Input Files tab.
    */
   private Pane createInputFilesPane() {
      // creates a FileChooser for selecting a file in the OS file manager
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Select CSV Files");
      fileChooser.getExtensionFilters() // filters files to only CSV
            .addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

      // creates controls for pane
      Label fileLabel = new Label("Selected Files:");
      fileLabel.setFont(header);
      TextField fileField = new TextField();
      fileField.setPrefWidth(290);
      fileField.setEditable(false);

      // a button that opens the FileChooser dialog
      Button fileButton = new Button("Select Files");
      fileButton.setOnAction(a -> { // stores files
         List<File> tempFiles = fileChooser.showOpenMultipleDialog(mainStage);

         if (tempFiles != null)
            inputFiles = tempFiles;

         if (inputFiles != null) {
            String fileNames = ""; // stores all file names

            for (File file : inputFiles) { // gets all file names
               String name = file.getName();
               name = name.substring(0, name.lastIndexOf(".")); // removes .csv
               fileNames = fileNames.concat(name + ", ");
            }

            // removes comma and space from end of string
            fileNames = fileNames.substring(0, fileNames.lastIndexOf(","));
            fileField.setText(fileNames); // displays file names
         }
      });

      // creates confirmation dialog and adds files
      Button addButton = new Button("Add Files");
      addButton.setOnAction(a -> {
         if (inputFiles != null) { // makes sure there's files to add

            // creates confirmation dialog
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setHeaderText("Save File Confirmation");
            alert.setContentText("Saving new files will overwrite a previous "
                  + "day if the date and farm id are the same. Would you still"
                  + " like to continue?");
            alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE,
                  Region.USE_PREF_SIZE);

            // creates buttons for dialog
            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No", ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(yesButton, noButton);

            // handles when a button is pressed
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == noButton) { // cancel file save
               alert.close();
               return;
            }

            if (inputFiles != null) { // adds files and updates
               for (File file : inputFiles) // adds all files
                  addFile(file);

               inputFiles = null;
               fileField.setText("Files Saved");
               updateComboBoxes();
               displayMainTableData();
            }
         }
      });

      // creates and formats GridPane
      GridPane inputFilePane = new GridPane();
      inputFilePane.add(fileButton, 0, 0);
      inputFilePane.addRow(1, fileLabel, fileField, addButton);
      inputFilePane.setHgap(10);
      inputFilePane.setVgap(10);
      inputFilePane.setPadding(new Insets(40, 10, 10, 10));
      inputFilePane.setAlignment(Pos.TOP_CENTER);

      return inputFilePane;
   }


   /**
    * Creates the GUI and displays it to the user.
    * 
    * @param pStage The primary stage to display the GUI window in.
    */
   @Override
   public void start(Stage mainStage) throws Exception {
      // initializes variables
      this.mainStage = mainStage;
      header = new Font(14);
      comboWidth = 100;
      inputFiles = null;
      double fieldWidth = 100;

      // creates tabs
      Tab homeTab = new Tab("   Home   ");
      Tab tableTab = new Tab("   Table   ");
      Tab reportsTab = new Tab("  Reports  ");
      Tab inputFilesTab = new Tab("Input Files");

      // creates controls stored as fields
      createComboBoxes();
      createTextFields(fieldWidth);

      // sets the panes for each tab
      homeTab.setContent(createHomePane());
      tableTab.setContent(createMainTablePane());
      reportsTab.setContent(createReportsPane());
      inputFilesTab.setContent(createInputFilesPane());

      // adds the tabs to the TabPane
      TabPane tabPane =
            new TabPane(homeTab, tableTab, reportsTab, inputFilesTab);
      tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

      // formats the stage and shows it to the user
      Scene scene = new Scene(tabPane);
      mainStage.setScene(scene);
      mainStage.setTitle(" Milk Weights GUI");

      try { // tries to set icon in title bar
         mainStage.getIcons().add(new Image("logo.png"));
      } catch (Exception e) {
      }

      mainStage.show();
   }


   /**
    * Starts the program.
    * 
    * @param args The general arguments when running the program.
    */
   public static void main(String[] args) {
      launch(args);
   }


}
