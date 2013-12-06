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

package bad.robot.http.apache;

import bad.robot.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

import java.util.concurrent.Callable;

class ApacheHttpRequestExecutor implements Callable<HttpResponse> {

    private final org.apache.http.client.HttpClient client;
    private final HttpContext localContext;
    private final HttpUriRequest request;

    public ApacheHttpRequestExecutor(org.apache.http.client.HttpClient client, HttpContext localContext, HttpUriRequest request) {
        this.request = request;
        this.client = client;
        this.localContext = localContext;
    }

    @Override
    public HttpResponse call() throws Exception {
        return client.execute(request, new HttpResponseHandler(request, new ToStringConsumer()), localContext);
    }
}
