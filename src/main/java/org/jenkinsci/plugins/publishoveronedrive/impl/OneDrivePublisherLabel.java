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

import hudson.Extension;
import hudson.model.Describable;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;
import jenkins.plugins.publish_over.PublisherLabel;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.kohsuke.stapler.DataBoundConstructor;

public class OneDrivePublisherLabel extends PublisherLabel implements Describable<OneDrivePublisherLabel> {

    @DataBoundConstructor
    public OneDrivePublisherLabel(final String label) {
        super(label);
    }

    @Override
    public OneDrivePublisherLabelDescriptor getDescriptor() {
        return Jenkins.getInstance().getDescriptorByType(OneDrivePublisherLabelDescriptor.class);
    }

    @Override
    public boolean equals(final Object that) {
        if (this == that) {
            return true;
        }
        if (that == null || getClass() != that.getClass()) {
            return false;
        }

        return addToEquals(new EqualsBuilder(), (OneDrivePublisherLabel) that).isEquals();
    }

    @Override
    public int hashCode() {
        return addToHashCode(new HashCodeBuilder()).toHashCode();
    }

    @Override
    public String toString() {
        return addToToString(new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)).toString();
    }

    @Extension
    public static class OneDrivePublisherLabelDescriptor extends Descriptor<OneDrivePublisherLabel> {

        @Override
        public String getDisplayName() {
            return Messages.publisherLabel_descriptor();
        }

        public jenkins.plugins.publish_over.view_defaults.PublisherLabel.Messages getCommonFieldNames() {
            return new jenkins.plugins.publish_over.view_defaults.PublisherLabel.Messages();
        }
    }
}
