package suryagaddipati.cupcakecd;

import akka.actor.ActorSystem;
import akka.http.javadsl.model.ResponseEntity;
import akka.stream.ActorMaterializer;
import com.typesafe.config.ConfigFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import suryagaddipati.cupcakecd.docker.api.DockerApiRequest;
import suryagaddipati.cupcakecd.docker.api.containers.ContainerLogRequest;
import suryagaddipati.cupcakecd.docker.api.response.ApiException;
import suryagaddipati.cupcakecd.docker.api.response.ApiSuccess;
import suryagaddipati.cupcakecd.docker.api.response.SerializationException;

import java.nio.charset.Charset;
import java.util.Properties;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ContainerLogStreamer {
    public static void main(String args[]) throws ExecutionException, InterruptedException {

        final ActorSystem as = ActorSystem.create("cupcakeCD", ConfigFactory.load());;

        final CompletionStage<Object> nodesStage = new DockerApiRequest(as, new ContainerLogRequest("http://localhost:2376","f6d56c491e85e7c319dca41ada3ba23351d5ebe1c555e501c356c21277b618b1")).execute();

        Object o = nodesStage.toCompletableFuture().get();
        if(o instanceof  ApiException){
            ((ApiException)o).getCause().printStackTrace();
            as.terminate();
        }

        //Kafka

        final String bootstrapServers = args.length > 0 ? args[0] : "localhost:9092";
        final Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        final KafkaProducer<String, String> logEventProducer = new KafkaProducer<>(props,
                Serdes.String().serializer(),Serdes.String().serializer());


        ResponseEntity responseEntity = ((ApiSuccess) o).getResponseEntity();
        ActorMaterializer materializer = ActorMaterializer.create(as);
        responseEntity.getDataBytes().runForeach(x -> {
            logEventProducer.send(
                    new ProducerRecord<>("build-log-events", x.decodeString(Charset.defaultCharset()))
            );

            System.out.print(x.decodeString(Charset.defaultCharset()));
        } , materializer);

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
