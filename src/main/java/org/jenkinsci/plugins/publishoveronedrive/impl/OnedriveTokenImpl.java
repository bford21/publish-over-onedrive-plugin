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
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import org.jenkinsci.plugins.publishoveronedrive.OneDriveToken;
import org.jenkinsci.plugins.publishoveronedrive.domain.OneDrive;
import org.kohsuke.stapler.DataBoundConstructor;
import javax.annotation.Nonnull;
import java.io.IOException;

public class OnedriveTokenImpl extends BaseStandardCredentials implements OneDriveToken {

    @Nonnull
    private final String authorizationCode;
    @Nonnull
    private final String accessCode;

    @DataBoundConstructor
    public OnedriveTokenImpl(CredentialsScope scope, String id, @Nonnull String authorizationCode, String description) throws IOException {
        super(scope, id, description);
        this.authorizationCode = authorizationCode;
        this.accessCode = OneDrive.convertAuthorizationToAccessCode(authorizationCode);
    }

    @NonNull
    @Override
    public String getAuthorizationCode() {
        return authorizationCode;
    }

    @NonNull
    @Override
    public String getAccessCode() {
        return accessCode;
    }

    @Extension
    public static class DescriptorImpl extends BaseStandardCredentialsDescriptor {

        @Override
        public String getDisplayName() {
            return Messages.OneDriveTokenImpl_api_token();
        }
    }
}
