/**
 * Project: Milk Weights Final Project
 * Files: RunFinalProject.java
 * 
 * Description: This is the final project for CS 400 Summer 2020. This program
 * is an interactive data visualizer that utilizes a GUI to display the data.
 * Through the GUI the user be able to add, copy, and change data.
 * 
 * http://tutorials.jenkov.com/javafx/
 * 
 * Author: Alec Osmak
 * Email: osmak@wisc.edu
 */

package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * 
 * 
 * @author Alec Osmak
 */
public class RunFinalProject extends Application {

   private ObservableList<YearData> yearList;
   private Stage mainStage;
   private File inputFile;
   private Font header = new Font(14);
   private TableView<DayData> table;
   private LineChart<Number, Number> chart;
   private VBox rightOptions;


   private void addYear(File file, int year) {
      if (yearList == null)
         yearList = FXCollections.observableArrayList();

      yearList.add(new YearData(file, year));
   }


   private YearData getYear(int year) {
      for (YearData yearData : yearList) {
         if (yearData.getYear() == year)
            return yearData;
      }

      return null;
   }


   /**
    * Creates the pane for the Home tab. This pane contains all the information
    * and controls for that tab.
    * 
    * @return The pane for the home tab.
    */
   private Pane createHomePane() {
      BorderPane homePane = new BorderPane();

      // the text to display on the home tab
      String homeString = "Welcome to the Milk Weights GUI!  Please select a "
            + "tab to access milk weight data.\n - Tables displays a data table"
            + " of all current data\n - Charts shows a chart of weight sold for"
            + " each day\n - Input Files allows you to add more data from a CSV"
            + " file\n - Reports shows general weight summaries";

      // adds and formats the text for the pane
      Text homeText = new Text(homeString);
      homeText.setFont(new Font(18));
      homePane.setCenter(homeText);
      BorderPane.setAlignment(homeText, Pos.CENTER);

      // vbox for the bottom elements
      VBox bottom = new VBox();
      bottom.setAlignment(Pos.CENTER);
      bottom.setPadding(new Insets(0, 0, 2, 0));


      try { // tries to load in an image to the home tab
         FileInputStream inputImage = new FileInputStream("cheeseLogo.jpg");
         Image image =
               new Image(inputImage, Region.USE_PREF_SIZE, 200, true, true);
         ImageView imageView = new ImageView(image);
         bottom.getChildren().add(imageView);

      } catch (FileNotFoundException e) {
         // popup window for if the home page image cannot be found
         Alert alert = new Alert(AlertType.INFORMATION,
               "Home image could not be found. Everything else will still "
                     + "function correctly.");
         alert.setHeaderText("Problem Loading");
         alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE,
               Region.USE_PREF_SIZE);
         alert.showAndWait();
      }

      Text infoText = new Text("Designed by Alec Osmak for a final project.  "
            + "UW-Madison CS400 Summer 2020");
      bottom.getChildren().add(infoText);
      homePane.setBottom(bottom);

