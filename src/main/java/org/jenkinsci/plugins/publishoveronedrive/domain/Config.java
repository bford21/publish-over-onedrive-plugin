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
package org.jenkinsci.plugins.publishoveronedrive.domain;

public class Config {

    /**
     * @see Config#CLIENT_ID
     */
    /* TODO: Remove when finalized */
    static final String CLIENT_SECRET = "NNe3LAukrteNJZ7CEyitDJw2q2aHpBNl";
    static final String CLIENT_ID = "0000000048170EFD";
    private static String authorizeUrl = "https://login.live.com/oauth20_authorize.srf?client_id=" + CLIENT_ID + "&scope=wl.signin%20wl.basic%20wl.offline_access%20wl.skydrive_update&response_type=code&redirect_uri=https://login.live.com/oauth20_desktop.srf";

    public static String getAuthorizeUrl() {
        return authorizeUrl;
    }
}
