package com.threads.threads.models;

import com.threads.threads.gui.ServerViewController;
import com.threads.threads.logic.SimulationManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{

    private int serverID;
    private AtomicInteger totalWaitingTime;
    private AtomicInteger waitingTime;
    private BlockingQueue<Client> clientsQueue;
    private SimulationManager simulationManager;
    private HBox serverHBox;
    private ServerViewController serverViewController;

    public HBox getServerHBox() {
        return serverHBox;
    }

    public void setServerHBox(HBox serverHBox) {
        this.serverHBox = serverHBox;
    }

    public Server(int serverID, SimulationManager simulationManager) {
        this.serverID = serverID;
        this.totalWaitingTime = new AtomicInteger(0);
        this.waitingTime = new AtomicInteger(0);
        this.clientsQueue = new LinkedBlockingQueue<>();
        this.simulationManager = simulationManager;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/threads/threads/server-view.fxml"));
        try {
            this.serverHBox = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.serverViewController = fxmlLoader.getController();
        this.serverViewController.setData(this);
    }

    public ServerViewController getServerViewController() {
        return serverViewController;
    }

    public void setServerViewController(ServerViewController serverViewController) {
        this.serverViewController = serverViewController;
    }

    public int getServerID() {
        return serverID;
    }

    public void setServerID(int serverID) {
        this.serverID = serverID;
    }

    public AtomicInteger getTotalWaitingTime() {
        return totalWaitingTime;
    }

    public void setTotalWaitingTime(AtomicInteger totalWaitingTime) {
        this.totalWaitingTime = totalWaitingTime;
    }

    public AtomicInteger getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(AtomicInteger waitingTime) {
        this.waitingTime = waitingTime;
    }

    public BlockingQueue<Client> getClientsQueue() {
        return clientsQueue;
    }

    public void setClientsQueue(BlockingQueue<Client> clientsQueue) {
        this.clientsQueue = clientsQueue;
    }

    public SimulationManager getSimulationManager() {
        return simulationManager;
    }

    public void setSimulationManager(SimulationManager simulationManager) {
        this.simulationManager = simulationManager;
    }


    @Override
    public void run() {
        while (!simulationManager.getEndingOfSimulation().get()) {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
                Client client = clientsQueue.peek();
                if (client != null&&simulationManager.getCurrentTime().get()>=client.getArrivalTime()) {
                    int remainingServiceTime = client.getServiceTime() - 1;
                    client.setServiceTime(remainingServiceTime);
                    waitingTime.getAndDecrement();

                    if (remainingServiceTime == 0) {
                        clientsQueue.poll();
                        totalWaitingTime.addAndGet(simulationManager.getCurrentTime().get()-client.getArrivalTime());
                        serverViewController.removeClient();
                    }
                    else
                    {
                        client.getWaitingClientsViewController().updateServiceTime(remainingServiceTime);
                    }
                }


            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public String toString() {
        return "Server{" +
                "serverID=" + serverID +
                ", totalWaitingTime=" + totalWaitingTime +
                ", waitingTime=" + waitingTime +
                ", clientsQueue=" + clientsQueue+
                '}';
    }
}
