/**
 * Versión echo con TCP para el cliente pero 
 * @author: Santiago Rodenas Herráiz
 * @version: 5/12/2021
 * @param: Acepta el puerto e ip del servidor
 * 
 */

package tcp.explicacion;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClienteTcp {
    public static final Scanner ioS = new Scanner(System.in);
   

    
    public static void main(String[] args) {
        int numPuertoServidor;
        String hostServidor;
        Socket socketComunicacion;
        InetAddress ipServidor;
        String echo;
        
        if (args.length <2){
            System.out.println("Error, debes pasar el puerto del servidor y host servidor");
            System.exit(1);
        }

        numPuertoServidor= Integer.parseInt(args[0]);  //puerto servidor
        hostServidor = args[1];         //ip servidor
        try{
            //Creamos el socket del servidor.

            
                socketComunicacion = new Socket(hostServidor, numPuertoServidor);
                ipServidor=socketComunicacion.getInetAddress();
                System.out.printf("Cliente conectado con servidor %s...%n",ipServidor.getHostAddress());

                Scanner br = new Scanner (socketComunicacion.getInputStream());
                PrintWriter pw = new PrintWriter(socketComunicacion.getOutputStream());
                System.out.print(">");

                while ( (echo=ioS.nextLine()).length()>0 ){  
                    pw.println(echo);
                    pw.flush();
                    System.out.println("Ya he mandado al socket");
                    System.out.printf("Respuesta: %s%n", br.nextLine());
                    System.out.print(">");       
                }
        }  
        catch (UnknownHostException ex){
            System.out.printf("Servidor desconocido %s%n", hostServidor);
            ex.printStackTrace();
            System.exit(2);
        } 
        catch (IOException e){
            System.out.println("Error en flujo de E/S");
            e.printStackTrace();
            System.exit(1);
        }
       
     }   //fin main
}  //fin clase
