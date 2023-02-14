package postmanDeLaMuerte;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Servidor {

    public static void main(String[] args) {

        int puerto = 5000;
        int tam = 100;
        Almacen almacen = new Almacen(tam);
        try(
                ServerSocket serverSocket = new ServerSocket(puerto)
                ){
            while(true)
                new SocketServidor(serverSocket.accept(),almacen).start();
        } catch (IOException e) {
            System.out.println("Error abriendo el servidor");
            e.printStackTrace();
            System.exit(1);
        }

    }

    private static class SocketServidor extends Thread{

        //El socket para comunicarme con el cliente
        Socket socket;

        //El objeto compartido
        Almacen almacen;

        //Los regex de los comandos

        static final Pattern COMANDOGETALL = Pattern.compile("^GET$");
        static final Pattern COMANDOGETONE = Pattern.compile("^GET (\\d+)$");
        static final Pattern COMANDOPUSH = Pattern.compile("^POST ([^\r]*)$");
        static final Pattern COMANDOPUT = Pattern.compile("^PUT (\\d+) ([^\r]*)$");

        public SocketServidor(Socket socket, Almacen almacen) {
            this.socket = socket;
            this.almacen = almacen;
        }

        @Override
        public void run() {
            if(socket==null)
                return;
            try(
                    Scanner in = new Scanner(socket.getInputStream());
                    PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
                    ) {
                String comando;

                //Leo el comando que me llega del servidor
                while ((comando = in.nextLine())!=null && comando.length()>0){
                    System.out.println("COMANDO "+comando+" de "+socket.getInetAddress()+":"+socket.getPort());
                    String respuesta;
                    if(comando.matches(COMANDOGETALL.pattern()))
                        respuesta = almacen.getAll();
                    else if(comando.matches(COMANDOGETONE.pattern())){
                        Matcher m = COMANDOGETONE.matcher(comando);
                        if(m.find()){
                            int pos = Integer.parseInt(m.group(1));
                            respuesta = almacen.getOne(pos);
                        }else{
                            respuesta = "HUBO UN ERROR 1";
                        }
                    }else if(comando.matches(COMANDOPUSH.pattern())){
                        Matcher m = COMANDOPUSH.matcher(comando);
                        if(m.find()){
                           String insertar = m.group(1);
                           if(almacen.post(insertar))
                               respuesta = "Se inserto correctamente";
                           else
                               respuesta = "No se pudo insertar";
                        }else{
                            respuesta = "HUBO UN ERROR 2";
                        }
                    }else if(comando.matches(COMANDOPUT.pattern())){
                        Matcher m = COMANDOPUT.matcher(comando);
                        if(m.find()){
                            int pos = Integer.parseInt(m.group(1));
                            String insertar = m.group(2);
                            if(almacen.put(pos,insertar))
                                respuesta = "Se actualizo correctamente";
                            else
                                respuesta = "No se pudo actualizar";
                        }else{
                            respuesta = "HUBO UN ERROR 3";
                        }
                    }else
                        respuesta = "HUBO UN ERROR, NO SE RECONOCIO EL COMANDO";
                    System.out.println("Enviando respuesta "+respuesta+" a "+socket.getInetAddress()+":"+socket.getPort());
                    out.println(respuesta);
                }

            } catch (IOException e) {
                System.out.println("Error en el envio de mensajes");
            }finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error cerrando el socket");
                }
            }

        }
    }

    private static class Almacen{
        String[] array;
        int tamLogico = 0;

        public Almacen(int tam){
            array = new String[tam];
        }

        public synchronized String getAll(){
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < tamLogico; i++) {
                if (array[i] != null)
                    sb.append(array[i]);
                else
                    sb.append("null");
                sb.append(",");
            }
            if(sb.length()!=1)
                sb.deleteCharAt(sb.length()-1);
            sb.append("]");
            return sb.toString();
        }

        public synchronized String getOne(int pos){
            if(pos >= tamLogico)
                return "Posicion erronea";
            if(array[pos]==null)
                return "No hay dato";
            return array[pos];
        }

        public synchronized boolean post(String string){
            if(tamLogico==array.length)
                return false;
            array[tamLogico++] = string;
            return true;
        }

        public synchronized boolean put(int pos, String string){
            if(pos >= array.length)
                return false;
            if(pos >= tamLogico)
                tamLogico = pos+1;
            array[pos] = string;
            return true;
        }
    }

}
