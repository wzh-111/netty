package Demo02AIO.Cleint;

public class TimeClient {
    public static void main(String[] args) {
        int port = 8080;
        AsyncTimeClientHandler asyncTimeClientHandler1 = new AsyncTimeClientHandler("127.0.0.1", port);
        new Thread(asyncTimeClientHandler1, "AIO-timeClient001").start();

        AsyncTimeClientHandler asyncTimeClientHandler2 = new AsyncTimeClientHandler("127.0.0.1", port);
        new Thread(asyncTimeClientHandler2, "AIO-timeClient002").start();
    }
}
