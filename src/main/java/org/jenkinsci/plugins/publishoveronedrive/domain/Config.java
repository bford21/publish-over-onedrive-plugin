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
    public static String CLIENT_ID = "";
    public static String CLIENT_SECRET = "";
    public static final String URL_TOKEN = "https://login.live.com/oauth20_token.srf?";
    public static final String REDIRECT_URI = "https://login.live.com/oauth20_desktop.srf";
    public static final String AUTHORIZE_URI = "https://login.live.com/oauth20_authorize.srf";
    public static final String PATH_SEPERATOR = "/";
    public static final String VALUE_AUTHORIZATION_CODE = "authorization_code";
    public static final String[] SCOPES = {"wl.signin", "wl.basic", "wl.offline_access", "wl.skydrive_update"};
}
