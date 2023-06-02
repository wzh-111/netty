package Demo01TimeServer.Client;

public class TimeClient {
    public static void main(String[] args) {
        int port = 8080;
        TimeClientHandle timeClientHandle = new TimeClientHandle("127.0.0.1", port);
        new Thread(timeClientHandle, "Demo01TimeClient-001").start();
    }
}
