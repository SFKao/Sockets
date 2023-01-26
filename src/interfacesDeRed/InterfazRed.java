package interfacesDeRed;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class InterfazRed {

    public static void main(String[] args) {
        ej4();
    }

    public static void ej4(){
        try {
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while(networkInterfaceEnumeration.hasMoreElements()){
                NetworkInterface e = networkInterfaceEnumeration.nextElement();
                List<InterfaceAddress> interfaceAddresses = e.getInterfaceAddresses();
                for (InterfaceAddress interfaceAddress : interfaceAddresses) {
                    if(interfaceAddress.getAddress()!=null && interfaceAddress.getAddress() instanceof Inet4Address)
                        System.out.printf("\tAddress: %s,Broadcast: %s%n", interfaceAddress.getAddress().getHostAddress(), interfaceAddress.getBroadcast() != null ? interfaceAddress.getBroadcast().getHostAddress() : null);
                }
            }


        } catch (SocketException e) {
            System.out.println(e);
        }
    }

    public static void ej3(){
        try{
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaceEnumeration.hasMoreElements()){
                NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
                if(networkInterface.getHardwareAddress()!=null){
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                    while (inetAddresses.hasMoreElements()){
                        InetAddress inetAddress = inetAddresses.nextElement();
                        if(inetAddress instanceof Inet4Address){
                            System.out.println(inetAddress.getHostAddress());
                            if(inetAddress.getHostAddress().startsWith("192.168.")){
                                byte[] address = inetAddress.getAddress();
                                byte[] addressToCheck = new byte[address.length];
                                for (int j = 0; j < addressToCheck.length; j++) {
                                    addressToCheck[j] = address[j];
                                }
                                for(byte i = -127; i > -128;i++){
                                    addressToCheck[3] = i;
                                    System.out.print(Arrays.toString(addressToCheck));
                                    if(InetAddress.getByAddress(addressToCheck).isReachable(10)){
                                        System.out.println("  alcanzada");
                                    }else{
                                        System.out.println("  no alcanzada");
                                    }

                                }
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            System.out.println(e);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void ej2(){
        try {
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while(networkInterfaceEnumeration.hasMoreElements()){
                NetworkInterface e = networkInterfaceEnumeration.nextElement();
                if(true) {
                    String macAddress = null;
                    if(e.getHardwareAddress()!=null) {
                        String[] hexadecimal = new String[e.getHardwareAddress().length];
                        for (int i = 0; i < e.getHardwareAddress().length; i++) {
                            hexadecimal[i] = String.format("%02X", e.getHardwareAddress()[i]);
                        }
                        macAddress = String.join(":", hexadecimal);
                    }
                    System.out.printf("Nombre: %s, interfaz: %s, MAC: %s, MTU: %d%n", e.getDisplayName(), e.getName(), macAddress, e.getMTU());
                    System.out.printf("Es virutal: %b, es loopback: %b, soporta multicast: %b%n", e.isVirtual(),e.isLoopback(),e.supportsMulticast());
                    Enumeration<InetAddress> inetAddresses = e.getInetAddresses();
                    System.out.println("INetAdresses: ");
                    while(inetAddresses.hasMoreElements()){
                        InetAddress inetAddress = inetAddresses.nextElement();
                        if(inetAddress instanceof Inet4Address)
                            System.out.printf("\tAddress:\t %s%n",inetAddress.getHostAddress());
                        else
                            System.out.printf("\tIPv6:\t\t %s%n",inetAddress.getHostAddress());

                    }
                    System.out.println("InterfaceAdresses: ");
                    List<InterfaceAddress> interfaceAddresses = e.getInterfaceAddresses();
                    for (InterfaceAddress interfaceAddress : interfaceAddresses) {
                        System.out.printf("\tAddress: %s,Broadcast: %s, Mascara: %s %n", interfaceAddress.getAddress().getHostAddress(), interfaceAddress.getBroadcast() != null ? interfaceAddress.getBroadcast().getHostAddress() : null, printMask(interfaceAddress.getNetworkPrefixLength()));
                    }
                    System.out.println();
                }



            }


        } catch (SocketException e) {
            System.out.println(e);
        }}

    public static String printMask(short i){
        if(i>32)
            return null;
        String s = "";
        int extras = 4-(i/8);
        while (i>=8){
            s = s.concat("255.");
            i-=8;
        }
        while (extras>0){
            s = s.concat("0.");
            extras--;
        }
        return s.substring(0,s.length()-1);

    }

}
