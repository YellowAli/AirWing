package datastructure.airplaneapplication;

import javafx.event.EventHandler;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class IntermediateRegisterController implements Initializable
{
    @FXML
    private Label airlineLabel;

    @FXML
    private AnchorPane root;

    @FXML
    private Label travellerLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        root.setStyle("-fx-background-color: #ffffff");

        airlineLabel.setOnMouseEntered(mouseEvent -> hover(airlineLabel));
        travellerLabel.setOnMouseEntered(mouseEvent -> hover(travellerLabel));

        airlineLabel.setOnMouseExited(mouseEvent -> exitHover(airlineLabel));
        travellerLabel.setOnMouseExited(mouseEvent -> exitHover(travellerLabel));

        airlineLabel.setOnMouseClicked(mouseEvent -> {
            try {
                onClick(airlineLabel);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        travellerLabel.setOnMouseClicked(mouseEvent -> {
            try {
                onClick(travellerLabel);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


    }

    public void hover(Label label)
    {
            label.setTextFill(Color.web("#ae3535"));
    }

    public void exitHover(Label label)
    {
        label.setTextFill(Color.web("#000000"));
    }

    public void onClick(Label label) throws IOException
    {
        String refrence = "Register-view.fxml";

        FXMLLoader fxmlLoader = new FXMLLoader(AirWingApplication.class.getResource(refrence));
        Stage stage= new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        Stage thisStage = (Stage) root.getScene().getWindow();
        thisStage.close();
        stage.show();

        RegisterController controller = (RegisterController) fxmlLoader.getController();

        if(airlineLabel == label)
        {
            controller.setPersonRegistering("airline-");
        }
        else
        {
            controller.setPersonRegistering("traveller-");
        }

    }




}
