package tcp.explicacion.ejercicio;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServidorTcp {

    public static void main(String[] args) {
        String puerto = "2367";
        if(args.length>=1)
            puerto = args[0];
        int port = Integer.parseInt(puerto);
        try(
                ServerSocket serverSocket = new ServerSocket(port);
                ){
            while(true){
                new ServidorTcp.hiloServidor(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.out.println("Hubo un error abriendo el servidor");
            e.printStackTrace();
        }

    }


    private static class hiloServidor extends Thread{

        Socket socket;
        public hiloServidor(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                    Scanner in = new Scanner(socket.getInputStream());
                    PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
                    ) {
                String linea;
                while ((linea = in.nextLine())!=null && linea.length()>0) {
                    out.println("#"+linea+"#");
                }
            } catch (SocketException | NoSuchElementException e) {
                System.out.println("Se cerró la conexion");
            } catch (IOException e) {
                System.out.println("Hubo un problema con el I/O del servidor");
            }finally {
                if(socket!=null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}





