package datastructure.airplaneapplication;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Advanced;
import de.mkammerer.argon2.Argon2Factory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

public class ConfirmationController implements Initializable
{
    @FXML
    private AnchorPane root;
    @FXML
    private TextField codeee;
    private String departureLoco;

    @FXML
    private Button reSend;

    @FXML
    private Button submit;
    double confirmationCode;
    private String email;
    private String personRegistering;
    private String newPassword;
    private String newUserName;
    private String tableName;

    private GeneralAdapter adapter;

    private Boolean changingPassword = false;





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        confirmationCode = Math.ceil(Math.random()*400000);

    }

    public void send()
    {
        Email.sendEmail(email,Double.toString(confirmationCode));
    }

    public void onReSend()
    {
        confirmationCode = Math.ceil(Math.random()*400000);
        Email.sendEmail(email,Double.toString(confirmationCode));
    }

    public void onSubmit() throws SQLException, IOException {
        if(codeee.getText().equals(Double.toString(confirmationCode)) && !changingPassword)
        {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            Argon2 argon2 = Argon2Factory.create();
            int iterations = 4;
            int memory = 65536;
            int parallelism = 1;
            String hash = argon2.hash(iterations, memory, parallelism, newPassword.toCharArray());
            ArrayList<Object> lis = adapter.findOneColumn("ReferenceID", "ReferenceID");


            map.put("ReferenceID", returnKey(lis,personRegistering));
            map.put("Username", newUserName);
            map.put("Passwords", hash);
            map.put("Email",email);
            map.put("DepartureLoco",departureLoco);
            adapter.addRecord(map);

            String reference = "Login-view.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(AirWingApplication.class.getResource(reference));
            Stage stage= new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            Stage thisStage =(Stage) root.getScene().getWindow();
            thisStage.close();

        }
        else if (codeee.getText().equals(Double.toString(confirmationCode)) && changingPassword)
        {
            String reference = "ChangePassword-view.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(AirWingApplication.class.getResource(reference));
            Stage stage= new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            ChangePasswordController cont = (ChangePasswordController) fxmlLoader.getController();
            cont.setUsername(newUserName);
            cont.setAdapter(adapter);
            cont.setTableName(tableName);

            Stage thisStage =(Stage) root.getScene().getWindow();
            thisStage.close();

        }
        else
        {
            String reference = "Alert-view.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(AirWingApplication.class.getResource(reference));
            Stage stage= new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            AlertController alert = (AlertController) fxmlLoader.getController();
            alert.setErrorMessage("Wrong code");
        }
    }

    public String returnKey(ArrayList<Object> lis,String personRegistering)
    {

        String j = "1";
        String missingKey = "z";
        int max = 0;
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < lis.size(); i++)
        {
            String[] refSplit = ((String) lis.get(i)).split("-");
            String ref = refSplit[1];

            if(refSplit[0].concat("-").equals(personRegistering))
                list.add(ref);
        }
        for (int i = 0; i < list.size(); i++) {
            if (!list.contains(j)) {
                missingKey = j;
                break;
            }
            if(max < Integer.parseInt(list.get(i)))
                max = Integer.parseInt(list.get(i));
            int convert = Integer.parseInt(j);
            convert++;
            j = Integer.toString(convert);
        }

        if(!missingKey.equals("z"))
        {
            return personRegistering+ missingKey;
        }
        else
        {

            return personRegistering+ ++max;
        }


    }






    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPersonRegistering() {
        return personRegistering;
    }

    public void setPersonRegistering(String personRegistering) {
        this.personRegistering = personRegistering;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewUserName() {
        return newUserName;
    }

    public void setNewUserName(String newUserName) {
        this.newUserName = newUserName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setAdapter(GeneralAdapter adapter)
    {
        this.adapter = adapter;
    }

    public void setChangingPassword(Boolean changingPassword)
    {
        this.changingPassword = changingPassword;
    }
    public void setDepartureLoco(String departureLoco)
    {
        this.departureLoco = departureLoco;
    }


}
