package udp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class UDPServer {

    public static void main(String[] args) {
        serverTexto();
    }

    private static void serverObjetos(){
        try(
                DatagramSocket datagramSocket = new DatagramSocket(26655);
        ){
            DatagramPacket datoRecibido = new DatagramPacket(new byte[300],300);
            datagramSocket.receive(datoRecibido);

            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(datoRecibido.getData()));
            System.out.println(objectInputStream.readObject());

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void serverTexto() {
        try (
                DatagramSocket datagramSocket = new DatagramSocket(26655);
        ) {
            while (true) {
                DatagramPacket dato = new DatagramPacket(
                        new byte[100], 100
                );
                datagramSocket.receive(dato);
                System.out.println(new String(dato.getData(),0, dato.getLength()));
                String saludos_de_vuelta = "Muchas gracias, yo tambien le saludo a usted";
                byte[] bytes = saludos_de_vuelta.getBytes(StandardCharsets.UTF_8);
                DatagramPacket respuesta = new DatagramPacket(
                        bytes,
                        bytes.length,
                        dato.getAddress(),
                        dato.getPort()
                );
                datagramSocket.send(respuesta);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
