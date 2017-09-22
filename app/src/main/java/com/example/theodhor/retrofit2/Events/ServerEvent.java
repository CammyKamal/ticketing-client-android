package com.example.theodhor.retrofit2.Events;

import com.example.theodhor.retrofit2.net.ServerResponse;

/**
 * Created by Theodhor Pandeli on 2/11/2016.
 */
public class ServerEvent {
    private ServerResponse serverResponse;

    public ServerEvent(ServerResponse serverResponse) {
        this.serverResponse = serverResponse;
    }

    public ServerResponse getServerResponse() {
        return serverResponse;
    }

    public void setServerResponse(ServerResponse serverResponse) {
        this.serverResponse = serverResponse;
    }

}
