package chatroom_server;

import chatroom_client.ClientDomain;
import comm_utils.CommUtils;

import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ChatRoomServer extends CommUtils {
    //存入客户端实体
    public static List<ClientDomain> clientList = new ArrayList<ClientDomain>();
    private static Lock listLock = new ReentrantLock();

    //线程池大小
    private static final int threadNum = 20;

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        ServerSocket serverSocket = new ServerSocket(9999);
        System.out.println("服务器创建成功，等待用户连接");
        for (int i = 0; i < threadNum; i++) {
            Socket client = serverSocket.accept();
            executorService.execute(new ExcuteClientThread(client));
        }
        executorService.shutdown();
        serverSocket.close();
    }

    public static class ExcuteClientThread implements Runnable {
        Socket client;

        public ExcuteClientThread(Socket client) {
            this.client = client;
        }

        public void run() {
            try {
                boolean flag = true;
                ClientDomain clientDomain = new ClientDomain();
                //获取客户端输入
                Scanner scanner = new Scanner(client.getInputStream());
                scanner.useDelimiter("\n");
                String userName = "";
                while (flag) {
                    if (scanner.hasNext()) {
                        String str = scanner.next().trim();
                        //判断是否结束
                        if ("byebye".equalsIgnoreCase(str)) {
                            //结束输入，关闭socket
                            //在客户端列表中移除本实体
                            //删除操作，上锁
                            listLock.lock();
                            try {
                                Iterator<ClientDomain> iterator = clientList.iterator();
                                while (iterator.hasNext()) {
                                    ClientDomain clientDomain1 = iterator.next();
                                    if (clientDomain1.getUserName().equals(userName)) {
                                        iterator.remove();
                                        break;
                                    }
                                }
                                if (clientList.size() != 0) {
                                    System.out.println("在线的好友有：" + clientList.size() + "人");
                                } else {
                                    System.out.println("所有用户均已退出群聊");
                                }
                            } catch (Exception e) {
                                System.out.println("删除操作出错" + e);
                            } finally {
                                listLock.unlock();
                            }
                            scanner.close();
                            client.close();
                            flag = false;
                        } else {
                            //用户注册
                            if (str.contains("userName")) {
                                userName = str.substring(str.indexOf(":") + 1);
                                //写入操作，上锁
                                listLock.lock();
                                try {
                                    //将用户名和socket存入实体
                                    clientDomain.setUserName(userName);
                                    clientDomain.setSocket(client);
                                    clientList.add(clientDomain);
                                    System.out.println("客户：" + userName + "上线了！");
                                    continue;
                                } catch (Exception e) {
                                    System.out.println("写入操作出错" + e);
                                } finally {
                                    listLock.unlock();
                                }
                            }
                            //发送群聊信息
                            String outMessage = "（群聊信息）您的好友" + userName + "说 :" + str;
                            System.out.println("服务器群聊信息为: " + outMessage);
                            for (ClientDomain clientDomain1 : clientList) {
                                PrintStream out = new PrintStream(clientDomain1.getSocket().getOutputStream());
                                out.println(outMessage);
                                out.flush();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
