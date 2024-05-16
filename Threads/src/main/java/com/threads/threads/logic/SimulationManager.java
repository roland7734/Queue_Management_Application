package com.threads.threads.logic;

import com.threads.threads.gui.SimulationViewController;
import com.threads.threads.models.Client;
import com.threads.threads.models.Server;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulationManager implements  Runnable {

    private int clientsNumber;
    private int serversNumber;
    private int simulationTime;
    private int serviceTimeMin;
    private int serviceTimeMax;
    private int arrivalTimeMin;
    private int arrivalTimeMax;
    private List<Client> clientsList = new ArrayList<>();
    private List<Server> serversList = new ArrayList<>();
    private ExecutorService executor;
    private AtomicBoolean endingOfSimulation = new AtomicBoolean(false);
    private AtomicInteger currentTime = new AtomicInteger(0);
    private SimulationViewController simulationViewController;
    private Strategy strategy = new TimeStrategy();

    public SimulationManager(int clientsNumber, int serversNumber, int simulationTime, int serviceTimeMin, int serviceTimeMax, int arrivalTimeMin, int arrivalTimeMax,SimulationViewController simulationViewController) {
        this.clientsNumber = clientsNumber;
        this.serversNumber = serversNumber;
        this.simulationTime = simulationTime;
        this.serviceTimeMin = serviceTimeMin;
        this.serviceTimeMax = serviceTimeMax;
        this.arrivalTimeMin = arrivalTimeMin;
        this.arrivalTimeMax = arrivalTimeMax;
        this.simulationViewController=simulationViewController;
        this.executor= Executors.newFixedThreadPool(serversNumber);
    }
    public List<Client> getClientsList() {
        return clientsList;
    }

    public List<Server> getServersList() {
        return serversList;
    }
    public AtomicInteger getCurrentTime() {
        return currentTime;
    }


    public AtomicBoolean getEndingOfSimulation() {
        return endingOfSimulation;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void run() {
        int peakTime=0;
        int numberClientsAtServers=0;
        generateRandomClients();
        generateServers();
        createNewLogFile();
        double averageServiceWaitingTime = averageServiceWaitingTime();

        while (!endingOfSimulation.get()) {
            distributeClients();

            if (checkEndOfSimulation()) {
                endingOfSimulation.set(true);
            }

            if(numberClientsAtServers<getClientsNumberAtServers())
            {
                numberClientsAtServers=getClientsNumberAtServers();
                peakTime = currentTime.get();
            }

            simulationViewController.updateInterface();
            writeToLogFile(createOutputForLogging());
            currentTime.getAndIncrement();
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        double averageWaitingTime = averageWaitingTime();
        String formattedWaitingTime = String.format("%.2f", averageWaitingTime);
        String formattedServiceTime = String.format("%.2f", averageServiceWaitingTime);
        writeToLogFile(generateAverageMetricsString(formattedWaitingTime,formattedServiceTime,peakTime));
        simulationViewController.setTimes(formattedWaitingTime,formattedServiceTime,peakTime);

    }

    private void generateServers() {
        for (int i = 0; i < serversNumber; i++) {
            Server server = new Server(i, this);
            serversList.add(server);
            executor.execute(server);
            this.simulationViewController.addServerHBox(server.getServerHBox());
        }
    }

    private static int[] generateIncreasingArrivalTimes(int clientsNumber, int arrivalTimeMin, int arrivalTimeMax) {
        int[] arrivalTimes = new int[clientsNumber];
        Random random = new Random();

        for (int i = 0; i < clientsNumber; i++) {
            arrivalTimes[i] = random.nextInt(arrivalTimeMax - arrivalTimeMin + 1) + arrivalTimeMin;
        }
        Arrays.sort(arrivalTimes);

        return arrivalTimes;
    }
    private void generateRandomClients() {
        int[] arrivalTimes = generateIncreasingArrivalTimes(clientsNumber,arrivalTimeMin,arrivalTimeMax);
        Random random = new Random();
        for (int i = 0; i < clientsNumber; i++) {
            int clientID = i;
            int arrivalTime = arrivalTimes[i];
            int serviceTime = random.nextInt(serviceTimeMax - serviceTimeMin + 1) + serviceTimeMin;
            Client client = new Client(clientID, arrivalTime, serviceTime);
            clientsList.add(client);
            this.simulationViewController.addClientToWaitingQueue(client.getClientHBox());
        }
    }


    private void distributeClients() {
        Iterator<Client> iterator = clientsList.iterator();
        while (iterator.hasNext()) {
            Client client = iterator.next();
            if (client.getArrivalTime() <= currentTime.get()) {
                strategy.addClient(client,serversList);
                simulationViewController.removeClientFromWaitingQueue(client.getClientHBox());
                iterator.remove();
            }
        }
    }


    private boolean checkEndOfSimulation() {
        if (!getClientsList().isEmpty()) {
            return false;
        }
        for (Server server : serversList) {
            if (!server.getClientsQueue().isEmpty()) {
                return false;
            }
        }

        if (currentTime.get() < simulationTime) return false;
        return true;
    }
    private void writeToLogFile(String output) {
        try (FileWriter writer = new FileWriter("logfile.txt", true)) {
            writer.write(output + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String createOutputForLogging() {
        StringBuilder output = new StringBuilder();
        output.append("Time ").append(currentTime).append("\n");

        output.append("Waiting clients: ");
        for (Client client : clientsList) {
            output.append("(").append(client.getClientID()).append(",")
                    .append(client.getArrivalTime()).append(",")
                    .append(client.getServiceTime()).append("); ");
        }
        output.append("\n");

        for (Server server : serversList) {
            output.append("Queue ").append(server.getServerID() + 1).append(": ");
            if (server.getClientsQueue().isEmpty()) {
                output.append("closed").append("\n");
            } else {
                for (Client client : server.getClientsQueue()) {
                    output.append("(").append(client.getClientID()).append(",")
                            .append(client.getArrivalTime()).append(",")
                            .append(client.getServiceTime()).append("); ");
                }
                output.append("\n");
            }
        }

        return output.toString();
    }

    private void createNewLogFile()
    {
        try (FileWriter writer = new FileWriter("logfile.txt", false)) {
        } catch (IOException e) {
            throw new RuntimeException("Cant delete Log File");
        }
    }

    private double averageServiceWaitingTime() {
        double totalServiceWaitingTime = 0;
        int totalClients = clientsNumber;

        for (Client client : clientsList) {
            totalServiceWaitingTime += client.getServiceTime();
        }

        return totalClients == 0 ? 0 : totalServiceWaitingTime / totalClients;
    }

    private double averageWaitingTime() {
        double totalWaitingTime = 0;
        int totalClients = clientsNumber;

        for (Server server : serversList)
            totalWaitingTime = totalWaitingTime + server.getTotalWaitingTime().get();

        return totalClients == 0 ? 0 : totalWaitingTime / totalClients;
    }

    public int getClientsNumberAtServers() {

        int nrClientAtServer=0;
        for (Server server : serversList) {
            nrClientAtServer += server.getClientsQueue().size();
        }

        return nrClientAtServer;
    }

    public String generateAverageMetricsString(String averageWaitingTime, String averageServiceTime, int peakTime) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Average Waiting Time: ").append(averageWaitingTime).append("\n");
        stringBuilder.append("Average Service Time: ").append(averageServiceTime).append("\n");
        stringBuilder.append("Peak Time: ").append(peakTime).append("\n");
        return stringBuilder.toString();
    }


}