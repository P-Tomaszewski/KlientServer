/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.security.*;
import java.math.*;



/**
 *
 * @author s17579
 */
public class Klient {

    private  static int Server_PORT;
    public  static String numer = "";
    private final static String  Name  = "KSer[I]";


    public static void log(String message){
        System.out.println(Name+": "+ message);
        System.out.flush();
    }

    private static String getClientInfo(Socket clientSocket) {
        String clientIP = clientSocket.getInetAddress().toString();
        int clientPort = clientSocket.getPort();
        return clientIP + ":" + clientPort;

    }

    static ServerSocket welcomeSocket;



    static {
        try {
            welcomeSocket = new ServerSocket(Server_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //****************************************************************
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {


        if(args.length > 0) {
            numer = args[0];
        }
        System.out.println("Klient F1 " + numer);

        log("start serrK");

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        String tekst = "";

        Runnable task = () -> {


            boolean serverRunning = true;
            int port;
            while (serverRunning) {



                Socket clientSocket = null;
                try {
                    clientSocket = welcomeSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    ClientServerOperation(clientSocket);
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };

        Thread newThread = new Thread(task);
        newThread.start();

        System.out.println("TORRENT P_Tomaszewski \n Opis Komend: \n 1 - Wysylanie swojej listy plikow i portu na serwer \n 2 - Pobieranie listy plikow z serwera " +
                "\n 3 - Pobranie pliku z konkretnego klienta. Wczesniej nalezy pobrac liste z serwera zeby wiedziec gdzie jest jaki plik");

        while(!tekst.equals("done")){



            System.out.print("Czekam na twój ruch: ");
            tekst =inFromUser.readLine();
            System.out.println(tekst);


            switch (tekst) {
                case "1":
                    try {
                        ServerConncet();
                        System.out.println("Wyslano liste na serwer");
                    } catch (IOException e)
                    {
                        System.out.println("Serwer nie odpowiada.");
                    }
                    break;
                case "2":
                    try {
                        getList();
                        System.out.println("Pobrano liste plikow z serwera");
                    } catch (IOException e)
                    {
                        System.out.println("Serwer nie odpowiada.");
                    }
                    break;
                case "3":
                    System.out.println("podaj nazwe pliku");
                    String plik = inFromUser.readLine();
                    System.out.println("podaj port serwera z ktorega bedziesz pobieral");
                    String port = inFromUser.readLine();
                    try {
                        ConnectToClientServer(port, plik);
                    } catch (IOException e)
                    {
                        System.out.println("Błędny port lub nazwa pliku");
                    }
                    break;
                case "done":

                    break;
                default: {
                    System.out.println("Brak takiej komendy");
                }
            }

        }

        System.out.println("Klient zakonczyl dzialanie");

        System.exit(0);





    }
    //*******************************************


    static void ServerConncet() throws IOException {
        BufferedReader in = null;
        BufferedWriter out = null;
        int ServerPort = 4444;
        String serverName = "localhost";
        String nazwa;


        InetAddress serverAddress = InetAddress.getByName(serverName);
        Socket clientSocket = new Socket(serverAddress, ServerPort);

        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));



        String zdanie = Text();
        out.write("lista");
        out.newLine();
        out.flush();
        out.write(zdanie);
        out.newLine();
        out.flush();
        out.write("" + welcomeSocket.getLocalPort());
        out.newLine();
        out.flush();

        in.close();
        out.close();
        clientSocket.close();
    }

    static void  ClientServerOperation(Socket connectionClient) throws IOException {
        Socket clientSocket = connectionClient;
        BufferedReader in = null;
        BufferedWriter out = null;



        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

        String slowo;
        slowo = in.readLine();
        Upload(slowo);
        out.write("wyslano");
        out.newLine();
        out.flush();


        in.close();
        out.close();
        clientSocket.close();

        System.out.println("Polaczenie zakonczono");




    }







    static void ConnectToClientServer(String port, String plik) throws IOException {
        BufferedReader in = null;
        BufferedWriter out = null;
        int ServerPort = Integer.parseInt(port);
        String serverName = "localhost";
        String nazwa = plik;


        InetAddress serverAddress = InetAddress.getByName(serverName);
        Socket clientSocket = new Socket(serverAddress, ServerPort);

        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

        String slowo ;
        out.write(plik);
        out.newLine();
        out.flush();
        Downolad(nazwa);
        slowo = in.readLine();
        System.out.println(slowo);

        in.close();
        out.close();
        clientSocket.close();

    }

    static void getList() throws IOException {
        BufferedReader in = null;
        BufferedWriter out = null;
        int ServerPort = 4444;
        String serverName = "localhost";



        InetAddress serverAddress = InetAddress.getByName(serverName);
        Socket clientSocket = new Socket(serverAddress, ServerPort);

        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

        out.write("getList");
        out.newLine();
        out.flush();
        System.out.println(in.readLine());

        in.close();
        out.close();
        clientSocket.close();

    }
    static void Upload(String name) throws IOException {

        String nazwa = name;
        String SRName = "localhost";
        Socket socket = new Socket(SRName, 11111);
        InputStream in = new FileInputStream("Klient" + numer + "/" + nazwa);
        OutputStream out = socket.getOutputStream();
        byte[] buf = new byte[1024];
        int len = 0;
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
        socket.close();
    }

    static void Downolad(String slowo) throws IOException{



        String nazwa = slowo;
        ServerSocket ss = new ServerSocket(11111);
        Socket socket = ss.accept();
        InputStream in = socket.getInputStream();
        OutputStream out = new FileOutputStream("Klient" + numer + "/" + nazwa);
        byte[] buf = new byte[1024];
        int len = 0;
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }

        in.close();
        out.close();
        ss.close();
        socket.close();
    }



    public static boolean testChecksum(String file, String testChecksum) throws NoSuchAlgorithmException, IOException
    {
        String nazwa = file;
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        FileInputStream fis = new FileInputStream("Klient" + numer + "/" + nazwa);

        byte[] data = new byte[1024];
        int read = 0;
        while ((read = fis.read(data)) != -1) {
            md5.update(data, 0, read);
        };
        byte[] hashBytes = md5.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hashBytes.length; i++) {
            sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        String fileHash = sb.toString();

        return fileHash.equals(testChecksum);
    }



    private static String getFileChecksum(String slowo) throws IOException
    {
        String nazwa = slowo;

        FileInputStream fis = new FileInputStream("Klient" + numer + "/" + nazwa);


        byte[] byteArray = new byte[1024];
        int bytesCount = 0;
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        };


        fis.close();


        byte[] bytes = digest.digest();


        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }


        return sb.toString();
    }

    static String Text() {

        FileReader fr = null;
        String linia = "";
        String wynik = "";

        // OTWIERANIE PLIKU:
        try {
            fr = new FileReader("Klient" + numer + "/" + "lista.txt");
        } catch (FileNotFoundException e) {
            System.out.println("BLAD PRZY OTWIERANIU PLIKU!");
            System.exit(1);
        }

        BufferedReader bfr = new BufferedReader(fr);
        // ODCZYT KOLEJNYCH LINII Z PLIKU:
        try {
            while ((linia = bfr.readLine()) != null) {
                wynik += " PlikKlientaNumer: ["+ numer + "]" + linia + " SumaMD5: -> " + getFileChecksum(linia) + "|";

            }
        } catch (IOException e) {
            System.out.println("BLAD ODCZYTU Z PLIKU!");
            System.exit(2);
        }

        // ZAMYKANIE PLIKU
        try {
            fr.close();
        } catch (IOException e) {
            System.out.println("BLAD PRZY ZAMYKANIU PLIKU!");

        }
        return wynik ;
    }

}







