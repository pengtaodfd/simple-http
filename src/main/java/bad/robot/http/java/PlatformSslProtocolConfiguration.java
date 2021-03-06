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

package bad.robot.http.java;

public class PlatformSslProtocolConfiguration implements ConfigurePlatformDefaultSslSocketFactory, ConfigurePlatformDefaultHostnameVerifier {

    @Override
    public void configureDefaultSslSocketFactory() {
        if (platformIsUsingDeprecatedSslProtocol())
            new AlwaysTrustingDeprecatedDefaultSslSocketFactory().configureDefaultSslSocketFactory();
        else
            new AlwaysTrustingDefaultSslSocketFactory().configureDefaultSslSocketFactory();
    }

    @Override
    public void configureDefaultHostnameVerifier() {
        if (platformIsUsingDeprecatedSslProtocol())
            new AlwaysTrustingDeprecatedDefaultHostnameVerifier().configureDefaultHostnameVerifier();
        else
            new AlwaysTrustingDefaultHostnameVerifier().configureDefaultHostnameVerifier();
    }

    private static boolean platformIsUsingDeprecatedSslProtocol() {
        return ("com.sun.net.ssl.internal.www.protocol".equals(System.getProperty("java.protocol.handler.pkgs")));
    }
}
