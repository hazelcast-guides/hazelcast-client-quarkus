package guides.hazelcast.quarkus;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@ApplicationScoped
public class HazelcastClientConfig {

    @Produces
    @Singleton
    HazelcastInstance createInstance() {
        ClientConfig clientConfig = new ClientConfig();
        String[] members = System.getenv("HAZELCAST_IP").split(",");
        clientConfig.getNetworkConfig().addAddress(members);
        return HazelcastClient.newHazelcastClient(clientConfig);
    }
}
