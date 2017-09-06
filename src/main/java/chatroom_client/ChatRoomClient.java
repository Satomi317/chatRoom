package chatroom_client;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ChatRoomClient {
    private static Socket client;

    public static void main(String[] args) throws Exception {
        client = new Socket("127.0.0.1", 9999);
        new Thread(new ReadMsg()).start();
        new Thread(new SendMsg()).start();

    }

   static  class ReadMsg implements Runnable {

        public void run() {
            try {
                //接受从服务器发来的信息
                Scanner scanner = new Scanner(client.getInputStream());
                scanner.useDelimiter("\n");
                boolean flag = true;
                while (flag) {
                    if (scanner.hasNext()) {
                        System.out.println(scanner.next());
                    }
                    if (client.isClosed()) {
                        break;
                    }
                }
                scanner.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    static class SendMsg implements Runnable {
        public void run() {
            try {
                //向服务器发送信息
                Scanner scanner = new Scanner(System.in);
                scanner.useDelimiter("\n");
                PrintStream outWriter = new PrintStream(client.getOutputStream());
                boolean flag = true;
                while (flag) {
                    System.out.println("请输入要发送的信息 ：");
                    String str = null;
                    if (scanner.hasNext()) {
                        str = scanner.next().trim();
                        //向服务器发送数据
                        System.out.println("发送数据为：" + str);
                        outWriter.println(str);
                        outWriter.flush();
                        //结束当前socket
                        if (str.equalsIgnoreCase("byebye")) {
                            System.out.println("关闭客户端");
                            flag = false;
                            scanner.close();
                            outWriter.close();
                            client.close();
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
