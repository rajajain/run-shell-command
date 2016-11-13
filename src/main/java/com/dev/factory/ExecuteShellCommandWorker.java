package com.dev.factory;


import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;


public class ExecuteShellCommandWorker implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteShellCommandWorker.class.getCanonicalName());

    private Socket socket = null;
    private InputStream input = null;
    private OutputStream output = null;
    private Request request = null;
    private Response response = null;

    public ExecuteShellCommandWorker(Socket socket) throws IOException {
        this.socket = socket;
        this.input = socket.getInputStream();
        output = socket.getOutputStream();
        request = new Request(input);
        response = new Response(output);
        request.parse();
    }

    @Override
    public void run() {

        try {
            LOGGER.info(request.getBody());
            JSONObject jsonObject = (JSONObject) JSONValue.parse(request.getBody());

            boolean async = jsonObject.get("runInBackground") == null ? false : (boolean) jsonObject.get("runInBackground");
            JSONArray commandArr = (JSONArray) jsonObject.get("commands");
            String[] commands = new String[commandArr.size()];
            for (int i = 0; i < commandArr.size(); i++) {
                commands[i] = (String) commandArr.get(i);
            }
            String outputFilePath = (String) jsonObject.get("wirteOutputFilePath");
            if (outputFilePath == null || "".equals(outputFilePath)) {
                outputFilePath = "/tmp/runcommand.log";
            }
            if (async) {
                for (String command : commands) {
                    StringTokenizer st = new StringTokenizer(command);
                    String[] cmdarray = new String[st.countTokens()];
                    for (int i = 0; st.hasMoreTokens(); i++) {
                        cmdarray[i] = st.nextToken();
                    }
                    executeAndOutputInFile(cmdarray, outputFilePath);
                }

            } else {
                for (String command : commands) {
                    executeCommand(command, outputFilePath);
                }
            }
            response.sendSuccess();
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void executeAndOutputInFile(String[] command, String outputFilePath) {
        LOGGER.info("Running commenad :{}", command);
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectOutput(new File(outputFilePath));
        try {
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void executeCommand(String command, String outputFilePath) {
        LOGGER.info("Running commenad : {}", command);
        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FileUtils.writeStringToFile(new File(outputFilePath), output.toString());
        } catch (IOException e) {
//            return output.toString();
            e.printStackTrace();
        }

    }

}
