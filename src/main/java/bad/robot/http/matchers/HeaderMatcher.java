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
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

class HeaderMatcher extends TypeSafeMatcher<Header> {

    private final String name;
    private final Matcher<String> value;

    @Factory
    public static Matcher<Header> header(String name, Matcher<String> matcher) {
        return new HeaderMatcher(name, matcher);
    }

    private HeaderMatcher(String name, Matcher<String> value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean matchesSafely(Header actual) {
        if (actual.name().equals(name) && value.matches(actual.value()))
            return true;
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("header ").appendValue(name).appendText(" with value of ");
        value.describeTo(description);
    }
}
