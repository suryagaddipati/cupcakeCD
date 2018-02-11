package suryagaddipati.cupcakecd.docker.api.service;

import akka.http.javadsl.model.HttpMethods;
import suryagaddipati.cupcakecd.docker.api.request.ApiRequest;

public class ServiceLogRequest extends ApiRequest {

    public ServiceLogRequest(String dockerSwarmUrl, String id) {
        super(HttpMethods.GET, dockerSwarmUrl, "/services/" + id + "/logs?follow=true&stdout=true&stderr=true",null,null);
    }
}
