package hunt.social.mldata.util;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Component
public class MongoUtil {
	public static String mongoHost;
	
	public static MongoClient getClient(){
		MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(50).connectTimeout(60000).maxWaitTime(120000).threadsAllowedToBlockForConnectionMultiplier(50).socketKeepAlive(false).build();
		MongoCredential credential = MongoCredential.createCredential("mldata",
				"mldata", "123QWEasd".toCharArray());
		MongoClient mongoClient = new MongoClient(new ServerAddress(mongoHost, 50000), Arrays.asList(credential), options);
		return mongoClient;
	}
	
	@Value("${ml.mongodb.host}")
    public void setMongoHost(String value) {
        this.mongoHost = value;
    }
}
