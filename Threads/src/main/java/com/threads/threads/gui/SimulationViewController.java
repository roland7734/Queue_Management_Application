package com.threads.threads.gui;

import com.threads.threads.logic.ShortestQueueStrategy;
import com.threads.threads.logic.SimulationManager;
import com.threads.threads.logic.TimeStrategy;
import com.threads.threads.models.Client;
import com.threads.threads.models.Server;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class SimulationViewController {

    @FXML
    private Label peakLabel;

    @FXML
    private Label serviceLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private VBox serverVBox;

    @FXML
    private Label waitingLabel;

    @FXML
    private VBox waitingClientsVBox;

    private SimulationManager simulationManager;
    @FXML
    private Button shortestQueueStrategyButton;

    @FXML
    private Button shortestTimeStrategyButton;

    public void setSimulationManager(SimulationManager simulationManager) {
        this.simulationManager = simulationManager;
    }

    public void updateInterface() {
        Platform.runLater(() ->timeLabel.setText("Time: " + simulationManager.getCurrentTime().get()));
//        Platform.runLater(() ->waitingClientsVBox.getChildren().clear());
//        for (Client client : simulationManager.getClientsList()) {
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/threads/threads/waiting-clients-view.fxml"));
//            HBox hBox;
//            try {
//                hBox = fxmlLoader.load();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            WaitingClientsViewController waitingClientsViewController = fxmlLoader.getController();
//            waitingClientsViewController.setData(client);
//            Platform.runLater(() ->waitingClientsVBox.getChildren().add(hBox));
//        }
//        Platform.runLater(() -> serverVBox.getChildren().clear());
//
////        for(Server server : simulationManager.getServersList())
////        {
////            FXMLLoader fxmlLoader = new FXMLLoader();
////            fxmlLoader.setLocation(getClass().getResource("/com/threads/threads/server-view.fxml"));
////            HBox hBox;
////            try {
////                hBox = fxmlLoader.load();
////            } catch (IOException e) {
////                throw new RuntimeException(e);
////            }
////
////            ServerViewController serverViewController = fxmlLoader.getController();
////            serverViewController.setData(server);
////            serverVBox.getChildren().add(hBox);
////        }
//
//        for (Server server : simulationManager.getServersList()) {
//            FXMLLoader fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(getClass().getResource("/com/threads/threads/server-view.fxml"));
//            HBox hBox;
//            try {
//                hBox = fxmlLoader.load();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//            ServerViewController serverViewController = fxmlLoader.getController();
//            serverViewController.setData(server);
//            Platform.runLater(() -> serverVBox.getChildren().add(hBox));
//        }

    }

    @FXML
    void onShortestQueueStrategyButtonClick() {
        simulationManager.setStrategy(new ShortestQueueStrategy());

    }

    @FXML
    void onShortestTimeStrategyButtonClick() {
        simulationManager.setStrategy(new TimeStrategy());
    }

    public void setTimes(String waitingTime, String serviceTime, int peakTime)
    {
        Platform.runLater(() ->waitingLabel.setText("Average Waiting Time: "+waitingTime));
        Platform.runLater(() ->serviceLabel.setText("Average Service Time: "+serviceTime));
        Platform.runLater(() ->peakLabel.setText("Peak Time: "+peakTime));
    }
    public void addClientToWaitingQueue(HBox clientHBox)
    {
        Platform.runLater(() ->waitingClientsVBox.getChildren().add(clientHBox));
    }
    public void removeClientFromWaitingQueue(HBox clientHBox)
    {
        Platform.runLater(() ->waitingClientsVBox.getChildren().remove(clientHBox));
    }

    public void addServerHBox(HBox serverHBox)
    {
        Platform.runLater(() ->serverVBox.getChildren().add(serverHBox));
    }


}
