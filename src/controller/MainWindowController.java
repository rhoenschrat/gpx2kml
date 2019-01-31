package controller;

import java.io.File;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Track;
import util.KmlLineExporter;
import util.ChoiceBoxKVPairs;
import util.GpxLoader;

public class MainWindowController {
	
	//Views
	@FXML private Button openGpxButton;
	@FXML private ImageView trackGpxImage;
	@FXML private Label waypointCounterGpxLabel;
	@FXML private Button saveKmlButton;
	@FXML private TextField trackTitleTextField;
	@FXML private ColorPicker lineColorPicker;
	@FXML private TextField lineWidthTextField;
	@FXML private ColorPicker polyColorPicker;
	@FXML private ChoiceBox<ChoiceBoxKVPairs> altitudeModeChoiceBox;
	@FXML private TextField altitudeTextField;
	@FXML private CheckBox extrudeCheckBox;
	
	public Track trackGpx;
	
	private Main main;
	private Stage primaryStage;
	
	public void setMain(Main main) {
		this.main = main;
		this.primaryStage = this.main.getPrimaryStage();
		this.primaryStage.setTitle("GPX2KML");
		this.trackGpx = null;
	
		this.trackTitleTextField.textProperty().addListener((obs, oldText, newText) -> {
		    this.trackGpx.setTitle(newText);
			System.out.println("Title changed to " + newText);
		});

		this.altitudeTextField.textProperty().addListener((obs, oldText, newText) -> {
		    this.trackGpx.setAltitude(new Integer(newText));
			System.out.println("Altitude changed to " + newText);
		});
		
		ChoiceBoxKVPairs selectedMode = new ChoiceBoxKVPairs(Track.Behavior.RELATIVE, "Relative to ground");
		this.altitudeModeChoiceBox.getItems().add(new ChoiceBoxKVPairs(Track.Behavior.TRACK, "From GPX track"));
		this.altitudeModeChoiceBox.getItems().add(new ChoiceBoxKVPairs(Track.Behavior.GROUND, "Ground"));
		this.altitudeModeChoiceBox.getItems().add(selectedMode);
		this.altitudeModeChoiceBox.getItems().add(new ChoiceBoxKVPairs(Track.Behavior.ABSOLUTE, "Absolute"));
		this.altitudeModeChoiceBox.setValue(selectedMode);
		
		this.altitudeModeChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
			       (ObservableValue<? extends Number> ov, Number oldVal, Number newVal) -> {
			            	   System.out.println("Altitude mode has changed to " + newVal);
			            	   switch (newVal.intValue()) {
			            	   	case 0:
			            	   		this.trackGpx.setBehavior(Track.Behavior.TRACK);
			            	   		this.extrudeCheckBox.setDisable(false);
			            	   		this.altitudeTextField.setDisable(true);
			            	   		break;
			            	   	case 1:
			            	   		this.trackGpx.setBehavior(Track.Behavior.GROUND);
			            	   		this.extrudeCheckBox.setDisable(true);
			            	   		this.altitudeTextField.setDisable(true);
			            	   		break;
			            	   	case 2:
			            	   		this.trackGpx.setBehavior(Track.Behavior.RELATIVE);
			            	   		this.extrudeCheckBox.setDisable(false);
			            	   		this.altitudeTextField.setDisable(false);
			            	   		break;
			            	   	case 3:
			            	   		this.trackGpx.setBehavior(Track.Behavior.ABSOLUTE);
			            	   		this.extrudeCheckBox.setDisable(false);
			            	   		this.altitudeTextField.setDisable(false);
			            	   		break;
			            	   }
			       });
		

