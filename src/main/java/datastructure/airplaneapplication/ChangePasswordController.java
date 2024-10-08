package datastructure.airplaneapplication;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChangePasswordController
{
    @FXML
    private PasswordField ConfirmPassword;

    @FXML
    private PasswordField NewPassword;

    @FXML
    private AnchorPane root;

    private boolean issue;
    private String error;

    private String userName;

    private GeneralAdapter adapter;
    private String tableName;

    public void onCLick() throws IOException, SQLException {
        if (!(NewPassword.getText().equals(ConfirmPassword.getText())))
        {
            issue = true;
            error = "Passwords Do not Match";
        }
        if(NewPassword.getText().isBlank() || NewPassword.getText().isEmpty() || NewPassword.getText()==null)
        {
            issue = true;
            error = "Enter Valid Password";

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
        else
        {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();

            Argon2 argon2 = Argon2Factory.create();
            String hash = argon2.hash(22, 65536, 1, NewPassword.getText().toCharArray());

            map.put("Passwords = ",hash);
            map.put(" WHERE Username = ",userName);
            adapter.updateTable(map);

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

    }

    public void setUsername(String userName)
    {
        this.userName = userName;
    }

    public void setAdapter(GeneralAdapter adapter)
    {
        this.adapter = adapter;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

}
