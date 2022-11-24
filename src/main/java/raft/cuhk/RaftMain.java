package raft.cuhk;

import java.io.*;
import java.net.URL;
import java.util.Properties;

public class RaftMain {
    static String[] replication_connection;
    static String localhost;
    static int lport;
    static RaftServer server;

    public static void main(String[] args) {
        System.out.println("Raft-KV-Platform starts....");
        load_config();
        System.out.println("Load config successful....");
        start_service();




    }

    static void start_service(){
        server = new RaftServer(replication_connection, localhost, lport);
        try {
            server.start();
            server.blockUntilShutdown();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }
    static void load_config(){
        URL url = RaftMain.class.getClassLoader().getResource("config.properties");
        if (url != null) {
            String fileName = url.getFile();
            InputStream in = null;
            try {
                in = new BufferedInputStream(new FileInputStream(fileName));
                Properties properties = new Properties();
                properties.load(in);
                int conn = Integer.parseInt(properties.getProperty("replication"));
                replication_connection = new String[conn];
                for (int i = 1; i <= conn; i++) {
                    replication_connection[i-1] = properties.getProperty("machine"+i);
                }
                String[] ss = properties.getProperty("localhost").split(":");
                localhost = ss[0];
                lport = Integer.parseInt(ss[1]);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
