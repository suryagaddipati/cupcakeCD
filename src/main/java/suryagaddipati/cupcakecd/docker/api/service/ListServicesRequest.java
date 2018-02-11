package suryagaddipati.cupcakecd.docker.api.service;

import akka.http.javadsl.model.HttpMethods;
import suryagaddipati.cupcakecd.docker.api.request.ApiRequest;
import suryagaddipati.cupcakecd.docker.marshalling.ResponseType;

public class ListServicesRequest extends ApiRequest {



    public ListServicesRequest(String dockerApiUrl, String url) {
        super(HttpMethods.GET, dockerApiUrl, url, ScheduledService.class, ResponseType.LIST);
    }

    public  ListServicesRequest(String dockerApiUrl, String filterKey, String filterValue){
       this(dockerApiUrl, "/services?filters="+encodeJsonFilter(filterKey,filterValue));
    }



}
