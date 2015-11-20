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
package org.jenkinsci.plugins.publishoverdropbox.domain;

public class Config {
    /**
     * Generate these values by creating your own Dropbox app registration on https://www.dropbox.com/developers/apps
     * <br/>
     * Replace the strings values in this file to run your own compiled version of the plugin
     */
    static final String CLIENT_ID = "0000000048170EFD";
    /**
     * @see Config#CLIENT_ID
     */
    static final String CLIENT_SECRET = "NNe3LAukrteNJZ7CEyitDJw2q2aHpBNl";
    private static String authorizeUrl = "https://login.live.com/oauth20_authorize.srf?client_id="+CLIENT_ID+"&scope=wl.signin%20wl.basic%20wl.offline_access%20wl.skydrive_update&response_type=code&redirect_uri=https://login.live.com/oauth20_desktop.srf";
    //private static final String authorizeUrl = "https://www.dropbox.com/1/oauth2/authorize?response_type=code&client_id=" + CLIENT_ID;

    public static String getAuthorizeUrl() {
        return authorizeUrl;
    }
}
