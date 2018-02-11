package suryagaddipati.cupcakecd;

import akka.actor.ActorSystem;
import akka.http.javadsl.model.ResponseEntity;
import akka.stream.ActorMaterializer;
import com.typesafe.config.ConfigFactory;
import suryagaddipati.cupcakecd.docker.api.DockerApiRequest;
import suryagaddipati.cupcakecd.docker.api.containers.ContainerLogRequest;
import suryagaddipati.cupcakecd.docker.api.response.ApiException;
import suryagaddipati.cupcakecd.docker.api.response.ApiSuccess;
import suryagaddipati.cupcakecd.docker.api.response.SerializationException;

import java.nio.charset.Charset;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ContainerLogStreamer {
        public static void main(String args[]) throws ExecutionException, InterruptedException {

        final ActorSystem as = ActorSystem.create("cupcakeCD", ConfigFactory.load());;

        final CompletionStage<Object> nodesStage = new DockerApiRequest(as, new ContainerLogRequest("http://localhost:2376","a2139e41a7a7f6205ba4666234644177c0ed5525bcdf559538d14f00c037971e")).execute();

        Object o = nodesStage.toCompletableFuture().get();
        if(o instanceof  ApiException){
            ((ApiException)o).getCause().printStackTrace();
        }
            ResponseEntity responseEntity = ((ApiSuccess) o).getResponseEntity();
            ActorMaterializer materializer = ActorMaterializer.create(as);
            responseEntity.getDataBytes().runForeach(x -> {
                System.out.print(x.decodeString(Charset.defaultCharset()));
            } , materializer);
        System.out.print(o);
//        as.terminate();
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
