package Demo02AIO.Server;

public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;

        AsyncTimeServerHandler asyncTimeServerHandler = new AsyncTimeServerHandler(port);
        new Thread(asyncTimeServerHandler, "AIO-timeServer001").start();
    }
}
