package suryagaddipati.cupcakecd;

import akka.actor.ActorSystem;
import com.typesafe.config.ConfigFactory;
import suryagaddipati.cupcakecd.docker.api.DockerApiRequest;
import suryagaddipati.cupcakecd.docker.api.response.ApiException;
import suryagaddipati.cupcakecd.docker.api.response.SerializationException;
import suryagaddipati.cupcakecd.docker.api.service.ListServicesRequest;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ContainerLogStreamer {
        public static void main(String args[]) throws ExecutionException, InterruptedException {

        final ActorSystem as = ActorSystem.create("swarm-plugin", ConfigFactory.load());;

        final CompletionStage<Object> nodesStage = new DockerApiRequest(as, new ListServicesRequest("http://localhost:2376","label","ROLE=jenkins-agent")).execute();

        Object o = nodesStage.toCompletableFuture().get();
        if(o instanceof  ApiException){
            ((ApiException)o).getCause().printStackTrace();
        }

        System.out.print(o);
        as.terminate();
    }

    private static <T> T  getFuture(final CompletionStage<Object> future, Class<T> clazz) {
        try {
            final Object result = future.toCompletableFuture().get(5, TimeUnit.SECONDS);
            return getResult(result,clazz);
        } catch (InterruptedException|ExecutionException |TimeoutException e) {
            throw  new RuntimeException(e);
        }
    }
    private static <T> T  getResult(Object result, Class<T> clazz){
        if(result instanceof SerializationException){
            throw new RuntimeException (((SerializationException)result).getCause());
        }
        if(result instanceof ApiException){
            throw new RuntimeException (((ApiException)result).getCause());
        }
        return clazz.cast(result);
    }
}
