/*
* Copyright (c) 2004 David Flanagan.  All rights reserved.
* This code is from the book Java Examples in a Nutshell, 3nd Edition.
* It is provided AS-IS, WITHOUT ANY WARRANTY either expressed or implied.
* You may study, use, and modify it for any non-commercial purpose,
* including teaching and use in open-source projects.
* You may distribute it non-commercially as long as you retain this notice.
* For a commercial use license, or to purchase the book, 
* please visit http://www.davidflanagan.com/javaexamples3.
*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.StringBuilder;
import java.util.*;
import org.json.*;

public class Server implements Runnable{
    Socket client;
    public Server(Socket sck) {
        this.client = sck;
    }

    public static class StockObj {
        int type;
        int association;
        int timeWindow;
        int subId;
        double value;
        String gcm;
        ArrayList<String> stocks;
        public StockObj(int type, int association, int timeWindow, int subId, double value, String gcm, ArrayList<String> stocks) {
            this.type = type;
            this.association = association;
            this.timeWindow = timeWindow;
            this.subId = subId;
            this.value = value;
            this.gcm = gcm;
            this.stocks = stocks;
        }

        public int getType() { return type; }
        public int getAssociation() { return association; }
        public int getTimeWindow() { return timeWindow; }
        public int getSubId() {return subId; }
        public double getValue() { return value; }
        public String getGcm() { return gcm; }
        public ArrayList<String> getSymbols() {return stocks; }
        public String getSymbol(int i) { 
            String str =  (i > (stocks.size() - 1)) ? null : stocks.get(i); 
            return str;
        }
    }

    //This gets the event info from the json string
    public static StockObj parseJsonString(String str){
        ArrayList<String> symbs;
        int type;
        int association;
        int timeWindow;
        int subId;
        double val;
        String gcm;
        try {
            JSONObject json = new JSONObject(str);
            
            JSONArray arr = (JSONArray) json.getJSONArray("Symbol");
            symbs = new ArrayList<String>();

            for(int i = 0; i < arr.length(); i++) {
                symbs.add((String)arr.get(i));
            }

            type = (Integer)json.get("type");
            timeWindow = (Integer)json.get("timewindow");
            association = (Integer)json.get("association");
            subId = (Integer)json.get("subId");
            val = (Double)json.get("value");
            gcm = (String)json.get("gcm");
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }

        return new StockObj(type, association, timeWindow, subId, val, gcm, symbs);
    }
    

    public static void main(String args[]) {
        try {
            // Get the port to listen on
            if(args.length != 1) {
                System.err.println("Usage: java Server <port > 1023>");
                return;
            }

            int port = Integer.parseInt(args[0]);
            // Create a ServerSocket to listen on that port.
            ServerSocket ss = new ServerSocket(port);
            // Now enter an infinite loop, waiting for & handling connections.
            while(true) {
                // Wait for a client to connect. The method will block;
                // when it returns the socket will be connected to the client
                Socket client = ss.accept();

                System.out.println("Connection!");
                new Thread(new Server(client)).start();
                } // Now loop again, waiting for the next connection
            }
            // If anything goes wrong, print an error message
            catch (Exception e) {
                System.err.println(e);
                System.err.println("Usage: java Server <port > 1023>");
            }
        }

        public void run() {
            try {
                // Get input streams to read from the client
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                String line;
                StringBuilder info = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    if (line.length() == 0)
                        break;
                    info.append(line);
                }

                System.out.println(client.isConnected());

                StockObj stk = parseJsonString(info.toString());
                System.out.print("Type:" + stk.getType() + " timewindow:" + stk.getTimeWindow() 
                    + " association:" + stk.getAssociation() + " Value:" + stk.getValue() + " SubId:" + 
                    stk.getSubId() + " GCM:" + stk.getGcm() +" Stock Symbols:");
                for(String str : stk.getSymbols()) 
                    System.out.print(str + " ");
                // Close socket, breaking the connection to the client, and
                // closing the inputstreams
                if(stk.getSymbols().size() == 1)
                	exampleMain.createStreamQuery(stk.getSymbol(0), stk.getType(), stk.getValue(),stk.getTimeWindow(), 
                			stk.getAssociation(), stk.getGcm(), stk.getSubId());
                in.close(); // Close the input stream
                client.close(); // Close the socket itself
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}