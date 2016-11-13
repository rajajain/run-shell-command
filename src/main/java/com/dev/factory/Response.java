package com.dev.factory;

import java.io.IOException;
import java.io.OutputStream;


public class Response {

    private OutputStream output;

    public Response(OutputStream output) {
        this.output = output;
    }

    public void setInvalidRequest() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 404 Not Found").append("\r\n");
        sb.append("Content-Type: text/text").append("\r\n");
        output.write(sb.toString().getBytes());
    }

    public void sendSuccess() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK").append("\r\n");
        sb.append("Content-Type: text/json").append("\r\n");
        sb.append("\r\n");
        sb.append("{\"Command Execution\":\"Done\"}");
        output.write(sb.toString().getBytes());
    }

}
