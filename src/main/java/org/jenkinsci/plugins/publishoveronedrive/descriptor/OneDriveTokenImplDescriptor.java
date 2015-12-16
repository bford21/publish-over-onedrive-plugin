/*
 * The MIT License
 *
 * Copyright 2015 by Brian Ford, Xamarin Inc.
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

import de.tuberlin.onedrivesdk.OneDriveException;
import hudson.Extension;
import hudson.util.FormValidation;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletException;
import org.jenkinsci.plugins.publishoveronedrive.domain.Config;
import org.jenkinsci.plugins.publishoveronedrive.domain.FormBuilder;
import org.jenkinsci.plugins.publishoveronedrive.impl.Messages;
import org.kohsuke.stapler.QueryParameter;

@Extension
public class OneDriveTokenImplDescriptor extends Descriptor<OneDriveTokenImpl> {

    /**
     * @see Config#CLIENT_ID
     */
    static String CLIENT_ID = "";
    static String CLIENT_SECRET = "";

    private static final String AUTHORIZE_URI = "https://login.live.com/oauth20_authorize.srf";
    private static final String REDIRECT_URI = "https://login.live.com/oauth20_desktop.srf";

    final String[] scopes = {"wl.signin", "wl.basic", "wl.offline_access", "wl.skydrive_update"};

    public FormValidation doTestConnection(@QueryParameter("clientId") final String clientID, @QueryParameter("clientSecret") final String clientSecret) throws IOException, ServletException, OneDriveException {
        try {
            CLIENT_SECRET = clientSecret;

            String queryString = new FormBuilder()
                    .appendQueryParameter("client_id", clientID)
                    .appendQueryParameter("scope", String.join(" ", scopes))
                    .appendQueryParameter("response_type", "code")
                    .appendQueryParameter("redirect_uri", REDIRECT_URI).toString();

           
            return FormValidation.ok(AUTHORIZE_URI + '?' + queryString);

        } catch (UnsupportedEncodingException e) {
            return FormValidation.error("Client error : "+e.getMessage());
        }

    }

  
    public String getDisplayName() {
        return Messages.hostconfig_descriptor();
    }
}
