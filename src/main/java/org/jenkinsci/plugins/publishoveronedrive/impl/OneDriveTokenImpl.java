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

import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.impl.BaseStandardCredentials;
import de.tuberlin.onedrivesdk.OneDriveException;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.util.FormValidation;
import org.jenkinsci.plugins.publishoveronedrive.OneDriveToken;
import org.jenkinsci.plugins.publishoveronedrive.domain.OneDrive;
import org.kohsuke.stapler.DataBoundConstructor;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletException;
import org.jenkinsci.plugins.publishoveronedrive.domain.Config;
import org.jenkinsci.plugins.publishoveronedrive.domain.FormBuilder;
import org.kohsuke.stapler.QueryParameter;

public class OneDriveTokenImpl extends BaseStandardCredentials implements OneDriveToken {

    @Nonnull
    private final String authorizationCode;
    @Nonnull
    private final String accessCode;
    @Nonnull
    private final String clientId;
    @Nonnull
    private final String clientSecret;


    @DataBoundConstructor
    public OneDriveTokenImpl(CredentialsScope scope, String id, @Nonnull String authorizationCode, String description, @Nonnull String clientId, @Nonnull String clientSecret) throws IOException {
        super(scope, id, description);
        this.authorizationCode = authorizationCode;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.accessCode = OneDrive.convertAuthorizationToAccessCode(authorizationCode);
    }

    @NonNull
    @Override
    public String getAuthorizationCode() {
        return authorizationCode;
    }

    @NonNull
    @Override
    public String getClientId() {
        return clientId;
    }

    @NonNull
    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    @NonNull
    @Override
    public String getAccessCode() {
        return accessCode;
    }

    @Extension
    public static class DescriptorImpl extends BaseStandardCredentialsDescriptor {

        public FormValidation doGetURL(@QueryParameter("clientId") final String clientId, @QueryParameter("clientSecret") final String clientSecret) throws IOException, ServletException, OneDriveException {
            try {
                Config.CLIENT_SECRET = clientSecret;
                Config.CLIENT_ID = clientId;

                String queryString = new FormBuilder()
                        .appendQueryParameter("client_id", clientId)
                        .appendQueryParameter("scope", String.join(" ", Config.SCOPES))
                        .appendQueryParameter("response_type", "code")
                        .appendQueryParameter("redirect_uri", Config.REDIRECT_URI)
                        .build();

                final String url = Config.AUTHORIZE_URI + "?" + queryString;
                final String message = String.format("The API token can be <a href=\"%s\" target=\"_blank\">generated</a> on the OneDrive website", url);

                return FormValidation.okWithMarkup(message);

            } catch (UnsupportedEncodingException e) {
                return FormValidation.error("Client error : " + e.getMessage());
            }
        }
        @Override
        public String getDisplayName() {
            return Messages.OneDriveTokenImpl_api_token();
        }
    }
}
