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
package org.jenkinsci.plugins.publishoveronedrive.impl;

import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import de.tuberlin.onedrivesdk.OneDriveException;
import hudson.model.Describable;
import jenkins.model.Jenkins;
import jenkins.plugins.publish_over.BPBuildInfo;
import jenkins.plugins.publish_over.BPHostConfiguration;
import jenkins.plugins.publish_over.BapPublisherException;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jenkinsci.plugins.publishoveronedrive.OneDriveToken;
import org.jenkinsci.plugins.publishoveronedrive.descriptor.OneDriveHostConfigurationDescriptor;
import org.jenkinsci.plugins.publishoveronedrive.domain.OneDrive;
import org.jenkinsci.plugins.publishoveronedrive.domain.OneDriveClient;
import org.kohsuke.stapler.DataBoundConstructor;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OneDriveHostConfiguration extends BPHostConfiguration<OneDriveClient, Object> implements Describable<OneDriveHostConfiguration> {

    public static final int DEFAULT_TIMEOUT = 300000;
    private OneDriveToken token;
    private final int timeout;

    @DataBoundConstructor
    public OneDriveHostConfiguration(final String name, final String token, final String remoteRootDir, final int timeout) {
        super(name, null, null, null, remoteRootDir, 0);
        this.timeout = timeout;
        this.token = token == null ? null : lookupTokenId(token);
    }

    private OneDriveToken lookupTokenId(String tokenId) {
        List<OneDriveToken> credentials = CredentialsProvider.lookupCredentials(OneDriveToken.class, Jenkins.getInstance(), null, (DomainRequirement) null);
        for (OneDriveToken token : credentials) {
            if (tokenId.equals(token.getId())) {
                return token;
            }
        }
        return null;
    }

    public OneDriveToken getToken() {
        return token;
    }

    public void setToken(final OneDriveToken token) {
        this.token = token;
    }

    @Override
    public OneDriveClient createClient(final BPBuildInfo buildInfo) {
        final OneDriveClient client = new OneDriveClient(createOneDrive(), buildInfo);
        try {
            init(client);
        } catch (IOException ioe) {
            throw new BapPublisherException(Messages.exception_bap_createclient(ioe.getLocalizedMessage()), ioe);
        } catch (OneDriveException ex) {
            Logger.getLogger(OneDriveHostConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
        return client;
    }

    private OneDrive createOneDrive() {
        return new OneDrive(token.getAccessCode());
    }

    private void init(final OneDriveClient client) throws IOException, OneDriveException {
        configureOneDriveClient(client);
        connect(client);
        changeToRootDirectory(client);
        setRootDirectoryInClient(client);
    }

    private void configureOneDriveClient(final OneDriveClient client) {
        client.setTimeout(timeout);
        client.setToken(token.getAuthorizationCode());
    }

    private void connect(final OneDriveClient client) throws IOException, OneDriveException {
        if (!client.connect()) {
            exception(client, Messages.exception_bap_logInFailed(getToken()));
        }
    }

    private void setRootDirectoryInClient(final OneDriveClient client) throws IOException {
        if (isDirectoryAbsolute(getRemoteRootDir())) {
            client.setAbsoluteRemoteRoot(getRemoteRootDir());
        }
    }

    @Override
    public OneDriveHostConfigurationDescriptor getDescriptor() {
        return Jenkins.getInstance().getDescriptorByType(OneDriveHostConfigurationDescriptor.class);
    }

    @Override
    protected HashCodeBuilder addToHashCode(final HashCodeBuilder builder) {
        return super.addToHashCode(builder)
                .append(token)
                .append(timeout);
    }

    protected EqualsBuilder addToEquals(final EqualsBuilder builder, final OneDriveHostConfiguration that) {
        return super.addToEquals(builder, that)
                .append(token, that.token)
                .append(timeout, that.timeout);
    }

    @Override
    protected ToStringBuilder addToToString(final ToStringBuilder builder) {
        return super.addToToString(builder)
                .append("token", token)
                .append("timeout", timeout);
    }

    @Override
    public boolean equals(final Object that) {
        if (this == that) {
            return true;
        }
        if (that == null || getClass() != that.getClass()) {
            return false;
        }

        return addToEquals(new EqualsBuilder(), (OneDriveHostConfiguration) that).isEquals();
    }

    @Override
    public int hashCode() {
        return addToHashCode(new HashCodeBuilder()).toHashCode();
    }

    @Override
    public String toString() {
        return addToToString(new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)).toString();
    }

}
