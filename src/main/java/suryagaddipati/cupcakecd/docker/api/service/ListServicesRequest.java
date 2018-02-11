package suryagaddipati.cupcakecd.docker.api.service;

import akka.http.javadsl.model.HttpMethods;
import suryagaddipati.cupcakecd.docker.api.request.ApiRequest;
import suryagaddipati.cupcakecd.docker.api.response.ApiException;
import suryagaddipati.cupcakecd.docker.api.response.SerializationException;
import suryagaddipati.cupcakecd.docker.marshalling.ResponseType;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ListServicesRequest extends ApiRequest {



    public ListServicesRequest(String dockerApiUrl, String url) {
        super(HttpMethods.GET, dockerApiUrl, url, ScheduledService.class, ResponseType.LIST);
    }

    public  ListServicesRequest(String dockerApiUrl, String filterKey, String filterValue){
       this(dockerApiUrl, "/services?filters="+encodeJsonFilter(filterKey,filterValue));
    }



}
