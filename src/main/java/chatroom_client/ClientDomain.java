package chatroom_client;

import java.net.Socket;

public class ClientDomain {
    private Socket socket;
    private String userName;
    private String password;
    private int clientPort;//客户端端口号
    private int clientStatus;//客户端状态

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getClientPort() {
        return clientPort;
    }

    public void setClientPort(int clientPort) {
        this.clientPort = clientPort;
    }

    public int getClientStatus() {
        return clientStatus;
    }

    public void setClientStatus(int clientStatus) {
        this.clientStatus = clientStatus;
    }
}
