/*
 * Versión echo con TCP Multihilo pero 
 * @author: Santiago Rodenas Herráiz
 * @version: 5/12/2021
 * @param: Acepta el puerto 
 * 
 */

package tcp.explicacion;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;




/*
*Hilo que intercambia información con su cliente
*/

class ServidorMultihilo extends Thread{

    private Socket socketComunicacion;
    
    public ServidorMultihilo(Socket mSocket){
        socketComunicacion = mSocket;
    }

    @Override
    public void run(){
        InetAddress ipCliente;


        try{

            Scanner br = new Scanner (socketComunicacion.getInputStream());
            PrintWriter pw = new PrintWriter(socketComunicacion.getOutputStream());

            System.out.println("Ahora esperamos echo");
            String lineaRecibida;
            while ( (lineaRecibida=br.nextLine())!=null && lineaRecibida.length()>0){
                ipCliente = socketComunicacion.getInetAddress();
                System.out.printf("Recibo desde %s por puerto %d> %s%n",ipCliente.getHostAddress(), socketComunicacion.getPort(), lineaRecibida);
                lineaRecibida = "#"+lineaRecibida+"#";
                pw.println(lineaRecibida);  //mandamos la linea recibida modificada
                pw.flush(); //limpiamos el buffer para que se mande inmediatamente.
            }
        }
        catch(IOException e){
            System.out.println("Error en flujo de E/S");
            e.printStackTrace();
        }
        catch (NoSuchElementException e){
            System.out.println("El Cliente ha cerrado su conexión....");
            if (socketComunicacion!=null)
                try{
                    System.out.printf("Socket servidor Multihilo, cerrado%n");
                     socketComunicacion.close();
                }catch (IOException ex){
                    System.out.println("Error en flujo de E/S al cerrar el Socket una vez desconectado con cliente");
                    ex.printStackTrace();
                }
          //  System.out.println("Hilo finalizado...");
        }
    }
}




/*
*El hilo principal, se encarga de aceptar peticiones, mientras que para cada una de las
*conexiones con un cliente, se lanza un hilo de ejecución.
*Finalizará cuando no haya ningún hilo atendiendo y al principal se le haga un Ctrol + c
*/

public class ServerTcp {
 
    public static void main(String[] args) {
        int numPuertoServidor;
        ServerSocket socketServidor;
        Socket socketComunicacion=null;
        InetAddress ipCliente;
        int puertoCliente;

        if (args.length <1){
            System.out.println("Error, debes pasar el puerto del servidor");
            System.exit(1);
        }

        numPuertoServidor= Integer.parseInt(args[0]);
        try{
            socketServidor = new ServerSocket(numPuertoServidor);
            while(true) {
                socketComunicacion = socketServidor.accept();  //aceptamos la comunicación con el cliente
                ipCliente = socketComunicacion.getInetAddress();
                puertoCliente = socketComunicacion.getPort();   //puerto del Cliente
                System.out.printf("Conexión establecida con cliente con ip ......%s : puerto: %d%n", ipCliente,puertoCliente);
                ServidorMultihilo servidorH = new ServidorMultihilo(socketComunicacion);
                servidorH.start();
            }

        }   
        
        catch (IOException e){
            System.out.println("Error en flujo de E/S");
            e.printStackTrace();
        }

    }

   
}
