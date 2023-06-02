package Demo01TimeServer.Server;

public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;

        MultiplexerTimeServer multiplexerTimeServer = new MultiplexerTimeServer(port);
        new Thread(multiplexerTimeServer, "Demo01TimeServer-001").start();
    }
}
