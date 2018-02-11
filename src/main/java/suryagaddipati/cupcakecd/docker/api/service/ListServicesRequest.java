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

//    public static void main(String args[]) throws ExecutionException, InterruptedException {
//
//        final ActorSystem as = ActorSystem.create("swarm-plugin", ConfigFactory.load());;
//
//        final CompletionStage<Object> nodesStage = new suryagaddipati.jenkinsdockerslaves.docker.api.DockerApiRequest(as, new ListServicesRequest("http://localhost:2376","label","ROLE=jenkins-agent")).execute();
//        CompletionStage<Object> tasksStage = nodesStage.thenApplyAsync(services -> {
//            if(services instanceof ApiException){
//                return services;
//            }
//            ((List<ScheduledService>)services).stream().filter(service -> {
//
//                return false;
//            });
//            for(ScheduledService service : (List<ScheduledService>)services ){
//                CompletionStage<Object> tasksRequest = new suryagaddipati.jenkinsdockerslaves.docker.api.DockerApiRequest(as, new ListTasksRequest("http://localhost:2376", "service", service.Spec.Name)).execute();
//                List<Task> tasks = getFuture(tasksRequest, List.class);
//                if(tasks != null && tasks.size()==1) {
//                    Task task = tasks.get(0);
//                    System.out.print(task.isComplete());
//                }
//                System.out.println(service.Spec.Name);
//            }
//            return services;
//        });
//        Object o = tasksStage.toCompletableFuture().get();
//        if(o instanceof  ApiException){
//            ((ApiException)o).getCause().printStackTrace();
//        }
//
//        System.out.print(o);
//        as.terminate();
//    }

    private static <T> T  getFuture(final CompletionStage<Object> future,Class<T> clazz) {
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
