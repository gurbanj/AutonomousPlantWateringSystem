package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.FormatStringConverter;

import java.text.DateFormat;
import java.util.List;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static TableView<XYChart.Data<Number, Number>> getTableView() {
        // TODO: Create a `TableView<XYChart.Data<Number, Number>>`.
        //  Refer to the documentation for further details.

        //step 1
        var table = new TableView<XYChart.Data<Number, Number>>();

        //step 2
        var timeColumn = new TableColumn<XYChart.Data<Number, Number>, Number>("Time");

        //step 3
        timeColumn.setCellValueFactory(row -> row.getValue().XValueProperty());

        //step 4
        var dateFormat = DateFormat.getTimeInstance();

        //step 5
        var converter = new FormatStringConverter<Number>(dateFormat);

        //step 6
        timeColumn.setCellFactory(column -> new TextFieldTableCell<>(converter));

        //step 7: repeat steps(2) & (3)
        var valueColumn = new TableColumn<XYChart.Data<Number, Number>, Number>("Value");
        valueColumn.setCellValueFactory(row -> row.getValue().YValueProperty());

        //step 8
        table.getColumns().setAll(List.of(timeColumn, valueColumn));

        //step 9
        return table;
    }

    @Override
    public void start(Stage primaryStage) {
        var serialPort = SerialPortService.getSerialPort("/dev/cu.usbserial-0001");
        var table = getTableView();
        var controller = new DataController();

        serialPort.addDataListener(controller);
        table.setItems(controller.getDataPoints());

        var vbox = new VBox(table);
        var scene = new Scene(vbox);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
