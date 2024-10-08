package datastructure.airplaneapplication;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class RegisterController implements Initializable
{
    @FXML
    private TextField AirlineName;
    private ObservableList<String> countryData;

    @FXML
    private ComboBox<String> DepartureFrom;

    @FXML
    private Button button;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private TextField confirmation;

    @FXML
    private ImageView image;

    @FXML
    private PasswordField newPassword;

    @FXML
    private TextField newUserName;

    @FXML
    private AnchorPane root;

    private GeneralAdapter adapter;
    private String tableName = "Account";

    private String personRegistering;
    private String error;
    private boolean issue = false;


    public RegisterController() throws SQLException
    {
        String[] info= {"ReferenceID VARCHAR(30)","Username VARCHAR(30)","Passwords VARCHAR(200)","Email VARCHAR(40)","DepartureLoco Varchar(1000)"};
        adapter = new GeneralAdapter(false,tableName,info);
    }


    public void setPersonRegistering(String personRegistering)
    {
        this.personRegistering = personRegistering;

        if(personRegistering == "airline-")
        {
            image.setImage(new Image("file:src/main/resources/datastructure/airplaneapplication/Pilot.png"));
        }
        else
        {
            image.setImage(new Image("file:src/main/resources/datastructure/airplaneapplication/Traveller.png"));
        }
    }

    public void onRegister() throws SQLException, IOException
    {
        HashMap<String,Object> map2 = new HashMap<>();
        map2.put("Username =",newUserName.getText());
        ArrayList<Object> list = adapter.findOneColumnRestricted("Username",map2);
        if(!list.isEmpty())
        {
            issue = true;
            error = "Username already taken";
        }
        if(newUserName.getText().isBlank() || newUserName.getText().isEmpty() || newUserName.getText() == null)
        {
            issue = true;
            error = "Enter Valid Username";

        }
        if (!(newPassword.getText().equals(confirmPassword.getText())))
        {
            issue = true;
            error = "Passwords Do not Match";
        }
        if(newPassword.getText().isBlank() || newPassword.getText().isEmpty() || newPassword.getText()==null)
        {
            issue = true;
            error = "Enter Valid Password";

        }

        if(confirmation.getText().isBlank() || confirmation.getText().isEmpty() || confirmation.getText()==null)
        {
            issue = true;
            error = "Enter Valid email";

        }

        if(issue)
        {
            String reference = "Alert-view.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(AirWingApplication.class.getResource(reference));
            Stage stage= new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            AlertController alert = (AlertController) fxmlLoader.getController();
            alert.setErrorMessage(error);


            issue = false;

        }
        else {
            String reference = "Confirmation-view.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(AirWingApplication.class.getResource(reference));
            Stage stage= new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();


            ConfirmationController cont = fxmlLoader.getController();
            cont.setAdapter(adapter);
            cont.setEmail(confirmation.getText());
            cont.setTableName(tableName);
            cont.setPersonRegistering(personRegistering);
            cont.setNewPassword(newPassword.getText());
            cont.setNewUserName(newUserName.getText());
            cont.setDepartureLoco(DepartureFrom.getValue());
            cont.send();

            Stage thisStage =(Stage) root.getScene().getWindow();
            thisStage.close();



        }
    }

    public void onEdit() throws IOException
    {
        ArrayList<String> airportLoco = AirportLocation.getCityInfo(DepartureFrom.getValue(),1);
        countryData = FXCollections.observableArrayList(airportLoco);
        System.out.println("here");
        DepartureFrom.setItems(countryData);
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        root.setStyle("-fx-background-color: #ffffff");

    }
}
