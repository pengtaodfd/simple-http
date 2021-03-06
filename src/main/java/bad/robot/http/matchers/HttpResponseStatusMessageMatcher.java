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

import bad.robot.http.HttpMessage;
import bad.robot.http.HttpResponse;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.Matchers.equalTo;

class HttpResponseStatusMessageMatcher extends TypeSafeMatcher<HttpResponse> {

    private final Matcher<String> delegate;

    @Factory
    public static Matcher<HttpResponse> statusMessage(Matcher<String> matcher) {
        return new HttpResponseStatusMessageMatcher(matcher);
    }

    @Factory
    public static Matcher<HttpResponse> statusMessage(String message) {
        return new HttpResponseStatusMessageMatcher(equalTo(message));
    }

    private HttpResponseStatusMessageMatcher(Matcher<String> matcher) {
        this.delegate = matcher;
    }

    @Override
    public boolean matchesSafely(HttpResponse actual) {
        return delegate.matches(actual.getStatusMessage());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a ").appendText(HttpMessage.class.getSimpleName()).appendText(" with status message ");
        delegate.describeTo(description);
    }
}
