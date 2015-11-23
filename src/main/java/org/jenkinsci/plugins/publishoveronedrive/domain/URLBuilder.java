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

package org.jenkinsci.plugins.publishoveronedrive.domain;

import java.io.UnsupportedEncodingException;
import java.net.*;

class URLBuilder {
    String scheme = "";
    String host = "";
    String path = "";
    String query = "";


    public URLBuilder(String uri) throws URISyntaxException {
        URI parseUri = new URI(uri);
        scheme = parseUri.getScheme() == null ? "" : parseUri.getScheme();
        host = parseUri.getHost() == null ? "" : parseUri.getHost();
        path = parseUri.getPath() == null ? "" : parseUri.getPath();
        query = parseUri.getQuery() == null ? "" : parseUri.getQuery();
    }

    public URLBuilder appendQueryParameter(String key, String value) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder(query);
        if (sb.length() > 0) {
            sb.append("&");
        }
        sb.append(URLEncoder.encode(key, "UTF-8"));
        sb.append("=");
        sb.append(URLEncoder.encode(value, "UTF-8"));
        query = sb.toString();
        return this;
    }

    public URLBuilder appendPath(String str) {
        StringBuilder sb = new StringBuilder(path);
        if (sb.charAt(sb.length() - 1) != '/' && !str.startsWith("/")) {
            sb.append('/');
        }
        sb.append(str);
        path = sb.toString();
        return this;
    }

    public URL build() throws MalformedURLException {
        StringBuilder sb = new StringBuilder(path);
        if (query.length() > 0) {
            sb.append("?");
            sb.append(query);
        }
        return new URL(scheme, host, sb.toString());
    }
}
