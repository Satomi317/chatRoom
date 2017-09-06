package chatroom_client;

import java.util.Scanner;

public class Test{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        while (true){
            String test;
            if (scanner.hasNext()){
                test = scanner.next().trim();
                if (test.contains("userName")){
                    System.out.println("字符串中包含用户名信息，且用户名为："+test.substring(test.indexOf(":")+1));
                }
                else {
                    System.out.println("字符串中不包含用户名信息");
                }
            }
        }
    }
}
