package server;

import data.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Locale;

public class Server {
    public static void main(String[] args) {
        ArrayList<User> users = new ArrayList<>();
        try {
            ServerSocket serverSocket = new ServerSocket(8188); //socket - разьем -
            while (true) {
                Socket socket = serverSocket.accept();//так мы ожидаем что ктото подсоединится//ожидаем клиента
                System.out.println("Клиент подключился! его ip: " + socket.getInetAddress().toString());
                User currentUser = new User(socket);
                users.add(currentUser);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DataOutputStream out = new DataOutputStream(socket.getOutputStream()); // отправляем клиенту
                            out.writeUTF("Введите имя:");
                            DataInputStream in = new DataInputStream(socket.getInputStream()); // принимаем клиенту
                            String userName = in.readUTF();
                            currentUser.setUserName(userName);
                            out.writeUTF("Добро пожаловать на сервер, " + currentUser.getUserName() + "!");
                            while (true) {
                                String request = in.readUTF();//получаем данные с клиента
                                //out.writeUTF(request.toUpperCase(Locale.ROOT));//отправляем на клиент с поднятым регистром
                                //Отправим всем подключенным
                                for(User user: users){
                                    if(currentUser.equals(user)) continue;
                                    DataOutputStream userOut = new DataOutputStream(user.getSocket().getOutputStream());
                                    userOut.writeUTF(userName + ": " + request);
                                }
                            }
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                });//когда к нам подключается клиент мы создаем для него отдельный поток
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();//port может быть занят из-за этого надо обрабатывать ошибки
        }
    }
}
