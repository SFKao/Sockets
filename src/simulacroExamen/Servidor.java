package simulacroExamen;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;
import java.util.Scanner;

public class Servidor {

    public static void main(String[] args) {
        try(
                ServerSocket serverSocket = new ServerSocket(5000);
                ){
            while (true) {
                new SocketCliente(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class SocketCliente extends Thread{

        private Socket socket;

        public SocketCliente(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try(
                    Scanner in = new Scanner(socket.getInputStream());
                    PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
                    ){
                System.out.println("Nueva conexion de "+socket.getInetAddress()+":"+socket.getPort());
                while (socket.isConnected()){
                    String s = in.nextLine();
                    String s2 = s.toUpperCase(Locale.ROOT);
                    out.println(s2);
                    System.out.println("Recibido mensaje de "+socket.getInetAddress()+":"+socket.getPort()+" con mensaje "+s+", devolviendo "+s2);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
