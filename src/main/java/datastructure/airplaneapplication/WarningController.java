package datastructure.airplaneapplication;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class WarningController {

    @FXML
    private AnchorPane root;

    @FXML
    private Button cancel;

    @FXML
    private Button confirm;

    @FXML
    private Label error;
    private ModifyController modifyController;

    public void setErrorMessage(String errorMessage)
    {
        error.setText(errorMessage);
    }

    public void setAccessingController(ModifyController modifyController)
    {
        this.modifyController = modifyController;
    }

    public void onConfirm()
    {
        modifyController.setConfirmation(true);
        Stage thisStage =(Stage) root.getScene().getWindow();
        thisStage.close();
    }

    public void onCancel()
    {
        modifyController.setConfirmation(false);
        Stage thisStage =(Stage) root.getScene().getWindow();
        thisStage.close();
    }


}
