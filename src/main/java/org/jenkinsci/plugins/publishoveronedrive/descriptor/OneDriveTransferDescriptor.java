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
package org.jenkinsci.plugins.publishoveronedrive.descriptor;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.publishoveronedrive.impl.OneDrivePublisherPlugin;
import org.jenkinsci.plugins.publishoveronedrive.impl.OneDriveTransfer;
import org.jenkinsci.plugins.publishoveronedrive.impl.Messages;
import org.kohsuke.stapler.QueryParameter;

@Extension
public class OneDriveTransferDescriptor extends Descriptor<OneDriveTransfer> {

    public OneDriveTransferDescriptor() {
        super(OneDriveTransfer.class);
    }

    @Override
    public String getDisplayName() {
        return Messages.transfer_descriptor();
    }

    public OneDrivePublisherPlugin.Descriptor getPublisherPluginDescriptor() {
        return Jenkins.getInstance().getDescriptorByType(OneDrivePublisherPlugin.Descriptor.class);
    }

    public FormValidation doCheckSourceFiles(@QueryParameter final String value) {
        return FormValidation.validateRequired(value);
    }

    public boolean canUseExcludes() {
        return OneDriveTransfer.canUseExcludes();
    }

    public jenkins.plugins.publish_over.view_defaults.BPTransfer.Messages getCommonFieldNames() {
        return new jenkins.plugins.publish_over.view_defaults.BPTransfer.Messages();
    }

}
