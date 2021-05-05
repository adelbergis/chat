package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("192.168.1.67",8188); // подключаемся к серверу
            //можно localhost можно 127.0.0.1
            System.out.println("Подключился");
            //Принимаем сообщения от сервереа:
            DataInputStream in = new DataInputStream(socket.getInputStream());
            String response = in.readUTF();//ждем сообщение от сервера
            System.out.println("Ответ от сервера: " + response);
            //теперь отправим что-то на сервер
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        try {
                            String response = in.readUTF();
                            System.out.println(response);
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            });//создаем отдельный поток для чтения с сервере
            thread.start();
            while (true) {
                String request = scanner.nextLine(); //читаем пользовательский ввод
                out.writeUTF(request); //отправляем на сервер текст с консоли
                //response = in.readUTF(); // Читаем ответ от сервера
                //System.out.println("Ответ от сервера: " + response); // Выводи на экран ответ от сервера
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
