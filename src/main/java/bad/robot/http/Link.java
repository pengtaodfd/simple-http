/*
 * Copyright (c) 2011-2013, bad robot (london) ltd
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package bad.robot.http;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import static bad.robot.http.Url.url;
import static java.lang.String.format;

/**
 * @see <a href="http://www.rfc-editor.org/rfc/rfc5988.txt">http://www.rfc-editor.org/rfc/rfc5988.txt</a>
 */
public class Link {

    private final Map<String, URL> links = new HashMap<>();

    public Link(Headers headers) {
        if (!headers.has("link"))
            return;
        StringTokenizer tokenizer = new StringTokenizer(headers.get("link").value(), ",");
        while (tokenizer.hasMoreElements()) {
            String relativeLink = (String) tokenizer.nextElement();
            String href = findHref(relativeLink);
            String relation = findRelation(relativeLink);
            links.put(relation, url(href));
        }
    }

    public URL next() {
        return links.get("next");
    }

    public URL previous() {
        if (!links.containsKey("prev"))
            return links.get("previous");
        return links.get("prev");
    }

    public URL first() {
        return links.get("first");
    }

    public URL last() {
        return links.get("last");
    }

    private String findRelation(String link) {
        try {
            return findRelationInLinkBetween(link, "rel=\"", "\"");
        } catch (Exception e) {
            return findRelationInLinkBetween(link, "rel='", "'");
        }
    }

    private String findRelationInLinkBetween(String link, String startDelimiter, String endDelimiter) {
        int start = link.indexOf(startDelimiter);
        int end = link.indexOf(endDelimiter, start + startDelimiter.length());
        if (start == -1 || end == -1)
            throw new LinkException(link);
        return link.substring(start + startDelimiter.length(), end).toLowerCase();
    }

    private String findHref(String link) {
        return link.substring(link.indexOf("<") + 1, link.indexOf(">;"));
    }

    private static class LinkException extends HttpException {
        public LinkException(String link) {
            super(format("failed to find a 'rel' within %s", link));
        }
    }
}
