package com.example.visualwatch;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WatchController {
    Cl cl;
    magazin shop;

    public WatchController(magazin shop){
        this.shop = shop;
    }
    public void setWatsh(Cl cl){
        this.cl = cl;
        name.setText(cl.Get_name());
        price.setText(Float.toString(cl.Get_price()));
        switch (cl.Get_type()){
            case Clock:
              type.setText("Без секундной");
              break;
            case Clock_3:
              type.setText("С секундной");
              break;
            default:
              break;
        }
        time.setText(cl.toString());
    }
    @FXML
    public Label name;
    @FXML
    public Label price;
    @FXML
    public Label type;
    @FXML
    public Label time;



    public void remove(ActionEvent actionEvent) {
        shop.remove(cl);
    }
}
