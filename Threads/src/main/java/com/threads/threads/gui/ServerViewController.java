package com.threads.threads.gui;
import com.threads.threads.models.Client;
import com.threads.threads.models.Server;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class ServerViewController {

        @FXML
        private Button serverNumber;

        @FXML
        private HBox serverHBox;

        public void setData(Server server) {
                serverNumber.setText(String.valueOf(server.getServerID() + 1));
                serverHBox.getChildren().clear();
        }

        public void addClient(Client client)
        {

                Platform.runLater(() ->serverHBox.getChildren().add(client.getClientHBox()));
        }
        public void removeClient()
        {
                Platform.runLater(() ->serverHBox.getChildren().remove(0));
        }




//                for(Client client : server.getClientsQueue())
//                {
//                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/threads/threads/waiting-clients-view.fxml"));
//                        HBox hBox;
//                        try {
//                                hBox = fxmlLoader.load();
//                        } catch (IOException e) {
//                                throw new RuntimeException(e);
//                        }
//                        WaitingClientsViewController waitingClientsViewController = fxmlLoader.getController();
//                        waitingClientsViewController.setData(client);
//                        serverHBox.getChildren().add(hBox);
//                }



}
