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

import de.tuberlin.onedrivesdk.OneDriveException;
import de.tuberlin.onedrivesdk.folder.OneFolder;
import hudson.FilePath;
import jenkins.plugins.publish_over.BPBuildInfo;
import jenkins.plugins.publish_over.BPDefaultClient;
import jenkins.plugins.publish_over.BapPublisherException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jenkinsci.plugins.publishoveronedrive.domain.model.Folder;
import org.jenkinsci.plugins.publishoveronedrive.impl.OneDriveTransfer;
import org.jenkinsci.plugins.publishoveronedrive.impl.Messages;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jenkinsci.plugins.publishoveronedrive.domain.model.RestException;

public class OneDriveClient extends BPDefaultClient<OneDriveTransfer> {

    private static final Log LOG = LogFactory.getLog(OneDriveClient.class);
    private BPBuildInfo buildInfo;
    private final OneDrive onedrive;
    private String token;

    public OneDriveClient(final OneDrive client, final BPBuildInfo buildInfo) {
        this.onedrive = client;
        this.buildInfo = buildInfo;
    }

    public BPBuildInfo getBuildInfo() {
        return buildInfo;
    }

    public void setBuildInfo(final BPBuildInfo buildInfo) {
        this.buildInfo = buildInfo;
    }

    public boolean changeDirectory(final String directory) {
        try {
            return onedrive.changeWorkingDirectory(directory);
        } catch (IOException ioe) {
            throw new BapPublisherException(Messages.exception_cwdException(directory), ioe);
        } catch (OneDriveException ex) {
            Logger.getLogger(OneDriveClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean makeDirectory(final String directory) {
        try {
            OneFolder folder = onedrive.makeDirectory(directory);
            return folder != null;
        } catch (IOException ioe) {
            throw new BapPublisherException(Messages.exception_mkdirException(directory), ioe);
        } catch (OneDriveException ex) {
            Logger.getLogger(OneDriveClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void deleteTree() throws OneDriveException {
        try {
            onedrive.cleanFolder();
        } catch (IOException ioe) {
            throw new BapPublisherException(Messages.exception_failedToStoreFile("Cleaning failed"), ioe);
        }
    }

    public void beginTransfers(final OneDriveTransfer transfer) {
        if (!transfer.hasConfiguredSourceFiles()) {
            throw new BapPublisherException(Messages.exception_noSourceFiles());
        }
        if (transfer.isRemoteDirectorySDF() && transfer.isPruneRoot()) {
            try {
                onedrive.pruneFolder(getAbsoluteRemoteRoot(), transfer.getPruneRootDays());
            } catch (IOException ioe) {
                throw new BapPublisherException(Messages.exception_failedToStoreFile("Pruning failed"), ioe);
            } catch (OneDriveException ex) {
                Logger.getLogger(OneDriveClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    public void transferFile(final OneDriveTransfer transfer, final FilePath filePath, final InputStream content) throws OneDriveException {
        try {
            transfer.getPatternSeparator();
            onedrive.storeFile(filePath.getRemote(), content);
        } catch (IOException ioe) {
            throw new BapPublisherException(Messages.exception_failedToStoreFile("Storing failed"), ioe);
        }
    }
    
    
    /*
    public void transferFile(final OneDriveTransfer transfer, final FilePath filePath, final InputStream content) throws OneDriveException{
        try{
           
            //java.io.File file = new java.io.File("/Users/brianford/Downloads/debian-7.9.0-powerpc-netinst.iso");
            java.io.File file = new java.io.File(filePath.getName());
            onedrive.storeFile(file);
            
        }catch(IOException ioe){
            throw new BapPublisherException(Messages.exception_failedToStoreFile("Storing failed"), ioe);
        }
        
    }
    */
    public boolean connect() throws OneDriveException {
        try {
            return onedrive.isConnected() || onedrive.connect();
        } catch (IOException ioe) {
            throw new BapPublisherException(Messages.exception_exceptionOnDisconnect(ioe.getLocalizedMessage()), ioe);
        }
    }

    public void disconnect() throws OneDriveException {
        if ((onedrive != null) && onedrive.isConnected()) {
            try {
                onedrive.disconnect();
            } catch (IOException ioe) {
                throw new BapPublisherException(Messages.exception_exceptionOnDisconnect(ioe.getLocalizedMessage()), ioe);
            }
        }
    }

    public void disconnectQuietly() {
        try {
            onedrive.disconnect();
        } catch (OneDriveException e) {
            LOG.warn(Messages.log_disconnectQuietly(), e);
        } catch (IOException e) {
            LOG.warn(Messages.log_disconnectQuietly(), e);
        }
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setTimeout(int timeout) {
        onedrive.setTimeout(timeout);
    }

    public int getTimeout() {
        return onedrive.getTimeout();
    }

   

}
