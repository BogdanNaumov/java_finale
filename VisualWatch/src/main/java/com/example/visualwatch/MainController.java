package com.example.visualwatch;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable,IOserver {


    public GridPane allWatch;
    magazin shop;
    public MainController(){
        allWatch = new GridPane();
        shop = new magazin("ShopWach","I");
        shop.cum(this);
    }




    @FXML
    public ChoiceBox<Type> type;
    @FXML
    public TextField Secund;
    @FXML
    public TextField name;
    @FXML
    public TextField price;
    @FXML
    public TextField Hour;
    @FXML
    public TextField Minut;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        type.getItems().addAll(Type.values());
        type.setOnAction(this::getType);
        type.setValue(Type.Clock);
    }

    public void getType(ActionEvent event){
        switch (type.getValue()){
            case Clock:
                Secund.setDisable(true);
                Secund.setText("00");
                break;
            case Clock_3:
                Secund.setDisable(false);
                break;
            default:
                break;
        }
    }

    public void addWatch(ActionEvent actionEvent) {
        Cl cl = ClockFactory.new_watch(type.getValue(),name.getText(),Float.parseFloat(price.getText()));
        try{
            cl.Set_time(Time.Hour, Integer.parseInt(Hour.getText()));
            cl.Set_time(Time.Minut, Integer.parseInt(Minut.getText()));
            cl.Set_time(Time.Sec, Integer.parseInt(Secund.getText()));
        }catch (InvalidType | IncorectData error){

        }
        shop.add(cl);
    }

    @Override
    public void event(magazin s) {
        allWatch.getChildren().clear();

        for (Cl p: shop) {
            try {
                WatchController pc = new WatchController(shop);
                FXMLLoader fxmlLoader = new FXMLLoader(WatchController.class.getResource("watch.fxml"));
                fxmlLoader.setController(pc);
                Parent root = fxmlLoader.load();
                pc.setWatsh(p);
                allWatch.addColumn(0, root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void sortName(ActionEvent actionEvent) {
        shop.Name();
    }

    public void sortPrice(ActionEvent actionEvent) {
        shop.MaxPriceWatch();
    }

    public void sortPopular(ActionEvent actionEvent) {
        shop.PopularName();
    }


    public void newTime(ActionEvent actionEvent) {
        ArrayList<Integer> arr;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NewTime.fxml"));
            Parent rootl = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Изменение времени");
            stage.setScene(new Scene(rootl, 226,100));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            Controller_new controller = fxmlLoader.getController();
            arr = controller.getValue();
            shop.SetTime(Time.Hour,arr.get(0));
            shop.SetTime(Time.Minut,arr.get(1));
            shop.SetTime(Time.Sec,arr.get(2));
        }catch (Exception e){
            System.out.println("error");
        }
    }
}