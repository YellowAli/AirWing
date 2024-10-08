package datastructure.airplaneapplication;



import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class LoginController implements Initializable
{
    @FXML
    private AnchorPane Background;

    @FXML
    private TextField Password;

    @FXML
    private TextField Username;

    @FXML
    private ImageView img;

    @FXML
    private Label signUp;

    @FXML
    private Label forgotPassword;

    private static String refOwner;




    private GeneralAdapter adapter;

    String tableName = "Account";


    public LoginController() throws SQLException {
        String[] info= {"ReferenceID VARCHAR(30)","Username VARCHAR(30)","Passwords VARCHAR(200)","Email VARCHAR(40)", "DEPARTURELOCO VARCHAR(1000)"};
            adapter = new GeneralAdapter(false,tableName,info);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Background.setStyle("-fx-background-color: #ffffff");
    }


    public void onSignUp()throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(AirWingApplication.class.getResource("IntermediateRegister-view.fxml"));

        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Registration");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

        Stage sc = (Stage) Background.getScene().getWindow();
        sc.close();


    }

    public void login() throws SQLException, IOException {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("Username = ",Username.getText());
        Account acc = new Account();
        try
        {
            String refrence;
            ArrayList<Factory> lis = adapter.findRecords(map,acc);
            Argon2 argon2 = Argon2Factory.create();
            System.out.println(((Account)lis.getFirst()).getPassword());
            System.out.println(Password.getText());
            System.out.println(argon2.verify(((Account)lis.getFirst()).getPassword(),Password.getText()));
            if(argon2.verify(((Account)lis.getFirst()).getPassword(),Password.getText()))
            {
                System.out.println("here");
                String[] refs = ((Account)lis.getFirst()).getId().split("-");
                if(refs[0].equals("airline"))
                {
                    System.out.println("we made it");
                    refrence = "AirlineMainPage-view.fxml";
                }
                else if(refs[0].equals("traveller"))
                {
                    refrence = "TravellerMainPage-view.fxml";
                }
                else
                {
                    refrence = "FlightCrewMainPage-view.fxml";
                }
                FXMLLoader fxmlLoader = new FXMLLoader(AirWingApplication.class.getResource(refrence));
                Stage stage= new Stage();
                Scene scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
                ((MainPage)fxmlLoader.getController()).setRefID(((Account)lis.getFirst()).getId());
                System.out.println(((Account)lis.getFirst()).getId());
                Stage thisStage = (Stage)Background.getScene().getWindow();
                thisStage.close();
            }
            else
            {
                Exception ex = new Exception();
                throw ex;
            }


        }
        catch (Exception ex)
        {
            String error = "No Account Exists Under That Username or Password";
            String reference = "Alert-view.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(AirWingApplication.class.getResource(reference));
            Stage stage= new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            AlertController alert = (AlertController) fxmlLoader.getController();
            alert.setErrorMessage(error);

        }
    }

    public void onClick() throws SQLException, IOException
    {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("Username = ",Username.getText());
        Account acc = new Account();
        try
        {
            ArrayList<Factory> lis = adapter.findRecords(map,acc);
            refOwner = ((Account)lis.getFirst()).getId();
            String reference = "Confirmation-view.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(AirWingApplication.class.getResource(reference));
            Stage stage= new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            ConfirmationController cont = fxmlLoader.getController();
            cont.setChangingPassword(true);
            cont.setAdapter(adapter);
            cont.setTableName(tableName);
            cont.setEmail(((Account)lis.getFirst()).getEmail());
            cont.setNewUserName(Username.getText());
            cont.send();
            Stage thisStage = (Stage)Background.getScene().getWindow();
            thisStage.close();

        } catch (Exception e) {
            String error = "Enter Valid Username before Proceeding";
            String reference = "Alert-view.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(AirWingApplication.class.getResource(reference));
            Stage stage= new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            AlertController alert = (AlertController) fxmlLoader.getController();
            alert.setErrorMessage(error);
        }


    }












}