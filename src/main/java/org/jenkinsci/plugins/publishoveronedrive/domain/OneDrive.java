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

import org.jenkinsci.plugins.publishoveronedrive.domain.model.RestException;
import org.jenkinsci.plugins.publishoveronedrive.domain.model.TokenResponse;
import org.jenkinsci.plugins.publishoveronedrive.domain.model.AccountInfo;
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
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;


public class OneDrive {

    private static final String URL_TOKEN = "https://login.live.com/oauth20_token.srf?";
    private static final String REDIRECT_URI = "https://login.live.com/oauth20_desktop.srf";
    
    /*
    private static final String URL_ACCOUNT_INFO = "https://api.dropbox.com/1/account/info";
    private static final String URL_FILE_METADATA = "https://api.dropbox.com/1/metadata/auto";
    private static final String URL_OPS_CREATE_FOLDER = "https://api.dropbox.com/1/fileops/create_folder";
    private static final String URL_FILE_UPLOAD = "https://api-content.dropbox.com/1/files_put/auto";
    private static final String URL_OPS_DELETE_FILE = "https://api.dropbox.com/1/fileops/delete";
    */
    
    private static final Gson gson = new Gson();
    public static final String PARAM_ROOT = "root";
    public static final String PARAM_PATH = "path";
    public static final String PARAM_LOCALE = "locale";
    public static final String PATH_SEPERATOR = "/";
    public static final String PARAM_OVERWRITE = "overwrite";
    public static final String PARAM_AUTORENAME = "autorename";
    public static final String PARAM_PARENT_REV = "parent_rev";
    public static final String VALUE_TRUE = "true";
    public static final String VALUE_FALSE = "false";
    public static final String VALUE_AUTHORIZATION_CODE = "authorization_code";
    public static final long MILLISECONDS_PER_DAY = 24 * 60 * 60 * 1000;
    private final String accessToken;
    private AccountInfo userInfo;
    private int timeout = -1;
    
    //private Folder workingFolder;
    private OneFolder workingFolder;
    
