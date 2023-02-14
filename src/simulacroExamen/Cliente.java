package simulacroExamen;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {
        try(
                Socket socket = new Socket(InetAddress.getLoopbackAddress(),5000);
                Scanner in = new Scanner(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
                Scanner teclado = new Scanner(System.in);
                ){
            String s;
            while((s = teclado.nextLine())!=null && s.length()>0){

                out.println(s);
                System.out.println(in.nextLine());

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
