package Demo02AIO.Cleint;

import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

public class AsyncTimeClientHandler implements Runnable, CompletionHandler<Void, AsyncTimeClientHandler> {
    private String host;
    private int port;
    private AsynchronousSocketChannel client;
    private CountDownLatch latch;

    public AsyncTimeClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            client = AsynchronousSocketChannel.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        client.connect(new InetSocketAddress(host, port), this, this);
        try {
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void completed(Void result, AsyncTimeClientHandler attachment) {
        // 连接建立后，发请求
        String body = "time";
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        client.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer writeBuffer) {
                // 没写完继续写
                if (writeBuffer.hasRemaining()) {
                    client.write(writeBuffer, writeBuffer, this);
                } else {
                    // 读取响应
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    client.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer readBuffer) {
                            if (result > 0) {
                                readBuffer.flip();
                                byte[] bytes = new byte[readBuffer.remaining()];
                                readBuffer.get(bytes);
                                try {
                                    String body = new String(bytes, "UTF-8");
                                    System.out.println("当前时间: "+ body);
                                    latch.countDown();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer readBuffer) {
                            try {
                                client.close();
                                latch.countDown();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer writeBuffer) {
                try {
                    client.close();
                    latch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void failed(Throwable exc, AsyncTimeClientHandler attachment) {
        try {
            client.close();
            latch.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
