/*
 * The MIT License
 *
 * Copyright (C) 2015 by Brian Ford, Xamarin Inc.
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

import org.jenkinsci.plugins.publishoveronedrive.domain.model.RestException;
import org.jenkinsci.plugins.publishoveronedrive.domain.model.TokenResponse;
import com.google.gson.Gson;
import de.tuberlin.onedrivesdk.OneDriveException;
import de.tuberlin.onedrivesdk.OneDriveFactory;
import de.tuberlin.onedrivesdk.OneDriveSDK;
import de.tuberlin.onedrivesdk.common.OneDriveScope;
import de.tuberlin.onedrivesdk.file.OneFile;
import de.tuberlin.onedrivesdk.folder.OneFolder;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.plugins.publishoveronedrive.impl.Messages;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OneDrive {

    private static final Gson gson = new Gson();
    private final String accessToken;
    private int timeout = -1;
    private OneFolder workingFolder;
    private static final OneDriveSDK sdk = OneDriveFactory.createOneDriveSDK(Config.CLIENT_ID, Config.CLIENT_SECRET, Config.REDIRECT_URI, OneDriveScope.READWRITE);

    public OneDrive(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getTimeout() {
        return timeout;
    }

    public boolean connect() throws IOException, OneDriveException {
        System.out.println("*** connect");
        try {
            sdk.authenticateWithRefreshToken(accessToken);
        } catch (OneDriveException ex) {
            throw new OneDriveException("Error connecting to OneDrive, could not be authenticated." + ex);
        }
        return isConnected();
    }

    public boolean isConnected() {
        return sdk.isAuthenticated();
    }

    public boolean changeWorkingDirectory(String path) throws IOException, RestException, OneDriveException {

        System.out.println("*** changeWorkingDirectory: " + path);

        boolean success = true;
        try {
            if (!StringUtils.isEmpty(path)) {
                workingFolder = retrieveFolder(path);
            }
        } catch (IOException e) {
            success = false;
        }
        return success && workingFolder != null;
    }

    public boolean disconnect() throws OneDriveException, IOException {
        sdk.disconnect();
        return true;
    }

    private OneFolder retrieveFolder(String path) throws RestException, IOException, OneDriveException {
        OneFolder folder = sdk.getFolderByPath(path);
        return folder;
    }

    public OneFolder makeDirectory(String dirName) throws IOException, OneDriveException {
        System.out.println("*** makeDirectory: " + dirName);
        OneFolder folder = sdk.getRootFolder().createFolder(dirName);
        return folder;
    }

    public boolean storeFile(String path) throws IOException {
        System.out.println("*** storeFile " + path);

        java.io.File file = new java.io.File(path);

        try {
            sdk.getFolderById(workingFolder.getId()).uploadFile(file).startUpload();
        } catch (OneDriveException ex) {
            Logger.getLogger(OneDrive.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException io) {
            Logger.getLogger(OneDrive.class.getName()).log(Level.SEVERE, null, io);
        }
        return true;
    }

    public void cleanFolder() throws IOException, OneDriveException {
        String id = workingFolder.getId();
        List<OneFile> files = sdk.getFolderById(id).getChildFiles();
        for (OneFile file : files) {
            file.delete();
        }
    }

    public static String convertAuthorizationToAccessCode(String authorizationCode) throws IOException {
        if (StringUtils.isEmpty(authorizationCode)) {
            return "";
        }
        String accessToken = null;
        if (accessToken == null) {
            accessToken = readAccessTokenFromWeb(authorizationCode);
        }

        return accessToken;
    }

    private static String readAccessTokenFromWeb(String authorizationCode) throws RestException, UnsupportedEncodingException {
        String accessToken;
        URL url = getUrl(Config.URL_TOKEN);
        FormBuilder builder = new FormBuilder()
                .appendQueryParameter("client_id", Config.CLIENT_ID)
                .appendQueryParameter("client_secret", Config.CLIENT_SECRET)
                .appendQueryParameter("code", authorizationCode)
                .appendQueryParameter("grant_type", Config.VALUE_AUTHORIZATION_CODE)
                .appendQueryParameter("redirect_uri", Config.REDIRECT_URI);

        String body = builder.build();
        String contentType = FormBuilder.CONTENT_TYPE;
        JsonObjectRequest<TokenResponse> request = new JsonObjectRequest<TokenResponse>(url, body, contentType, gson, TokenResponse.class);
        TokenResponse response = request.execute();
        accessToken = response.getAccessToken();
        return accessToken;
    }

    private static URL getUrl(String urlSource) throws RestException {
        URL url;
        try {
            url = new URLBuilder(urlSource).build();
        } catch (URISyntaxException e) {
            throw new RestException(Messages.exception_onedrive_url(), e);
        } catch (MalformedURLException e) {
            throw new RestException(Messages.exception_onedrive_url(), e);
        }
        return url;
    }
}
