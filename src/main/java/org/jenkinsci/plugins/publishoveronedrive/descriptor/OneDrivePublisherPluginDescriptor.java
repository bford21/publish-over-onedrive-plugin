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

import hudson.Util;
import hudson.model.AbstractProject;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import hudson.util.CopyOnWriteList;
import hudson.util.FormValidation;
import jenkins.model.Jenkins;
import jenkins.plugins.publish_over.*;
import net.sf.json.JSONObject;
import org.jenkinsci.plugins.publishoveronedrive.impl.OneDriveHostConfiguration;
import org.jenkinsci.plugins.publishoveronedrive.impl.OneDrivePublisherPlugin;
import org.jenkinsci.plugins.publishoveronedrive.impl.Messages;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import java.util.List;

public class OneDrivePublisherPluginDescriptor extends BuildStepDescriptor<Publisher> {

    private final CopyOnWriteList<OneDriveHostConfiguration> hostConfigurations = new CopyOnWriteList<OneDriveHostConfiguration>();
    private BPPluginDescriptor.BPDescriptorMessages msg;

    public OneDrivePublisherPluginDescriptor() {
        super(OneDrivePublisherPlugin.class);
        load();
    }

    @Override
    public String getDisplayName() {
        return Messages.descriptor_displayName();
    }

    @Override
    public boolean isApplicable(final Class<? extends AbstractProject> aClass) {
        return !BPPlugin.PROMOTION_JOB_TYPE.equals(aClass.getCanonicalName());
    }

    public List<OneDriveHostConfiguration> getHostConfigurations() {
        return hostConfigurations.getView();
    }

    public OneDriveHostConfiguration getConfiguration(final String name) {
        for (OneDriveHostConfiguration configuration : hostConfigurations) {
            if (configuration.getName().equals(name)) {
                return configuration;
            }
        }
        return null;
    }

    @Override
    public boolean configure(final StaplerRequest request, final JSONObject formData) {
        hostConfigurations.replaceBy(request.bindJSONToList(OneDriveHostConfiguration.class, formData.get("instance")));
        save();
        return true;
    }

    public boolean canSetMasterNodeName() {
        return JenkinsCapabilities.missing(JenkinsCapabilities.MASTER_HAS_NODE_NAME);
    }

    public String getDefaultMasterNodeName() {
        return BPInstanceConfig.DEFAULT_MASTER_NODE_NAME;
    }

    public boolean isEnableOverrideDefaults() {
        return JenkinsCapabilities.available(JenkinsCapabilities.SIMPLE_DESCRIPTOR_SELECTOR);
    }

    public OneDrivePublisherDescriptor getPublisherDescriptor() {
        return Jenkins.getInstance().getDescriptorByType(OneDrivePublisherDescriptor.class);
    }

    public OneDriveHostConfigurationDescriptor getHostConfigurationDescriptor() {
        return Jenkins.getInstance().getDescriptorByType(OneDriveHostConfigurationDescriptor.class);
    }

    public jenkins.plugins.publish_over.view_defaults.BPInstanceConfig.Messages getCommonFieldNames() {
        return new jenkins.plugins.publish_over.view_defaults.BPInstanceConfig.Messages();
    }

    public jenkins.plugins.publish_over.view_defaults.manage_jenkins.Messages getCommonManageMessages() {
        return new jenkins.plugins.publish_over.view_defaults.manage_jenkins.Messages();
    }

    public FormValidation doTestConnection(final StaplerRequest request, final StaplerResponse response) {
        final OneDriveHostConfiguration hostConfig = request.bindParameters(OneDriveHostConfiguration.class, "");
        final BPBuildInfo buildInfo = createDummyBuildInfo();
        try {
            hostConfig.createClient(buildInfo).disconnect();
            return FormValidation.ok(Messages.form_testConnection_ok());
        } catch (Exception e) {
            return FormValidation.errorWithMarkup("<p>"
                    + Messages.form_testConnection_error() + "</p><p><pre>"
                    + Util.escape(e.getClass().getCanonicalName() + ": " + e.getLocalizedMessage())
                    + "</pre></p>");
        }
    }

    private BPBuildInfo createDummyBuildInfo() {
        return new BPBuildInfo(
                TaskListener.NULL,
                "",
                Jenkins.getInstance().getRootPath(),
                null,
                null
        );
    }
}
