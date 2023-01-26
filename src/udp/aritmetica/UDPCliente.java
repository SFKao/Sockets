package udp.aritmetica;

import udp.Unidad;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class UDPCliente {

    public static void main(String[] args) {
        try(DatagramSocket datagramSocket = new DatagramSocket(25565)){

            String operacion = "10/3";
            byte[] bytes = operacion.getBytes(StandardCharsets.UTF_8);
            InetAddress address = InetAddress.getLoopbackAddress();
            DatagramPacket packet = new DatagramPacket(
                    bytes,
                    bytes.length,
                    address,
                    25566
            );

            datagramSocket.send(packet);
            packet = new DatagramPacket(new byte[100],100);
            datagramSocket.receive(packet);
            System.out.println(new String(packet.getData(),0,packet.getLength()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
