package com.threads.threads.gui;

import com.threads.threads.models.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WaitingClientsViewController {

    @FXML
    private Label arrivalTimeLabel;

    @FXML
    private Label idLabel;

    @FXML
    private ImageView image;

    @FXML
    private Label serviceTimeLabel;

    public void setData(Client client)
    {
        idLabel.setText(String.valueOf(client.getClientID()));
        serviceTimeLabel.setText(String.valueOf(client.getServiceTime()));
        arrivalTimeLabel.setText(String.valueOf(client.getArrivalTime()));
        Image img = new Image(getClass().getResourceAsStream("/com/threads/threads/p" +(client.getClientID()%6+1)+ ".png"));
        image.setImage(img);
    }

    public void updateServiceTime(int serviceTime)
    {
        Platform.runLater(() ->serviceTimeLabel.setText(String.valueOf(serviceTime)));
    }

}
