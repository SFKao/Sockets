package udp.chatSecuencial;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UDPServidor {
    public static void main(String[] args) {
        //[nombre], [puerto]
        if(args.length<2){
            System.out.println("[nombre], [puerto]");
            return;
        }
        int port= Integer.parseInt(args[1]);

        while(true){
            try(
                    DatagramSocket datagramSocket = new DatagramSocket(port);
                    Scanner scanner = new Scanner(System.in);
            ){

                String nombre = args[0];

                DatagramPacket datagramPacket = new DatagramPacket(new byte[2000],2000);
                System.out.println("Esperando mensaje");
                datagramSocket.receive(datagramPacket);
                datagramSocket.connect(datagramPacket.getAddress(),datagramPacket.getPort());
                String mensaje = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                System.out.println(mensaje);
                Matcher m = Pattern.compile("#(\\p{Alnum}+)@").matcher(mensaje);
                if(m.find()){
                    String nombreConexion = m.group(1);
                    mensaje = "#"+nombre+"@";
                    datagramPacket = new DatagramPacket(mensaje.getBytes(StandardCharsets.UTF_8),mensaje.length());

                    datagramSocket.send(datagramPacket);

                    while (!datagramSocket.isClosed()){
                        datagramSocket.receive(datagramPacket);
                        System.out.println(nombreConexion+"> "+new String(datagramPacket.getData(),0,datagramPacket.getLength()));
                        System.out.print(nombre+"> ");
                        mensaje = scanner.nextLine();
                        if(mensaje.equals(".")){
                            datagramSocket.close();
                        }else{
                            datagramPacket = new DatagramPacket(mensaje.getBytes(StandardCharsets.UTF_8),mensaje.length());
                            datagramSocket.send(datagramPacket);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
