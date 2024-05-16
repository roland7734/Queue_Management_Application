package com.threads.threads.logic;

import com.threads.threads.models.Client;
import com.threads.threads.models.Server;

import java.util.List;

public interface Strategy {

    public void addClient(Client client, List<Server> serverList);
}
