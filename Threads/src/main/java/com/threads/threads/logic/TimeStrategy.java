package com.threads.threads.logic;

import com.threads.threads.models.Client;
import com.threads.threads.models.Server;

import java.util.List;

public class TimeStrategy implements Strategy{
    @Override
    public void addClient(Client client, List<Server> serversList) {

        Server minWaitingTimeServer = serversList.get(0);
        for (Server server : serversList) {
            if (server.getWaitingTime().get() < minWaitingTimeServer.getWaitingTime().get()) {
                minWaitingTimeServer = server;
            }
        }
        minWaitingTimeServer.getClientsQueue().offer(client);
        minWaitingTimeServer.getWaitingTime().getAndAdd(client.getServiceTime());
        minWaitingTimeServer.getServerViewController().addClient(client);
    }

}