      return homePane;
   }


   @SuppressWarnings("unchecked")
   private TableView<DayData> createTable() {

      table = new TableView<>();

      TableColumn<DayData, Integer> dateCol = new TableColumn<>("Date");
      TableColumn<DayData, String> farmCol = new TableColumn<>("Farm ID");
      TableColumn<DayData, Integer> weightCol = new TableColumn<>("Weight");

      dateCol.setCellValueFactory(new PropertyValueFactory<>("day"));
      farmCol.setCellValueFactory(new PropertyValueFactory<>("farmID"));
      weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));

      table.getColumns().addAll(dateCol, farmCol, weightCol);

      return table;
   }


   /**
    * Creates the pane for the Tables tab. This pane contains all the
    * information and controls for that tab.
    * 
    * @return The pane for the Tables tab.
    */
   private Pane createTablesPane() {
      GridPane topOptions = new GridPane();

      // creates labels for options
      Label farmLabel = new Label("Farm ID:");
      Label yearLabel = new Label("Year:");
      Label monthLabel = new Label("Month:");

      // creates drop down boxes for options
      ComboBox<String> farms = new ComboBox<>();
      farms.setEditable(true);
      farms.getItems().addAll("farm 1", "farm 2");

      ComboBox<String> years = new ComboBox<>();
      years.setEditable(true);
      years.getItems().addAll("2019", "2020");

      ComboBox<String> months = new ComboBox<>();
      months.setEditable(true);
      months.getItems().addAll("jan", "feb");

      // determines what happens when the options are chose
      farms.setOnAction(e -> farmLabel.setText("Farm ID: " + farms.getValue()));
      years.setOnAction(e -> yearLabel.setText("Year: " + years.getValue()));
      months.setOnAction(
            e -> monthLabel.setText("Month: " + months.getValue()));

      // adding options to top
      topOptions.addRow(0, farmLabel, yearLabel, monthLabel);
      topOptions.addRow(1, farms, years, months);

      VBox rightOptions = new VBox();
      Button downloadButton = new Button("Download Report");
      Label editLabel = new Label("Edit Fields:");

      RadioButton onButton = new RadioButton("On");
      RadioButton offButton = new RadioButton("Off");
      offButton.setSelected(true);

      ToggleGroup radioGroup = new ToggleGroup();
      onButton.setToggleGroup(radioGroup);
      offButton.setToggleGroup(radioGroup);

      rightOptions.getChildren().addAll(downloadButton, editLabel, onButton,
            offButton);

      BorderPane tablesPane =
            new BorderPane(createTable(), topOptions, rightOptions, null, null);
      tablesPane.setPadding(new Insets(10, 10, 10, 10));

      return tablesPane;
   }


   /**
    * 
    * 
    * @return
    */
   private LineChart<Number, Number> createChart() {
      NumberAxis xAxis = new NumberAxis();
      xAxis.setLabel("Day");

      NumberAxis yAxis = new NumberAxis();
      yAxis.setLabel("Weight");

      chart = new LineChart<>(xAxis, yAxis);

      return chart;
   }


   private void addSeries(String farmID) {
      XYChart.Series<Number, Number> series = new XYChart.Series<>();
      series.setName(farmID);


      for (DayData dayData : table.getItems()) {
         series.getData().add(new XYChart.Data<Number, Number>(dayData.getDay(),
               dayData.getWeight()));
      }

      chart.getData().add(series);
   }


   /**
    * Creates the pane for the Charts tab. This pane contains all the
    * information and controls for that tab.
    * 
    * @return The pane for the Charts tab.
    */
   private Pane createChartsPane() {
      // Label and ComboBox for year input
      Label yearLabel = new Label("Year:");
      yearLabel.setFont(header);
      ComboBox<String> years = new ComboBox<>();
      years.setEditable(true);
      years.getItems().addAll("2019", "2020");
      HBox yearBox = new HBox(yearLabel, years);
      yearBox.setSpacing(5);

      // Label and ComboBox for month input
      Label monthLabel = new Label("Month:");
      monthLabel.setFont(header);
      ComboBox<String> months = new ComboBox<>();
      months.setEditable(true);
      months.getItems().addAll("jan", "feb");
      HBox monthBox = new HBox(monthLabel, months);
      monthBox.setSpacing(5);

      HBox topOptions = new HBox(yearBox, monthBox);
      topOptions.setSpacing(20);

      // Label and CheckBoxes for farm input
      Label farms = new Label("Farm IDs:");
      farms.setFont(header);
      rightOptions = new VBox(farms);
      // for (int i = 0; i < 4; i++) {
      // CheckBox box = new CheckBox("Farm " + i);
      // rightOptions.getChildren().add(box);
      // box.setOnAction(a -> {
      // if (box.isSelected())
      // addSeries(box.getText());
      // });
      // }

      

      rightOptions.setSpacing(5);

      BorderPane chartsPane =
            new BorderPane(createChart(), topOptions, rightOptions, null, null);
      chartsPane.setPadding(new Insets(10, 10, 10, 10));

      return chartsPane;
   }


   private void addFile(File file) {
      String name = file.getName();
      name = name.substring(0, name.lastIndexOf(".")); // removes .csv

      String[] splitName = name.split("-");

      int year = Integer.parseInt(splitName[0]);

      if (yearList == null) {
         addYear(file, year);

      } else {
         YearData yearData = getYear(year);

         if (yearData == null) {
            addYear(file, year);

         } else {
            yearData.addMonthData(file);
         }
      }

      table.setItems(yearList.get(0).getMonthList().get(0).getDayList());
      
      ObservableList<String> farms = FXCollections.observableArrayList();
      if (yearList != null) {
         for (YearData yearData : yearList) {
            if (yearData.getNumMonths() != 0) {
               for (MonthData month : yearData.getMonthList()) {
                  if (month.getNumDays() != 0) {
                     for (DayData dayData : month.getDayList()) {
                        
                        String farmID = dayData.getFarmID();
                        
                        if(!farms.contains(farmID)) {
                           
                           CheckBox box = new CheckBox(farmID);
                           box.setOnAction(a -> {
                              if (box.isSelected())
                                 addSeries(box.getText());
                           });
                           rightOptions.getChildren().add(box);
                           farms.add(farmID);
                        }
                        
                     }
                  }
               }
            }
         }
      }
   }


   /**
    * Creates the pane for the Input Files tab. This pane contains all the
    * information and controls for that tab.
    * 
    * @return The pane for the Input Files tab.
    */
   private Pane createInputFilesPane() {
      // creates a FileChooser for selecting a file with the OS's file manager
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Select CSV File");
      fileChooser.getExtensionFilters()
            .addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

      Label fileLabel = new Label("Selected File: ");
      fileLabel.setFont(header);
      TextField fileField = new TextField();
      fileField.setEditable(false);

      // a button that opens the FileChooser dialog
      Button fileButton = new Button("Select File");
      fileButton.setOnAction(a -> {
         inputFile = fileChooser.showOpenDialog(mainStage);
         if (inputFile != null) {
            fileField.setText(inputFile.getName());
         }
      });

      Button saveButton = new Button("Save File");

      // creates confirmation popup on saving file
      saveButton.setOnAction(a -> {
         if (inputFile != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setContentText("Saving a new file may overwrite previous data"
                  + ".  Would you still like to continue?");
            alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE,
                  Region.USE_PREF_SIZE);

            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No", ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(yesButton, noButton);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == noButton) {
               alert.close();
               fileField.setText("Canceled Save");
               inputFile = null;
            }

            if (inputFile != null) {
               addFile(inputFile);
               inputFile = null;
               fileField.setText("File Saved");
            }
         }
      });

      HBox bottom = new HBox(fileLabel, fileField, saveButton);
      bottom.setSpacing(10);

      VBox filesPane = new VBox(fileButton, bottom);
      filesPane.setSpacing(10);
      filesPane.setPadding(new Insets(25, 0, 0, 25));

      return filesPane;
   }


   /**
    * Creates the pane for the Reports tab. This pane contains all the
    * information and controls for that tab.
    * 
    * @return The pane for the Reports tab.
    */
   private Pane createReportsPane() {
      GridPane reportsPane = new GridPane();
      double controlWidth = 100;

      // creating and formatting all ComboBoxes
      ComboBox<String> farms = new ComboBox<>();
      farms.setEditable(true);
      farms.setPrefWidth(controlWidth + 10);
      farms.getItems().addAll("farm 1", "farm 2");

      ComboBox<String> years = new ComboBox<>();
      years.setEditable(true);
      years.setPrefWidth(controlWidth + 10);
      years.getItems().addAll("2019", "2020");

      ComboBox<String> months = new ComboBox<>();
      months.setEditable(true);
      months.setPrefWidth(controlWidth + 10);
      months.getItems().addAll("jan", "feb");

      // creating and formatting all Labels
      Label reportLabel = new Label("Report");
      reportLabel.setFont(header);
      Label selectLabel = new Label("Select");
      selectLabel.setFont(header);
      Label totalLabel = new Label("Total Weight");
      totalLabel.setFont(header);
      Label percentLabel = new Label("Percent of total");
      percentLabel.setFont(header);

      Label farmLabel = new Label("Farm:");
      Label yearLabel = new Label("Year:");
      Label monthLabel = new Label("Month:");

      // creating and formatting all TextFields
      TextField farmTotalField = new TextField();
      farmTotalField.setEditable(false);
      farmTotalField.setPrefWidth(controlWidth);
      TextField farmPercentField = new TextField();
      farmPercentField.setEditable(false);
      farmPercentField.setPrefWidth(controlWidth);

      TextField yearTotalField = new TextField();
      yearTotalField.setEditable(false);
      yearTotalField.setPrefWidth(controlWidth);
      TextField yearPercentField = new TextField();
      yearPercentField.setEditable(false);
      yearPercentField.setPrefWidth(controlWidth);

      TextField monthTotalField = new TextField();
      monthTotalField.setPrefWidth(controlWidth);
      monthTotalField.setEditable(false);
      TextField monthPercentField = new TextField();
      monthPercentField.setEditable(false);
      monthPercentField.setPrefWidth(controlWidth);

      // adding controls to GridPane
      reportsPane.addRow(0, reportLabel, selectLabel, totalLabel, percentLabel);
      reportsPane.addRow(1, farmLabel, farms, farmTotalField, farmPercentField);
      reportsPane.addRow(2, yearLabel, years, yearTotalField, yearPercentField);
      reportsPane.addRow(3, monthLabel, months, monthTotalField,
            monthPercentField);

      // formatting GridPane
      GridPane.setHalignment(selectLabel, HPos.CENTER);
      GridPane.setHalignment(totalLabel, HPos.CENTER);
      GridPane.setHalignment(percentLabel, HPos.CENTER);
      reportsPane.setPadding(new Insets(20, 10, 10, 10));
      reportsPane.setHgap(15);
      reportsPane.setVgap(10);

      return reportsPane;
   }


   /**
    * Creates the GUI and displays it to the user.
    * 
    * @param pStage The primary stage to display the GUI window.
    */
   @Override
   public void start(Stage mainStage) throws Exception {
      this.mainStage = mainStage;

      // creates all the tabs
      Tab homeTab = new Tab("Home");
      Tab tablesTab = new Tab("Tables");
      Tab chartsTab = new Tab("Charts");
      Tab inputFilesTab = new Tab("Input Files");
      Tab reportsTab = new Tab("Reports");

      // adds the panes to each tab
      homeTab.setContent(createHomePane());
      tablesTab.setContent(createTablesPane());
      chartsTab.setContent(createChartsPane());
      inputFilesTab.setContent(createInputFilesPane());
      reportsTab.setContent(createReportsPane());

      // adds the individual tabs to the TabPane
      TabPane tabPane = new TabPane(homeTab, tablesTab, chartsTab,
            inputFilesTab, reportsTab);
      tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

      // formats the stage and shows it to the user
      Scene scene = new Scene(tabPane);
      mainStage.setScene(scene);
      mainStage.setTitle("Milk Weights GUI");
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
