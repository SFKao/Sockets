package postmanDeLaMuerte;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {

        int puerto = 5000;
        String ip = "localhost";

        InetAddress address =null;
        try{
            address = Inet4Address.getByName(ip);
        } catch (UnknownHostException e) {
            System.out.println("IP no valida");
            System.exit(1);
        }

        try(
                Socket socket = new Socket(address,puerto);
                Scanner in = new Scanner(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
                Scanner teclado = new Scanner(System.in);
                ) {

            String s;
            while ((s = teclado.nextLine())!=null && s.length()>0){
                out.println(s);
                System.out.println(in.nextLine());
            }
            out.println(s);

        } catch (IOException e) {
            System.out.println("Error abriendo el socket");
            System.exit(2);
        }

    }

}
