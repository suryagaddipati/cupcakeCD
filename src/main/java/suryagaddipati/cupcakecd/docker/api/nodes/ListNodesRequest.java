
package suryagaddipati.cupcakecd.docker.api.nodes;

import akka.http.javadsl.model.HttpMethods;
import suryagaddipati.cupcakecd.docker.api.request.ApiRequest;
import suryagaddipati.cupcakecd.docker.marshalling.ResponseType;

public class ListNodesRequest extends ApiRequest {

    public ListNodesRequest(String dockerSwarmApiUrl) {
        super(HttpMethods.GET, dockerSwarmApiUrl, "/nodes", Node.class, ResponseType.LIST);
    }
}
