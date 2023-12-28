package com.example.visualwatch;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

public class NewTimeController {
    @FXML
    public Button close;
    @FXML
    public TextField hour;
    @FXML
    public TextField minut;
    @FXML
    public TextField sec;

    public ArrayList<Integer> getValue(){
        ArrayList<Integer> arr = new ArrayList<>();
        arr.add(Integer.parseInt(hour.getText()));
        arr.add(Integer.parseInt(minut.getText()));
        arr.add(Integer.parseInt(sec.getText()));
        return arr;
    }

    public void close(ActionEvent actionEvent) {
        Stage stage = (Stage)close.getScene().getWindow();
        stage.close();
    }
}
