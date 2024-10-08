package datastructure.airplaneapplication;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SuccessController {

    @FXML
    private Label error;

    public void setSuccess(String success)
    {
        error.setText(success);
    }

}
