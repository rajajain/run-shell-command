package com.dev.factory;

import java.io.IOException;
import java.io.InputStream;

public class Request {
    private InputStream input;
    private String uri;
    private String method;
    private String body;

    public Request(InputStream input) {
        this.input = input;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void parse() {
        StringBuilder sb = new StringBuilder();
        int in;
        byte[] buffer = new byte[2048];
        try {
            in = input.read(buffer);
        } catch (IOException ex) {
            in = -1;
        }
        for (int i = 0; i < in; ++i) {
            sb.append((char) buffer[i]);
        }
        method = findMethod(sb.toString());
        uri = findUri(sb.toString());
        body = parseRequestBody(sb.toString(), method);
    }

    private String parseRequestBody(String request, String method) {
        int start = request.indexOf("{");
        int end = request.lastIndexOf("}");
        String body = request.substring(start, end + 1);
        return body;
    }

    private String findMethod(String request) {
        if (request == null || request.isEmpty()) {
            return null;
        }
        int idx1 = request.indexOf(' ');
        return request.substring(0, idx1);
    }

    private String findUri(String request) {
        if (request == null || request.isEmpty()) {
            return null;
        }
        int idx1 = request.indexOf(' ');
        if (idx1 > 0) {
            int idx2 = request.indexOf(' ', idx1 + 1);
            if (idx2 > idx1) {
                return request.substring(idx1 + 1, idx2);
            }
        }
        return null;
    }

    public boolean isGetMethod() {
        return method != null && method.equalsIgnoreCase("GET");
    }

    public boolean isPostMethod() {
        return method != null && method.equalsIgnoreCase("POST");
    }

}
