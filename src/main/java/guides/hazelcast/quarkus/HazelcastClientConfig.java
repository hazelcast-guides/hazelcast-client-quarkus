package guides.hazelcast.quarkus;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientClasspathXmlConfig;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class HazelcastClientConfig {

    @Produces
    HazelcastInstance createInstance() {
        return HazelcastClient.newHazelcastClient(new ClientClasspathXmlConfig("hazelcast.xml"));
    }
}
