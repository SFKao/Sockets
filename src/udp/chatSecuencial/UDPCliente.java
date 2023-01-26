package udp.chatSecuencial;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UDPCliente {

    public static void main(String[] args) {
        //[nombre], [ip], [puerto]
        if(args.length<3){
            System.out.println("[nombre], [ip], [puerto]");
            return;
        }
        int port= Integer.parseInt(args[2]);

        try(
                DatagramSocket datagramSocket = new DatagramSocket(port+1);
                Scanner scanner = new Scanner(System.in);
                ){

            InetAddress ip = Inet4Address.getByName(args[1]);
            String nombre = args[0];
            String mensaje = "#"+nombre+"@";
            DatagramPacket datagramPacket = new DatagramPacket(mensaje.getBytes(StandardCharsets.UTF_8),mensaje.length());
            System.out.println("Conectando...");
            datagramSocket.connect(ip,port);
            datagramSocket.send(datagramPacket);

            datagramPacket = new DatagramPacket(new byte[2000],2000);
            System.out.println("Recibiendo saludo...");
            datagramSocket.receive(datagramPacket);
            mensaje = new String(datagramPacket.getData(),0,datagramPacket.getLength());
            System.out.println(mensaje);
            Matcher m = Pattern.compile("#(\\p{Alnum}+)@").matcher(mensaje);
            m.find();
            String nombreConexion = m.group(1);
            while (true){
                System.out.print(nombre+"> ");
                mensaje = scanner.nextLine();
                if(mensaje.equals(".")){
                    datagramSocket.close();
                    return;
                }
                datagramPacket = new DatagramPacket(mensaje.getBytes(StandardCharsets.UTF_8),mensaje.length());
                datagramSocket.send(datagramPacket);
                datagramSocket.receive(datagramPacket);
                System.out.println(nombreConexion+"> "+new String(datagramPacket.getData(),0,datagramPacket.getLength()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
