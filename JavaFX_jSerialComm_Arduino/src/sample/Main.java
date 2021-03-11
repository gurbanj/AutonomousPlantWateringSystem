package sample;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import sample.SerialPortService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        var sp = SerialPortService.getSerialPort("/dev/cu.usbserial-0001");
        var outputStream = sp.getOutputStream();

        var pane = new BorderPane();
        var label = new Label();
        var button = new Button("MOSFET Button");
        var slider = new Slider();
        slider.setMin(0.0);
        slider.setMax(100.0);

        //mouse pressed
        button.setOnMousePressed(value -> {
            try {
                outputStream.write(255);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });
        //mouse released
        button.setOnMouseReleased(value -> {
            try {
                outputStream.write(0);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });

        label.textProperty().bind(slider.valueProperty().asString("Value is %.0f "));

            // TODO: Add a 'listener' to the {@code valueProperty} of the slider. The listener
        //  should write the {@code byteValue()} of the new slider value to the output stream.

        slider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                outputStream.write(newValue.byteValue());
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });

        pane.setBottom(label);
        pane.setTop(button);
        pane.setCenter(slider);
        pane.setPadding(new Insets(0, 20, 0, 20));

        var scene = new Scene(pane, 400, 200);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}