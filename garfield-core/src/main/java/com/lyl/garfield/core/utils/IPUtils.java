package com.lyl.garfield.core.utils;

import com.lyl.garfield.core.exception.AgentException;

import java.net.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

public class IPUtils {

    private static Collection<InetAddress> getAllHostAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            Collection<InetAddress> addresses = new ArrayList<InetAddress>();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    addresses.add(inetAddress);
                }
            }

            return addresses;
        } catch (SocketException e) {
            throw new AgentException(e.getMessage(), e);
        }
    }

    public static Collection<String> getAllIpv4NoLoopbackAddresses() {
        Collection<String> noLoopbackAddresses = new ArrayList<String>();
        Collection<InetAddress> allInetAddresses = getAllHostAddress();
        for (InetAddress address : allInetAddresses) {
            if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                noLoopbackAddresses.add(address.getHostAddress());
            }
        }
        return noLoopbackAddresses;
    }

    public static String getLocalHostName() {
        try {
            InetAddress netAddress = InetAddress.getLocalHost();
            return netAddress.getHostName();
        } catch (UnknownHostException e) {
            return "";
        }
    }

    private static String getMACAddress(InetAddress ia) throws Exception {
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            String s = Integer.toHexString(mac[i] & 0xFF);
            sb.append(s.length() == 1 ? 0 + s : s);
        }

        return sb.toString().toUpperCase();
    }

    public static String getMACAddress(){
        Collection<InetAddress> allInetAddresses = getAllHostAddress();
        try {
            for (InetAddress address : allInetAddresses) {
                if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                    String mac = getMACAddress(address);
                    if (mac != null && !mac.equals("")) {
                        return mac;
                    }
                }
            }
        }catch (Exception e){
            // ignore
        }
        return "";
    }


}
