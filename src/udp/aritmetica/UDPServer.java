package udp.aritmetica;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UDPServer {

    static final Pattern pattern = Pattern.compile("(\\d+)\\s*([+\\-*/])\\s*(\\d+)");

    public static void main(String[] args) {
        try(DatagramSocket datagramSocket = new DatagramSocket(25566)){
            while(true) {
                DatagramPacket packet = new DatagramPacket(new byte[1000], 1000);
                datagramSocket.receive(packet);

                String data = new String(packet.getData(),0,packet.getLength());

                Matcher match = pattern.matcher(data);
                String respuesta = String.valueOf(new Date());
                if (match.find()) {
                    respuesta = respuesta.concat(" res:" + sol(match.group(1), match.group(3), match.group(2)));
                } else {
                    respuesta = respuesta.concat(" res:err");
                }
                byte[] bytes = respuesta.getBytes(StandardCharsets.UTF_8);
                packet = new DatagramPacket(
                        bytes,
                        bytes.length,
                        packet.getAddress(),
                        packet.getPort()
                );
                datagramSocket.send(packet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String sol(String valor1, String valor2, String operacion){
        switch (operacion){
            case "+" -> {
                return String.valueOf(Float.parseFloat(valor1)+Float.parseFloat(valor2));
            }
            case "-" -> {
                return String.valueOf(Float.parseFloat(valor1)-Float.parseFloat(valor2));
            }
            case "*" -> {
                return String.valueOf(Float.parseFloat(valor1)*Float.parseFloat(valor2));
            }
            case "/" -> {
                if(valor2.equals("0"))
                    return "err";
                return String.valueOf(Float.parseFloat(valor1)/Float.parseFloat(valor2));
            }
            default -> {
                return "err";
            }
        }
    }

}
