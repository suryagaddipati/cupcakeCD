package suryagaddipati.cupcakecd.docker.api.containers;

import akka.http.javadsl.model.HttpMethods;
import suryagaddipati.cupcakecd.docker.api.request.ApiRequest;

public class ContainerLogRequest extends ApiRequest {
    public ContainerLogRequest (String dockerSwarmUrl, String id) {
        super(HttpMethods.GET, dockerSwarmUrl, "/containers/" + id + "/logs?follow=true&stdout=true&stderr=true",null,null);
    }
}
