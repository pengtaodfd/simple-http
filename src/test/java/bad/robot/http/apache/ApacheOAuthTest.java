/*
 * Copyright (c) 2011-2012, bad robot (london) ltd
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

package bad.robot.http.apache;

import bad.robot.http.HttpClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.UrlMatchingStrategy;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static bad.robot.http.HttpClients.anApacheClient;
import static bad.robot.http.configuration.AuthorisationToken.authorisationToken;
import static bad.robot.http.configuration.OAuthCredentials.oAuth;
import static bad.robot.http.wiremock.WireMockHeaderMatcher.header;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;

public class ApacheOAuthTest {

    private final static WireMockServer server = new WireMockServer();

    @BeforeClass
    public static void startHttpServer() {
        WireMock.configureFor("localhost", 8080);
        server.start();
    }

    @AfterClass
    public static void stopHttpServer() {
        server.stop();
    }

    @Before
    public void resetHttpExpectations() {
        WireMock.reset();
    }

    @Test
    public void authorisationHeaderIsSet() throws MalformedURLException {
        HttpClient http = anApacheClient().with(oAuth(authorisationToken("XystZ5ee"), new URL("http://localhost:8080")));
        http.get(new URL("http://localhost:8080/test"));
        verify(getRequestedFor(urlEqualTo("/test")).withHeader("Authorization", equalTo("Bearer XystZ5ee")));
    }

    @Test
    public void authorisationHeaderIsNotSetForDifferingUrl() throws MalformedURLException {
        HttpClient http = anApacheClient().with(oAuth(authorisationToken("XystZ5ee"), new URL("https://localhost:8080")));
        http.get(new URL("http://localhost:8080/test"));
        verifyNoHeadersFor(urlEqualTo("/test"));
    }

    @Test
    public void basicAuthorisationForMultipleCredentials() throws MalformedURLException {
        HttpClient http = anApacheClient()
            .with(oAuth(authorisationToken("XystZ5ee"), new URL("http://localhost:8080")))
            .with(oAuth(authorisationToken("ZytrE2xR"), new URL("https://localhost:8080")));
        http.get(new URL("http://localhost:8080/test"));
        verify(getRequestedFor(urlEqualTo("/test")).withHeader("Authorization", equalTo("Bearer ZytrE2xR")));
    }

    private void verifyNoHeadersFor(UrlMatchingStrategy url) {
        List<LoggedRequest> requests = WireMock.findAll(getRequestedFor(url));
        assertThat(requests, not(contains(header("Authorization"))));
    }

}