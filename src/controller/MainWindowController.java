package controller;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Track;
import util.KmlLineExporter;
import util.GpxLoader;

public class MainWindowController {
	
	//Views
	@FXML private Button openGpxButton;
	@FXML private ImageView trackGpxImage;
	@FXML private Label waypointCounterGpxLabel;
	@FXML private Button saveKmlButton;
	
	public Track trackGpx;
	
	private Main main;
	private Stage primaryStage;
	
	public void setMain(Main main) {
		this.main = main;
		this.primaryStage = this.main.getPrimaryStage();
		this.primaryStage.setTitle("GPX2KML");
		this.trackGpx = null;
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
				Image image = new Image(getClass().getClassLoader().getResource("ok.png").toString(), true);
				trackGpxImage.setImage(image);
				this.waypointCounterGpxLabel.setText(Integer.toString(this.trackGpx.getWaypoints().size()));
				this.waypointCounterGpxLabel.setTextFill(Color.web("#608b32"));
				this.saveKmlButton.setDisable(false);
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

}