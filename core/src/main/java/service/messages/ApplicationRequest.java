package service.messages;

import service.core.ClientInfo;

public class ApplicationRequest implements MySerializable {
    private ClientInfo clientInfo;

    public ApplicationRequest(ClientInfo info) {
        this.clientInfo = info;
    }

    public ApplicationRequest() {
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setInfo(ClientInfo info) {
        this.clientInfo = info;
    }
}
