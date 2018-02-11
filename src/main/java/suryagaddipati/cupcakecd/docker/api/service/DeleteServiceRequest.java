package suryagaddipati.cupcakecd.docker.api.service;

import akka.http.javadsl.model.HttpMethods;
import suryagaddipati.cupcakecd.docker.api.request.ApiRequest;

public class DeleteServiceRequest extends ApiRequest {
    public DeleteServiceRequest(String dockerApiUrl,  String serviceName) {
        super(HttpMethods.DELETE,dockerApiUrl, "/services/"+serviceName,null,null);
    }

}