    private static final OneDriveSDK sdk = OneDriveFactory.createOneDriveSDK("https://login.live.com/oauth20_desktop.srf",OneDriveScope.READWRITE);
    
    
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
        //userInfo = retrieveAccountInfo(accessToken);
       // return isConnected();
        System.out.println ("*** connect");
        try {
            sdk.authenticateWithRefreshToken(accessToken);
        } catch(OneDriveException ex) {
            throw new OneDriveException("Error connecting to OneDrive, could not be authenticated." + ex);
        }
        return isConnected();
    }

    public boolean isConnected() {
        //return !StringUtils.isEmpty(accessToken) && userInfo != null;
        return sdk.isAuthenticated();
    }

    public boolean changeWorkingDirectory(String relative) throws IOException, RestException, OneDriveException {
        
        System.out.println ("*** changeWorkingDirectory: " + relative);

        boolean hasSuccess = true;
        try {
            if (!StringUtils.isEmpty(relative)) {
                workingFolder = retrieveFolderMetaData(relative);
            }
        } catch (IOException e) {
            hasSuccess = false;
        }
         //return workingFolder != null && hasSuccess && workingFolder.isDir();
        return hasSuccess && workingFolder != null;
    }

    public boolean disconnect() throws OneDriveException, IOException {
        try{
            sdk.disconnect();
            return true;
        } catch (IOException e){
            throw new IOException(e);
        }
    }
    
    /*
    private Folder retrieveFolderMetaData(String relative) throws RestException {
        String absolute = createAbsolutePath(relative);
        URL url;
        try {
            url = new URLBuilder(URL_FILE_METADATA)
                    .appendPath(absolute)
                    .build();
        } catch (URISyntaxException e) {
            throw new RestException(Messages.exception_onedrive_url(), e);
        } catch (MalformedURLException e) {
            throw new RestException(Messages.exception_onedrive_url(), e);
        }
        JsonObjectRequest<Folder> request = new JsonObjectRequest<Folder>(url, gson, Folder.class);
        request.setTimeout(timeout);
        request.sign(accessToken);
        final Folder folder;
        try {
            folder = request.execute();
        } catch (IOException e) {
            throw new RestException(Messages.exception_onedrive_folder(url), e);
        }
        return folder;
    }
    */
    
    private OneFolder retrieveFolderMetaData(String relative) throws RestException, IOException, OneDriveException{
        String absolute = createAbsolutePath(relative);
        OneFolder folder = sdk.getFolderByPath(absolute);
        return folder;
    }
    
    /*
    private File retrieveFileMetaData(String relative) throws IOException {
        String absolute = createAbsolutePath(relative);
        URL url;
        try {
            url = new URLBuilder(URL_FILE_METADATA)
                    .appendPath(absolute)
                    .build();
        } catch (URISyntaxException e) {
            throw new IOException(Messages.exception_onedrive_url(), e);
        }
        JsonObjectRequest<File> request = new JsonObjectRequest<File>(url, gson, File.class);
        request.setTimeout(timeout);
        request.sign(accessToken);

        return request.execute();
    }
    */
    
    private OneFile retrieveFileMetaData(String relative) throws IOException, OneDriveException {
        String absolute = createAbsolutePath(relative);
        OneFile file = sdk.getFileByPath(absolute);
        return file;    
    }
    
    /*
    public Folder makeDirectory(String relative) throws IOException {
        String absolute = createAbsolutePath(relative);
        URL url = getUrl(URL_OPS_CREATE_FOLDER);
        String body = new FormBuilder()
                .appendQueryParameter(PARAM_ROOT, workingFolder.getRoot())
                .appendQueryParameter(PARAM_PATH, absolute)
                .appendQueryParameter(PARAM_LOCALE, getOneDriveLocale())
                .build();
        String contentType = FormBuilder.CONTENT_TYPE;

        JsonObjectRequest<Folder> request = new JsonObjectRequest<Folder>(url, body, contentType, gson, Folder.class);
        request.setTimeout(timeout);
        request.sign(accessToken);

        return request.execute();
    }
    */
     public OneFolder makeDirectory(String dirName) throws IOException, OneDriveException {
         System.out.println ("*** makeDirectory: " + dirName);

         //String absolute = createAbsolutePath(relative);
          OneFolder folder = sdk.getRootFolder().createFolder(dirName);
          return folder;
     }
     
    /*
    public File storeFile(String name, InputStream content) throws IOException, OneDriveException {
        String absolute = createAbsolutePath(name);
        URL url;
        String parentRev = null;
        try {
            File file = (File) retrieveFileMetaData(name);
            parentRev = file.getRev();
        } catch (IOException e) {
            // Ignore, assume file doesn't exist
        }
        
        try {
            final URLBuilder builder = new URLBuilder(URL_FILE_UPLOAD);
            builder.appendPath(absolute)
                    .appendQueryParameter(PARAM_LOCALE, getOneDriveLocale())
                    .appendQueryParameter(PARAM_OVERWRITE, VALUE_TRUE)
                    .appendQueryParameter(PARAM_AUTORENAME, VALUE_FALSE);
            if (parentRev != null) {
                builder.appendQueryParameter(PARAM_PARENT_REV, parentRev);
            }
            url = builder.build();
        } catch (URISyntaxException e) {
            throw new IOException(Messages.exception_onedrive_url(), e);
        }
        
        JsonObjectRequest<File> request = new JsonObjectRequest<File>(url, content, null, gson, File.class);
        
        request.setTimeout(timeout);
        request.sign(accessToken);

        return request.execute();
        
        return null;
        
    }
    */
    public boolean storeFile(String path) throws IOException{
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
    
    /*
    public void cleanFolder() throws IOException {
        if (workingFolder.isDir()) {
            for (BaseFile file : workingFolder.getContents()) {
                DeletedFile deletedFile = deleteFile(file);
                if (!deletedFile.isDeleted()) {
                    throw new IOException(Messages.exception_onedrive_delete());
                }
            }
        } else {
            throw new IOException(Messages.exception_onedrive_deleteIsNotFolder());
        }
    }
    */
    public void cleanFolder() throws IOException, OneDriveException{
        // get contents of entire folder
        // delete all files in folder that are not directories
        String id = workingFolder.getId();
        List<OneFile> files = sdk.getFolderById(id).getChildFiles();
        for (OneFile file : files) {
           try{
               file.delete();
           }catch(OneDriveException e){
                throw new OneDriveException("Error deleting file from OneDrive. " + e);
           }
         } 
    }
    
    /*
    public void pruneFolder(String absoluteRemoteRoot, int pruneRootDays) throws IOException, RestException, OneDriveException {
        SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);
        Date cutoff = new Date(System.currentTimeMillis() - pruneRootDays * MILLISECONDS_PER_DAY);
        OneFolder root = retrieveFolderMetaData(absoluteRemoteRoot);
        if (root.isDir()) {
            for (BaseFile file : root.getContents()) {
                Date lastModified;
                try {
                    lastModified = df.parse(file.getModified());
                } catch (ParseException e) {
                    throw new IOException("Was unable to read OneDrive date format", e);
                }
                if (lastModified.before(cutoff))
                    deleteFile(file);
            }
        }
    }
    */
    public void pruneFolder(String absoluteRemoteRoot, int pruneRootDays) throws IOException, RestException, OneDriveException {
        SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US); 
         
    }
    
    /*
    private DeletedFile deleteFile(BaseFile file) throws RestException {
      //  URL url = getUrl(URL_OPS_DELETE_FILE);
        URL url = getURl("");
        String body;
        try {
            body = new FormBuilder()
                    .appendQueryParameter(PARAM_ROOT, file.getRoot())
                    .appendQueryParameter(PARAM_PATH, file.getPath())
                    .appendQueryParameter(PARAM_LOCALE, getOneDriveLocale())
                    .build();
        } catch (UnsupportedEncodingException e) {
            throw new RestException(Messages.exception_onedrive_body(), e);
        }
        JsonObjectRequest<DeletedFile> request = new JsonObjectRequest<DeletedFile>(url, body, FormBuilder.CONTENT_TYPE, gson, DeletedFile.class);
        request.setTimeout(timeout);
        request.sign(accessToken);

        return request.execute();
    }
    */
    private void deleteFile(OneFile file) throws RestException, IOException{
        try {
            file.delete();
        } catch (OneDriveException ex) {
            Logger.getLogger(OneDrive.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
    private AccountInfo retrieveAccountInfo(String accessToken) throws RestException {
        URL url = getUrl(URL_ACCOUNT_INFO);

        JsonObjectRequest<AccountInfo> request = new JsonObjectRequest<AccountInfo>(url, gson, AccountInfo.class);
        request.setTimeout(timeout);
        request.sign(accessToken);

        return request.execute();
    }
  
    public static String convertAuthorizationToAccessCode(String authorizationCode) throws IOException {
        if (StringUtils.isEmpty(authorizationCode)) {
            return "";
        }
        String accessToken = readAccessTokenFromProvider(authorizationCode);
        if (accessToken == null) {
            accessToken = readAccessTokenFromWeb(authorizationCode);
        }

        return accessToken;
    }

    private static String readAccessTokenFromWeb(String authorizationCode) throws RestException, UnsupportedEncodingException {
        String accessToken;
        URL url = getUrl(URL_TOKEN);
        FormBuilder builder = new FormBuilder()
                .appendQueryParameter("code", authorizationCode)
                .appendQueryParameter("grant_type", VALUE_AUTHORIZATION_CODE)
                .appendQueryParameter("client_id", Config.CLIENT_ID);
        try {
            // Apply production config not included in source distribution
            Class privateConfig = Class.forName("org.jenkinsci.plugins.publishoveronedrive.domain.ConfigPrivate");
            Class[] argClass = {builder.getClass()};
            Method method = privateConfig.getDeclaredMethod("append", argClass);
            method.invoke(null, builder);
        } catch (Exception e) {
            // Apply local development parameters
            builder.appendQueryParameter("client_secret", Config.CLIENT_SECRET);
        }
        String body = builder.build();

        String contentType = FormBuilder.CONTENT_TYPE;
        JsonObjectRequest<TokenResponse> request = new JsonObjectRequest<TokenResponse>(url, body, contentType, gson, TokenResponse.class);
        TokenResponse response = request.execute();
        accessToken = response.getAccessToken();
        return accessToken;
    }
    
    private static String readAccessTokenFromProvider(String authorizationCode) {
        String accessToken = null;
        List<OneDriveToken> tokens = CredentialsProvider.lookupCredentials(OneDriveToken.class, Jenkins.getInstance(), null, (DomainRequirement) null);
        for (OneDriveToken token : tokens) {
            if (token.getAuthorizationCode().equals(authorizationCode)) {
                accessToken = token.getAccessCode();
            }
        }
        return accessToken;
    }
    */
    
    public static String convertAuthorizationToAccessCode(String authorizationCode) throws IOException {
        if (StringUtils.isEmpty(authorizationCode)) {
            return "";
        }
        //String accessToken = readAccessTokenFromProvider(authorizationCode);
        String accessToken = null;
        if (accessToken == null) {
            accessToken = readAccessTokenFromWeb(authorizationCode);
        }

        return accessToken;
    }
    
    private static String readAccessTokenFromWeb(String authorizationCode) throws RestException, UnsupportedEncodingException {
        String accessToken;
        URL url = getUrl(URL_TOKEN);
        FormBuilder builder = new FormBuilder()
                .appendQueryParameter("client_id", Config.CLIENT_ID)
                .appendQueryParameter("client_secret", Config.CLIENT_SECRET)
                .appendQueryParameter("code", authorizationCode)
                .appendQueryParameter("grant_type", VALUE_AUTHORIZATION_CODE)
                .appendQueryParameter("redirect_uri", REDIRECT_URI);
                
        try {
            // Apply production config not included in source distribution
            //Class privateConfig = Class.forName("org.jenkinsci.plugins.publishoveronedrive.domain.ConfigPrivate");
            //Class[] argClass = {builder.getClass()};
            //Method method = privateConfig.getDeclaredMethod("append", argClass);
           // method.invoke(null, builder);
        } catch (Exception e) {
            // Apply local development parameters
           // builder.appendQueryParameter("client_secret", Config.CLIENT_SECRET);
        }
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

    private String getOneDriveLocale() {
        return Locale.getDefault().toLanguageTag();
    }

    private String createAbsolutePath(final String path) {
        StringBuilder sb = new StringBuilder();
        if (path.startsWith(PATH_SEPERATOR)) {
            // paths starting with / are already absolute
            sb.append(path);
        } else {
            // relative paths are prefix with the working folder
            if (workingFolder != null) {
               // sb.append(workingFolder.getPath());
            }

            // When working folder is the root the path could end with '/'
            if (sb.length() == 0 || !PATH_SEPERATOR.equals(sb.substring(sb.length() - 1))) {
                sb.append(PATH_SEPERATOR);
            }
            sb.append(path);
        }

        return sb.toString();
    }
    
    /*
    Method accepts a OneDrive folder as a parameter and returns 
    */
    /*
    private String getPath(OneFolder folder) throws IOException, OneDriveException{
        OneFolder path;
        try{
            path = folder.getParentFolder();
            
        }catch(OneDriveException){
            path = "";
        }
        
        return path;
    }
    */
}
