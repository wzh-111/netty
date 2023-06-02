package Demo01TimeServer.Client;

public class TimeClient {
    public static void main(String[] args) {
        int port = 8080;
        TimeClientHandle timeClientHandle1 = new TimeClientHandle("127.0.0.1", port);
        new Thread(timeClientHandle1, "Demo01TimeClient-001").start();
        TimeClientHandle timeClientHandle2 = new TimeClientHandle("127.0.0.1", port);
        new Thread(timeClientHandle2, "Demo01TimeClient-002").start();
        TimeClientHandle timeClientHandle3 = new TimeClientHandle("127.0.0.1", port);
        new Thread(timeClientHandle3, "Demo01TimeClient-003").start();
    }
}
