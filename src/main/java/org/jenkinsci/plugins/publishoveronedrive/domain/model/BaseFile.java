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

package org.jenkinsci.plugins.publishoveronedrive.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BaseFile {

    @Expose
    private String size;
    @Expose
    private String rev;
    @Expose
    private String modified;
    @Expose
    private String path;
    @SerializedName("is_dir")
    @Expose
    private boolean isDir;
    @Expose
    private String icon;
    @Expose
    private String root;
    @Expose
    private long bytes;
    @SerializedName("thumb_exists")
    @Expose
    private boolean thumbExists;
    @Expose
    private long revision;


    /**
     * @return The size
     */
    public String getSize() {
        return size;
    }

    /**
     * @param size The size
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * @return The rev
     */
    public String getRev() {
        return rev;
    }

    /**
     * @param rev The rev
     */
    public void setRev(String rev) {
        this.rev = rev;
    }

    /**
     * @return The modified
     */
    public String getModified() {
        return modified;
    }

    /**
     * @param modified The modified
     */
    public void setModified(String modified) {
        this.modified = modified;
    }

    /**
     * @return The path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path The path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return The isDir
     */
    public boolean isDir() {
        return isDir;
    }

    /**
     * @param isDir The is_dir
     */
    public void setIsDir(boolean isDir) {
        this.isDir = isDir;
    }

    /**
     * @return The icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon The icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return The root
     */
    public String getRoot() {
        return root;
    }

    /**
     * @param root The root
     */
    public void setRoot(String root) {
        this.root = root;
    }

    /**
     * @return The bytes
     */
    public long getBytes() {
        return bytes;
    }

    /**
     * @param bytes The bytes
     */
    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    /**
     * @return The thumbExists
     */
    public boolean isThumbExists() {
        return thumbExists;
    }

    /**
     * @param thumbExists The thumb_exists
     */
    public void setThumbExists(boolean thumbExists) {
        this.thumbExists = thumbExists;
    }

    /**
     * @return The revision
     */
    public long getRevision() {
        return revision;
    }

    /**
     * @param revision The revision
     */
    public void setRevision(long revision) {
        this.revision = revision;
    }

    public class PhotoInfo {

        @SerializedName("lat_long")
        @Expose
        private List<Float> latLong = new ArrayList<Float>();
        @SerializedName("time_taken")
        @Expose
        private String timeTaken;

        /**
         * @return The latLong
         */
        public List<Float> getLatLong() {
            return latLong;
        }

        /**
         * @param latLong The lat_long
         */
        public void setLatLong(List<Float> latLong) {
            this.latLong = latLong;
        }

        /**
         * @return The timeTaken
         */
        public String getTimeTaken() {
            return timeTaken;
        }

        /**
         * @param timeTaken The time_taken
         */
        public void setTimeTaken(String timeTaken) {
            this.timeTaken = timeTaken;
        }

    }
}