		this.lineWidthTextField.textProperty().addListener((obs, oldText, newText) -> {
		    this.trackGpx.setLineWidth(new Integer(newText));
			System.out.println("Line width changed to " + newText);
		});
	}
	
	@FXML public void handleOpenGpxButton() {
		System.out.println("Open button clicked");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open GPX file");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("GPX", "*.gpx"));
		File file = fileChooser.showOpenDialog(primaryStage);
		if (file != null) {
			this.trackGpx = new Track(file);
			this.trackGpx.setWaypoints(GpxLoader.loadWaypoints(this.trackGpx.getFile()));
			if (this.trackGpx.getWaypoints() != null) {
				this.trackGpx.setTitle(trackGpx.getFile().getName());
				Image image = new Image(getClass().getClassLoader().getResource("ok.png").toString(), true);
				trackGpxImage.setImage(image);
				this.waypointCounterGpxLabel.setText(Integer.toString(this.trackGpx.getWaypoints().size()));
				this.waypointCounterGpxLabel.setTextFill(Color.web("#608b32"));
				this.saveKmlButton.setDisable(false);
				
				this.trackTitleTextField.setText(this.trackGpx.getTitle());
				this.trackTitleTextField.setDisable(false);
				this.lineColorPicker.setValue(this.trackGpx.getLineColor());
				this.lineColorPicker.setDisable(false);
				this.lineWidthTextField.setText(this.trackGpx.getLineWidth().toString());
				this.lineWidthTextField.setDisable(false);
				this.polyColorPicker.setValue(this.trackGpx.getPolyColor());
				this.polyColorPicker.setDisable(false);
				this.altitudeTextField.setText(this.trackGpx.getAltitude().toString());
				this.altitudeTextField.setDisable(false);
				this.extrudeCheckBox.setSelected(this.trackGpx.getExtrude());
				this.extrudeCheckBox.setDisable(false);
				this.altitudeModeChoiceBox.setDisable(false);
				
			}
			else {
				Image image = new Image(getClass().getClassLoader().getResource("error.png").toString(), true);
				trackGpxImage.setImage(image);
			}
		};
	}

	@FXML public void handleSaveKmlButton() {
		System.out.println("Save KML button clicked");
		String targetFilename = trackGpx.getFile().getName();
		int pos = targetFilename.lastIndexOf(".");
		if (pos > 0) {
		    targetFilename = targetFilename.substring(0, pos);
		}
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save KML file");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("KML", "*.kml"));
		fileChooser.setInitialFileName(targetFilename + ".kml");
		File file = fileChooser.showSaveDialog(primaryStage);
		if (file != null) {
			KmlLineExporter.saveRoute(file, this.trackGpx);
		};
	}
	
	@FXML public void handleExtrudeCheckbox() {
		System.out.println("Set extrude to " + this.extrudeCheckBox.isSelected());
		this.trackGpx.setExtrude(this.extrudeCheckBox.isSelected());
	}
	
	@FXML public void handleTitleTextField() {
		System.out.println("Title field changed to " + this.trackTitleTextField.getText());
		this.trackGpx.setTitle(this.trackTitleTextField.getText());
	}

	@FXML public void handleLineColorPicker() {
		System.out.println("New line color " + this.lineColorPicker.getValue().toString());
		this.trackGpx.setLineColor(this.lineColorPicker.getValue());
	}
	
	@FXML public void handleLineWidthTextField() {
		System.out.println("New line width " + this.lineWidthTextField.getText());
		this.trackGpx.setLineWidth(new Integer(this.lineWidthTextField.getText()));
	}

	@FXML public void handlePolyColorPicker() {
		System.out.println("New poly color " + this.polyColorPicker.getValue().toString());
		this.trackGpx.setPolyColor(this.polyColorPicker.getValue());
	}

	@FXML public void handleAltitudeTextField() {
		System.out.println("Altitude field changed to " + this.altitudeTextField.getText());
		this.trackGpx.setAltitude(new Integer(this.altitudeTextField.getText()));
	}
	
	@FXML public void handleAltitudeModeChoiceBox() {
		System.out.println("Altitude mode changed to " + this.altitudeModeChoiceBox.getValue().getKey());
		
	}
	
	
}