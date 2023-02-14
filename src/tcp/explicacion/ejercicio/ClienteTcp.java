package tcp.explicacion.ejercicio;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClienteTcp {

    public static void main(String[] args) {

        String puerto = "2367";
        String ip = "localhost";

        if(args.length>=2){
            puerto = args[0];
            ip = args[0];
        }
        InetAddress address = null;
        int port = 0;
        try {
            address = Inet4Address.getByName(ip);
            port = Integer.parseInt(puerto);
        } catch (UnknownHostException | NumberFormatException e) {
            System.out.println("No se reconoce la ip o puerto");
            System.exit(-1);
        }

        try(Socket socket = new Socket(address,port);
            Scanner teclado = new Scanner(System.in);
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
        ){
            String lectura;
            System.out.print("> ");
            while (!(lectura = teclado.nextLine()).equals("")){
                out.println(lectura);
                System.out.println("Respuesta = "+in.nextLine());
                System.out.print("> ");
            }
            System.out.println("Saliendo...");
        } catch (IOException e) {
            System.out.println("Hubo un problema con el socket.");
        }

    }
}
