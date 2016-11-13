package com.dev.factory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by raja on 12/11/16.
 */
public class ExecuteShellCommand {

    public static void main() throws Exception {
        ServerSocket ss = new ServerSocket(3333);
        Socket s = ss.accept();
        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String str = "", str2 = "";
        while (!str.equals("stop")) {
            str = din.readUTF();
            System.out.println("client says: " + str);
            str2 = br.readLine();
            dout.writeUTF(str2);
            dout.flush();
        }
        din.close();
        s.close();
        ss.close();
    }


//        ExecuteShellCommand obj = new ExecuteShellCommand();
//
//        //String command = "curl -i -X GET --header \"Content-Type: application/json\" --header \"AUTH_TOKEN:c9e4eeae8616c591b2f02a21ed197cf8\" \"http://api-nap.airtel.in/v1/segment/segment-result-by-id?segmentId=d1f1d758-1a90-4ac5-990b-d8a375fa8d7e&pageSize=1&pageNo=1\"";
//        String command = "/Users/raja/fe_1";
//
//        String output = obj.executeAndOutputInFile(command);
//
////        String output = obj.executeCommand(command);
//        System.out.println(output);
//
//    }


    private String executeAndOutputInFile(String command) {
        ProcessBuilder processBuilder = new ProcessBuilder("sh", command);
        processBuilder.redirectOutput(new File("/tmp/report1.txt"));
        try {
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String executeCommand(String command) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
//            p.waitFor();
//            BufferedReader reader =
//                    new BufferedReader(new InputStreamReader(p.getInputStream()));
//
//            String line = "";
//            while ((line = reader.readLine()) != null) {
//                output.append(line + "\n");
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();

    }

}
