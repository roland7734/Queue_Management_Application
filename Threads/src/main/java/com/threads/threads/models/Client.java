package com.threads.threads.models;

import com.threads.threads.gui.WaitingClientsViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class Client {

    private int clientID;
    private int arrivalTime;
    private int serviceTime;
    private HBox clientHBox;
    private WaitingClientsViewController waitingClientsViewController;

    public Client(int clientID, int arrivalTime, int serviceTime) {
        this.clientID = clientID;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/threads/threads/waiting-clients-view.fxml"));
        try {
            this.clientHBox = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.waitingClientsViewController = fxmlLoader.getController();
        this.waitingClientsViewController.setData(this);
    }

    public WaitingClientsViewController getWaitingClientsViewController() {
        return waitingClientsViewController;
    }

    public void setWaitingClientsViewController(WaitingClientsViewController waitingClientsViewController) {
        this.waitingClientsViewController = waitingClientsViewController;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public HBox getClientHBox() {
        return clientHBox;
    }

    public void setClientHBox(HBox clientHBox) {
        this.clientHBox = clientHBox;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientID=" + clientID +
                ", arrivalTime=" + arrivalTime +
                ", serviceTime=" + serviceTime +
                '}';
    }
}
