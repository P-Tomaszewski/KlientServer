/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author s17579
 */
public class Serwer {
    private final static int Server_PORT = 4444;
    private final static String Name = "[Ser]";
    public static String[] tab = new String[10];
    static String[] lista = new String[100];
    static String [] porty = new String [100];
    public static int licznik = 0, licznik2 =0 ;
    public static Map<Integer, String> myMap = new HashMap<Integer,  String>();

    public static void log(String message) {
        System.out.println(Name + ": " + message);
        System.out.flush();
    }

    private static int getPort(Socket clientSocket) {
        int clientPort = clientSocket.getPort();
        return clientPort;

    }

    static ServerSocket welcomeSocket;

    static {
        try {
            welcomeSocket = new ServerSocket(Server_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // *****************************************************************
    public static void main(String[] args) throws IOException {

        log("Start");
        log("Server socket creation");


        Runnable task = () -> {


            boolean serverRunning = true;
            int port;
            while (serverRunning) {


                log("socket listening");
                Socket clientSocket = null;
                try {
                    clientSocket = welcomeSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                log("Client port:" + getPort(clientSocket));
                log("Streams collecting");
                //  RequestHandler2.sleep(2000);
                //new RequestHandler(clientSocket).start();
                try {
                    ServerOperation(clientSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };

        Thread newThread = new Thread(task);
        newThread.start();


    }

    // *****************************************************************
    public static void ServerOperation(Socket connection) throws IOException {
        Socket clientSocket = connection;
        BufferedReader in = null;
        BufferedWriter out = null;
        boolean connections2 = true;


        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

        String slowo;
        while (connections2 == true) {
            slowo = in.readLine();
            System.out.println(slowo);
            if (slowo.equals("lista")) {
                slowo = in.readLine();
                lista[licznik] = slowo;
                slowo = in.readLine();
                porty[licznik] = "" + slowo;
                licznik++;

            }

            if(slowo.equals("getList"))
            {
                String listaIporty = "";
                for(int k = 0; k<licznik; k++){
                    listaIporty += "| " + lista[k] + " -> Port:" + porty[k] + " | " ;
                }
                out.write(listaIporty);
                out.newLine();
                out.flush();
            }


            connections2 = false;

        }


    }






}
