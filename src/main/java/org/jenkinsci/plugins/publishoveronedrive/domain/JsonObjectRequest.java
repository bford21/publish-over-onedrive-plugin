/*
 * The MIT License
 *
 * Copyright (C) 2015 by René de Groot
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugins.publishoveronedrive.domain;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.jenkinsci.plugins.publishoveronedrive.domain.model.BaseResponse;
import org.jenkinsci.plugins.publishoveronedrive.domain.model.RestException;
import org.jenkinsci.plugins.publishoveronedrive.impl.Messages;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonObjectRequest<T> {

    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String UTF_8 = "UTF-8";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";
    public static final String OCTET_STREAM = "application/octet-stream";
    public static final String PARAM_AUTHORIZATION = "Authorization";
    public static final String VALUE_BEARER = "Bearer ";
    public static final int TIMEOUT_30_SECONDS = 30000;

    private final URL url;
    private InputStream bodyStream;
    private String contentType;
    private final Gson gson;
    private final Class<T> classOfT;
    private String bearerToken;
    private int timeout = TIMEOUT_30_SECONDS;

    public JsonObjectRequest(URL url, Gson gson, Class<T> classOfT) {
        this.url = url;
        this.gson = gson;
        this.classOfT = classOfT;
        this.contentType = null;
    }

    public JsonObjectRequest(URL url, InputStream content, String contentType, Gson gson, Class<T> classOfT) {
        this(url, gson, classOfT);
        this.contentType = contentType;
        this.bodyStream = content;
    }

    public JsonObjectRequest(URL url, String content, String contentType, Gson gson, Class<T> classOfT) {
        this(url, gson, classOfT);
        this.contentType = contentType;
        try {
            bodyStream = new ByteArrayInputStream(content.getBytes(UTF_8));
        } catch (UnsupportedEncodingException e) {
            // Ignored
        }
    }

    public T execute() throws RestException {
        T model;
        HttpURLConnection connection;
        InputStream inputStream = null;
        InputStream errorStream = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(timeout);
            connection.setConnectTimeout(timeout);

            if (bearerToken != null) {
                signWithBearerToken(connection);
            }

            if (bodyStream != null) {
                upload(connection, METHOD_POST);
            } else {
                download(connection);
            }

            int responseCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();
            if (responseCode < 200 || responseCode > 299) {
                errorStream = connection.getErrorStream();
                model = readModel(errorStream);
                String description = "";
                if (model instanceof BaseResponse && ((BaseResponse) model).hasError()) {
                    description = " : " + ((BaseResponse) model).getErrorDescription();
                }
                throw new RestException(Messages.exception_rest_http(responseCode, responseMessage, description));
            }
            inputStream = connection.getInputStream();
            model = readModel(inputStream);
        } catch (IOException e) {
            throw new RestException(Messages.exception_rest_connection(), e);
        } finally {
            closeQuietly(errorStream);
            closeQuietly(inputStream);
        }
        if (model == null) {
            throw new RestException(Messages.exception_rest_model());
        }

        return model;
    }

    private void signWithBearerToken(HttpURLConnection connection) {
        connection.setRequestProperty(PARAM_AUTHORIZATION, VALUE_BEARER + bearerToken);
    }

    private void download(HttpURLConnection connection) throws IOException {
        connection.setRequestMethod(METHOD_GET);
        connection.setDoOutput(false);
    }

    private void upload(HttpURLConnection connection, String method) throws IOException {
        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        if (contentType != null) {
            connection.addRequestProperty(HEADER_CONTENT_TYPE, contentType);
        } else {
            // Leaving content type null will result in malformed requests, not setting it will result in an incorrect value
            connection.addRequestProperty(HEADER_CONTENT_TYPE, OCTET_STREAM);
        }
        DataOutputStream outputStream = null;
        try {
            OutputStream stream = connection.getOutputStream();
            outputStream = new DataOutputStream(stream);
            IOUtils.copy(bodyStream, outputStream);
            outputStream.flush();
        } finally {
            closeQuietly(bodyStream);
            closeQuietly(outputStream);
        }
    }

    private void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                //Ignored
            }
        }
    }

    private T readModel(InputStream inputStream) throws IOException {
        T model = null;
        if (inputStream != null) {
            InputStreamReader reader = null;
            try {
                reader = new InputStreamReader(inputStream);
                model = gson.fromJson(reader, classOfT);
            } finally {
                closeQuietly(reader);
            }
        }
        return model;
    }

    public void sign(String accessCode) {
        this.bearerToken = accessCode;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
