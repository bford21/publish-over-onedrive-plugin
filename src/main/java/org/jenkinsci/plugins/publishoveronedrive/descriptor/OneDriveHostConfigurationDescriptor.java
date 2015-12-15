/*
 * The MIT License
 *
 * Copyright (C) 2015 by Brian Ford, Xamarin inc.
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
package org.jenkinsci.plugins.publishoveronedrive.descriptor;

import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import de.tuberlin.onedrivesdk.OneDriveException;
import de.tuberlin.onedrivesdk.OneDriveFactory;
import de.tuberlin.onedrivesdk.OneDriveSDK;
import de.tuberlin.onedrivesdk.common.OneDriveScope;
import de.tuberlin.onedrivesdk.folder.OneFolder;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import java.io.IOException;
import jenkins.model.Jenkins;
import jenkins.plugins.publish_over.BPValidators;
import org.jenkinsci.plugins.publishoveronedrive.OneDriveToken;
import org.jenkinsci.plugins.publishoveronedrive.impl.OneDriveHostConfiguration;
import org.jenkinsci.plugins.publishoveronedrive.impl.Messages;
import org.kohsuke.stapler.QueryParameter;
import java.util.List;
import javax.servlet.ServletException;

@Extension
public class OneDriveHostConfigurationDescriptor extends Descriptor<OneDriveHostConfiguration> {

    public OneDriveHostConfigurationDescriptor() {
        super(OneDriveHostConfiguration.class);
    }

    @Override
    public String getDisplayName() {
        return Messages.hostconfig_descriptor();
    }

    public int getDefaultTimeout() {
        return OneDriveHostConfiguration.DEFAULT_TIMEOUT;
    }

    public FormValidation doCheckName(@QueryParameter final String value) {
        return BPValidators.validateName(value);
    }

    public FormValidation doCheckTimeout(@QueryParameter final String value) {
        return FormValidation.validateNonNegativeInteger(value);
    }

    public FormValidation doCheckRemoteRootDir(@QueryParameter String remoteRootDir) {
        if (remoteRootDir.matches("/.*")) {
            return FormValidation.ok();
        } else {
            return FormValidation.error(Messages.hostconfig_formvalidation_root());
        }
    }

    public ListBoxModel doFillTokenItems() {
        ListBoxModel items = new ListBoxModel();
        for (OneDriveToken token : getOneDriveTokens()) {
            items.add(token.getDescription(), token.getId());
        }
        if (items.size() > 0) {
            items.get(0).selected = true;
        }
        return items;
    }

    private List<OneDriveToken> getOneDriveTokens() {
        return CredentialsProvider.lookupCredentials(OneDriveToken.class, Jenkins.getInstance(), null, (DomainRequirement) null);
    }

    public FormValidation doTestConnection(@QueryParameter("name") final String name, @QueryParameter("token") final String token, @QueryParameter("remoteRootDir") final String dir) throws IOException, ServletException, OneDriveException {
        String refreshToken = null;
        List<OneDriveToken> tokens = CredentialsProvider.lookupCredentials(OneDriveToken.class, Jenkins.getInstance(), null, (DomainRequirement) null);
        for (OneDriveToken token2 : tokens) {
            refreshToken = token2.getAccessCode();
        }

        OneDriveSDK sdk = OneDriveFactory.createOneDriveSDK("https://login.live.com/oauth20_desktop.srf", OneDriveScope.READWRITE);

        try {
            sdk.authenticateWithRefreshToken(refreshToken);
            OneFolder rootFolder = sdk.getRootFolder();
            System.out.println(rootFolder);
            return FormValidation.ok("Succesfully authenticated using token!");
        } catch (OneDriveException ex) {
            return FormValidation.error("Error authenticating using token: " + refreshToken);
        }
    }

    public jenkins.plugins.publish_over.view_defaults.HostConfiguration.Messages getCommonFieldNames() {
        return new jenkins.plugins.publish_over.view_defaults.HostConfiguration.Messages();
    }
}
