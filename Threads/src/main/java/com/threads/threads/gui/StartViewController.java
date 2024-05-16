package com.threads.threads.gui;

import com.threads.threads.logic.SimulationManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class StartViewController {

    @FXML
    private TextField arrivalTimeMaxTextField;

    @FXML
    private TextField arrivalTimeMinTextField;

    @FXML
    private Button clearButton;

    @FXML
    private TextField nrClientsTextField;

    @FXML
    private TextField nrServersTextField;

    @FXML
    private TextField serviceTimeMaxTextField;

    @FXML
    private TextField serviceTimeMinTextField;

    @FXML
    private TextField simulationTimeTextField;

    @FXML
    private Button startSimulationButton;

    @FXML
    void onClearButtonClick() {
        arrivalTimeMaxTextField.clear();
        arrivalTimeMinTextField.clear();
        nrClientsTextField.clear();
        nrServersTextField.clear();
        serviceTimeMaxTextField.clear();
        serviceTimeMinTextField.clear();
        simulationTimeTextField.clear();
    }

    @FXML
    void onStartSimulationButtonClick() {
        if (areEmptyFields()) {
            AlertUtility.openAlertError("Empty Fields", "Please fill in all the fields.");
        } else if (!isNumeric(arrivalTimeMinTextField.getText()) || !isNumeric(arrivalTimeMaxTextField.getText()) ||
                !isNumeric(serviceTimeMinTextField.getText()) || !isNumeric(serviceTimeMaxTextField.getText()) ||
                !isNumeric(simulationTimeTextField.getText()) || !isNumeric(nrClientsTextField.getText()) ||
                !isNumeric(nrServersTextField.getText())) {
            AlertUtility.openAlertError("Invalid Input", "All fields must contain numeric values.");
        } else if (!isValidRange(arrivalTimeMinTextField.getText(), arrivalTimeMaxTextField.getText())) {
            AlertUtility.openAlertError("Invalid Range", "Min Arrival Time must be less than Max Arrival Time.");
        } else if (!isValidRange(serviceTimeMinTextField.getText(), serviceTimeMaxTextField.getText())) {
            AlertUtility.openAlertError("Invalid Range", "Min Service Time must be less than Max Service Time.");
        } else {

            Stage stage = (Stage) startSimulationButton.getScene().getWindow();
            stage.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/threads/threads/simulation-view.fxml"));
            Scene scene;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            SimulationViewController simulationViewController = fxmlLoader.getController();
            SimulationManager simulationManager = new SimulationManager(Integer.parseInt(nrClientsTextField.getText()),
                    Integer.parseInt(nrServersTextField.getText()), Integer.parseInt(simulationTimeTextField.getText()),
                    Integer.parseInt(serviceTimeMinTextField.getText()),
                    Integer.parseInt(serviceTimeMaxTextField.getText()),Integer.parseInt(arrivalTimeMinTextField.getText()),
                    Integer.parseInt(arrivalTimeMaxTextField.getText()),simulationViewController);
            simulationViewController.setSimulationManager(simulationManager);
            Thread simulationThread = new Thread(simulationManager);
            simulationThread.start();
            stage.setTitle("Simulation");
            stage.setScene(scene);
            stage.show();

        }
    }

    private boolean areEmptyFields() {
        return arrivalTimeMaxTextField.getText().isEmpty()
                || arrivalTimeMinTextField.getText().isEmpty()
                || nrClientsTextField.getText().isEmpty()
                || nrServersTextField.getText().isEmpty()
                || serviceTimeMaxTextField.getText().isEmpty()
                || serviceTimeMinTextField.getText().isEmpty()
                || simulationTimeTextField.getText().isEmpty();
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

    private boolean isValidRange(String minStr, String maxStr) {
        int min = Integer.parseInt(minStr);
        int max = Integer.parseInt(maxStr);
        return min < max;
    }


}