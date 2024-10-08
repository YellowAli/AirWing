package datastructure.airplaneapplication;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AlertController implements Initializable
{
    @FXML
    private Label error;
    private String errorMessage;

    public void setErrorMessage(String errors)
    {
        this.errorMessage = errors;
        error.setText(errorMessage);
    }

    @FXML
    private AnchorPane root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        root.setStyle("-fx-background-color:  #FDFBF7 ");

    }
}
