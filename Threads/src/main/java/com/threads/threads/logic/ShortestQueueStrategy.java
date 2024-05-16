package com.threads.threads.logic;

import com.threads.threads.models.Client;
import com.threads.threads.models.Server;

import java.util.List;

public class ShortestQueueStrategy implements Strategy{
    @Override
    public void addClient(Client client, List<Server> serversList) {

        Server minQueueServer = serversList.get(0);
        for (Server server : serversList) {
            if (server.getClientsQueue().size() < minQueueServer.getClientsQueue().size()) {
                minQueueServer = server;
            }
        }
        minQueueServer.getClientsQueue().offer(client);
        minQueueServer.getWaitingTime().getAndAdd(client.getServiceTime());
        minQueueServer.getServerViewController().addClient(client);
    }

}
