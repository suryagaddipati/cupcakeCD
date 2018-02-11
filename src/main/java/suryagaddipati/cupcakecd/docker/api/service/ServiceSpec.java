package suryagaddipati.cupcakecd.docker.api.service;

import akka.http.javadsl.model.HttpMethods;
import suryagaddipati.cupcakecd.docker.api.containers.ContainerSpec;
import suryagaddipati.cupcakecd.docker.api.network.Network;
import suryagaddipati.cupcakecd.docker.api.request.ApiRequest;
import suryagaddipati.cupcakecd.docker.api.task.TaskTemplate;
import suryagaddipati.cupcakecd.docker.marshalling.ResponseType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceSpec extends ApiRequest {
    public suryagaddipati.cupcakecd.docker.api.task.TaskTemplate TaskTemplate ;
    public String Name;
    public Map<String,String> Labels = new HashMap<>();

    public List<Network> Networks = new ArrayList<>();
    public ServiceSpec(String dockerApiUrl, String name, String Image, String[] Cmd, String[] Env) {
        super(HttpMethods.POST, dockerApiUrl, "/services/create",CreateServiceResponse.class, ResponseType.CLASS);
        this.Name = name;
        this.TaskTemplate = new TaskTemplate(Image,Cmd,Env);
    }

    public ServiceSpec(){
        super(HttpMethods.POST, "", "/services/create",CreateServiceResponse.class, ResponseType.CLASS);
    }

    public void  addBindVolume(String source,String target){
        ContainerSpec.Mount mount = ContainerSpec.Mount.bindMount(source, target);
        this.TaskTemplate.ContainerSpec.Mounts.add(mount);
    }
    public void addCacheVolume(String cacheVolumeName, String target, String cacheDriverName) {
        ContainerSpec.Mount mount = ContainerSpec.Mount.cacheMount(cacheVolumeName, target,cacheDriverName);
        this.TaskTemplate.ContainerSpec.Mounts.add(mount);
    }
    public void addTmpfsMount(String tmpfsDir) {
        ContainerSpec.Mount mount = ContainerSpec.Mount.tmpfsMount(tmpfsDir);
        this.TaskTemplate.ContainerSpec.Mounts.add(mount);
    }

    public void setTaskLimits(long nanoCPUs, long memoryBytes) {
        this.TaskTemplate.Resources.Limits.NanoCPUs = nanoCPUs;
        this.TaskTemplate.Resources.Limits.MemoryBytes = memoryBytes;
    }

    public void setTaskReservations(long nanoCPUs, long memoryBytes) {
        this.TaskTemplate.Resources.Reservations.NanoCPUs = nanoCPUs;
        this.TaskTemplate.Resources.Reservations.MemoryBytes = memoryBytes;
    }

    public void setNetwork(String network) {
        if(network!= null && network != ""){
            Networks.add(new Network(network));
        }
    }
    public  void addLabel(String key, String value){
        this.Labels.put(key,value);
    }
}
