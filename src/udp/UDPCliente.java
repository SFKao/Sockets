package udp;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class UDPCliente {

    public static void main(String[] args) {
        clienteTexto();
    }

    public static void clienteObjeto(){
        try(
                DatagramSocket datagramSocket = new DatagramSocket();
        ){
            Unidad unidad = new Unidad(7.5f,"Tanque",7,9, "OS");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
            objectOutputStream.writeObject(unidad);
            DatagramPacket datoEnviado = new DatagramPacket(
                    out.toByteArray(),
                    out.size(),
                    Inet4Address.getLoopbackAddress(),
                    26655
            );
            datagramSocket.send(datoEnviado);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clienteTexto(){
        try (
                DatagramSocket datagramSocket = new DatagramSocket(26656);
        ){
            String saludos_de_vuelta = "Hola soy Oscar y quiero saludarle";
            byte[] bytes = saludos_de_vuelta.getBytes(StandardCharsets.UTF_8);

            InetAddress ip = Inet4Address.getLoopbackAddress();
            int port = 26655;
            DatagramPacket dato = new DatagramPacket(
                    bytes,
                    bytes.length,
                    ip,
                    port
            );
            datagramSocket.connect(ip,port);
            datagramSocket.send(dato);

            DatagramPacket respuesta = new DatagramPacket(
                    new byte[100],100
            );
            datagramSocket.receive(respuesta);
            System.out.println(new String(respuesta.getData(),0, respuesta.getLength()));

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
