package datastructure.airplaneapplication;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AirlineMainPageController implements Initializable,MainPage
{

    @FXML
    private MenuItem Plane;

    @FXML
    private MenuItem Flights;

    private String refID;



    @FXML
    private AnchorPane root;
    public void onClick(ActionEvent ev) throws IOException, SQLException {
        MenuItem event = (MenuItem) ev.getSource();
        FXMLLoader fxmlLoader = new FXMLLoader(AirWingApplication.class.getResource(event.getId()+"-view.fxml"));
        Stage stage= new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        if(event.getId().equals("Flights") && event.getText().equals("Modify/View Flights"))
        {
            ((FlightsController)fxmlLoader.getController()).setTab();
        }
        ((MainTabs)fxmlLoader.getController()).setRefID(refID);
        Stage thiss = (Stage)root.getScene().getWindow();
        thiss.close();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Plane.setOnAction(event -> {
            try {
                onClick(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        Flights.setOnAction(event -> {
            try {
                onClick(event);
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }


    @Override
    public void setRefID(String RefID)
    {
        this.refID = RefID;
    }




}
