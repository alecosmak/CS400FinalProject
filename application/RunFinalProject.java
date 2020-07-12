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
import java.util.ArrayList;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * 
 * 
 * @author Alec Osmak
 */
public class RunFinalProject extends Application {

   private ArrayList<YearData> yearList;
   private Stage mainStage;
   private File inputFile;


   private void addFile(File file) {

   }


   private void addYear() {

   }


   private YearData getYear(int year) {
      return yearList.get(year);
   }


   /**
    * 
    * 
    * @return
    */
   private Node createHomePane() {

      Text homeText = new Text(
            "Welcome! Please select a tab to access milk weight data.");
      VBox pane = new VBox(homeText);

      FileInputStream input;

      try {
         input = new FileInputStream("cheeseLogo.jpg");
         Image image = new Image(input, Region.USE_PREF_SIZE, 200, true, true);
         ImageView imageView = new ImageView(image);
         pane.getChildren().add(imageView);
      } catch (FileNotFoundException e) {
         Alert alert = new Alert(AlertType.INFORMATION,
               "Home image could not be found. Everything else will still "
                     + "function correctly.");
         alert.setResizable(true);
         alert.setHeaderText("Problem Loading");
         alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
         alert.showAndWait();
      }

      return pane;
   }


   /**
    * 
    * 
    * @return
    */
   private Node createTablesPane() {
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


      TableView<FarmDay> table = new TableView<>();


      TableColumn<FarmDay, Integer> dateCol = new TableColumn<>("Date");
      TableColumn<FarmDay, String> farmCol = new TableColumn<>("Farm ID");
      TableColumn<FarmDay, Integer> weightCol = new TableColumn<>("Weight");

      farmCol.setCellValueFactory(
            p -> new ReadOnlyObjectWrapper<>(p.getValue().getFarmID()));

      table.getColumns().addAll(dateCol, farmCol, weightCol);


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

      BorderPane pane =
            new BorderPane(table, topOptions, rightOptions, null, null);
      return pane;
   }


   /**
    * 
    * 
    * @return
    */
   private Node createGraphsPane() {
      NumberAxis xAxis = new NumberAxis();
      xAxis.setLabel("Day");

      NumberAxis yAxis = new NumberAxis();
      yAxis.setLabel("Weight");
      LineChart chart = new LineChart(xAxis, yAxis);
      XYChart.Series dataSeries1 = new XYChart.Series();
      dataSeries1.setName("2014");

      dataSeries1.getData().add(new XYChart.Data( 1, 567));
      dataSeries1.getData().add(new XYChart.Data( 5, 612));
      dataSeries1.getData().add(new XYChart.Data(10, 800));
      dataSeries1.getData().add(new XYChart.Data(20, 780));
      dataSeries1.getData().add(new XYChart.Data(40, 810));
      dataSeries1.getData().add(new XYChart.Data(80, 850));

      chart.getData().add(dataSeries1);
      
      ComboBox<String> years = new ComboBox<>();
      years.setEditable(true);
      years.getItems().addAll("2019", "2020");

      ComboBox<String> months = new ComboBox<>();
      months.setEditable(true);
      months.getItems().addAll("jan", "feb");

      HBox topOptions = new HBox(years, months);

      Label farms = new Label("Farm IDs:");

      VBox rightOptions = new VBox(farms);
      for (int i = 0; i < 4; i++) {
         rightOptions.getChildren().add(new CheckBox("Farm " + i));
      }

      BorderPane pane =
            new BorderPane(chart, topOptions, rightOptions, null, null);
      return pane;
   }


   /**
    * 
    * 
    * @return
    */
   private Node createInputFilesPane() {

      Button fileButton = new Button("Select File");
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Select File");
      fileChooser.getExtensionFilters()
            .addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

      Label fileLabel = new Label("Selected File: ");
      TextField fileField = new TextField();
      fileField.setEditable(false);

      Button saveButton = new Button("Save File");

      fileButton.setOnAction(a -> {
         inputFile = fileChooser.showOpenDialog(mainStage);
         if (inputFile != null)
            fileField.setText(inputFile.getName());
      });

      saveButton.setOnAction(a -> {
         if (inputFile != null) {
            addFile(inputFile);
            fileField.setText(null);
         }
      });

      HBox bottom = new HBox(fileLabel, fileField, saveButton);

      VBox pane = new VBox(fileButton, bottom);
      return pane;
   }


   /**
    * 
    * 
    * @return
    */
   private Node createReportsPane() {

      ComboBox<String> farms = new ComboBox<>();
      farms.setEditable(true);
      farms.getItems().addAll("farm 1", "farm 2");

      Label farmTotalLabel = new Label("Farm Total:");
      Label farmPercentLabel = new Label("Farm Percent:");

      TextField farmTotalField = new TextField();
      farmTotalField.setEditable(false);
      TextField farmPercentField = new TextField();
      farmPercentField.setEditable(false);

      HBox farmData = new HBox(farms, farmTotalLabel, farmTotalField,
            farmPercentLabel, farmPercentField);


      ComboBox<String> years = new ComboBox<>();
      years.setEditable(true);
      years.getItems().addAll("2019", "2020");

      Label yearTotalLabel = new Label("Year Total:");
      Label yearPercentLabel = new Label("Year Percent:");

      TextField yearTotalField = new TextField();
      yearTotalField.setEditable(false);
      TextField yearPercentField = new TextField();
      yearPercentField.setEditable(false);
      HBox yearData = new HBox(years, yearTotalLabel, yearTotalField,
            yearPercentLabel, yearPercentField);


      ComboBox<String> months = new ComboBox<>();
      months.setEditable(true);
      months.getItems().addAll("jan", "feb");

      Label monthTotalLabel = new Label("Month Total:");
      Label monthPercentLabel = new Label("Month Percent:");

      TextField monthTotalField = new TextField();
      monthTotalField.setEditable(false);
      TextField monthPercentField = new TextField();
      monthPercentField.setEditable(false);
      HBox monthData = new HBox(months, monthTotalLabel, monthTotalField,
            monthPercentLabel, monthPercentField);


      VBox pane = new VBox(farmData, yearData, monthData);
      return pane;
   }


   /**
    * 
    * 
    * @param pStage The primary stage to display the GUI window.
    */
   @Override
   public void start(Stage mainStage) throws Exception {
      this.mainStage = mainStage;

      // creates all the tabs
      Tab homeTab = new Tab("Home");
      Tab tablesTab = new Tab("Tables");
      Tab graphsTab = new Tab("Graphs");
      Tab inputFilesTab = new Tab("Input Files");
      Tab reportsTab = new Tab("Reports");

      // adds the elements to each tab
      homeTab.setContent(createHomePane());
      tablesTab.setContent(createTablesPane());
      graphsTab.setContent(createGraphsPane());
      inputFilesTab.setContent(createInputFilesPane());
      reportsTab.setContent(createReportsPane());

      // adds the individual tabs to a TabPane
      TabPane tabPane = new TabPane(homeTab, tablesTab, graphsTab,
            inputFilesTab, reportsTab);
      tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

      // formats stage and shows it to the user
      Scene scene = new Scene(tabPane);
      mainStage.setScene(scene);
      mainStage.setTitle("Milk Weights GUI");
      mainStage.show();
   }


   /**
    * 
    * 
    * @param args
    */
   public static void main(String[] args) {
      launch(args);
   }


}
