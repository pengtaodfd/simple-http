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

package bad.robot.http.matchers;

import bad.robot.http.Header;
import bad.robot.http.HttpPost;
import bad.robot.http.HttpResponse;
import bad.robot.http.UnencodedStringMessage;
import org.hamcrest.StringDescription;
import org.junit.Ignore;
import org.junit.Test;

import static bad.robot.http.HeaderList.headers;
import static bad.robot.http.HeaderPair.header;
import static bad.robot.http.HttpClients.anApacheClient;
import static bad.robot.http.Url.url;
import static bad.robot.http.matchers.Matchers.has;
import static bad.robot.http.matchers.Matchers.headerWithValue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class HttpMessageHeaderValueMatcherTest {

    private final Header header = header("Accept", "text/html");
    private final Header anotherHeader = header("Accept-Language", "en-US");
    private final HttpPost request = new UnencodedStringMessage("body", headers(anotherHeader, header));

    @Test
    @Ignore("an example only")
    public void exampleUsage() {
        HttpPost request = new UnencodedStringMessage("body", headers(header));
        HttpResponse response = anApacheClient().post(url("http://www.google.com"), request);

        assertThat(request, has(headerWithValue("Accept", containsString("html"))));
        assertThat(response, has(headerWithValue("Content-Type", not(containsString("xml")))));
    }

    @Test
    public void matches() {
        assertThat(headerWithValue("Accept", equalTo("text/html")).matches(request), is(true));
    }

    @Test
    public void doesNotMatch() {
        assertThat(headerWithValue("Accept", equalTo("application/json")).matches(request), is(false));
        assertThat(headerWithValue("accept", equalTo("text/html")).matches(header), is(false));
        assertThat(headerWithValue("Accept-Language", equalTo("text/html")).matches(header), is(false));
    }

    @Test
    public void description() {
        StringDescription description = new StringDescription();
        headerWithValue("Accept", equalTo("application/json")).describeTo(description);
        assertThat(description.toString(), allOf(
                containsString("a HttpMessage with"),
                containsString("Accept"),
                containsString("application/json"))
        );
    }
}
