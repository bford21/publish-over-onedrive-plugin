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
import java.net.URLEncoder;

public class FormBuilder {

    public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    public static final String AND = "&";
    public static final String UTF_8 = "UTF-8";
    public static final String EQUALS = "=";
    private final StringBuilder query = new StringBuilder();

    public FormBuilder appendQueryParameter(String key, String value) throws UnsupportedEncodingException {
        if (query.length() > 0) {
            query.append(AND);
        }
        query.append(URLEncoder.encode(key, UTF_8));
        query.append(EQUALS);
        query.append(URLEncoder.encode(value, UTF_8));
        return this;
    }

    public String build() {
        return query.toString();
    }
}