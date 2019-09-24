package users;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.*;

import javax.validation.constraints.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsersConfiguration extends Configuration {
	 @JsonProperty
	    @NotEmpty
	    public String mongoHost;

	    @JsonProperty
	    @Min(1)
	    @Max(65535)
	    public int mongoPort;

	    @JsonProperty
	    @NotEmpty
	    public String mongoDB;
	    
	    @JsonProperty
	    @NotEmpty
	    public String influxHost;
		
		@JsonProperty
	    @NotEmpty
	    public String influxDataBase;
	    
	    @JsonProperty
	    @NotEmpty
	    public String collectionName;

	    public String getMongoHost() {
	        return mongoHost;
	    }

	    public void setMongoHost(String mongoHost) {
	        this.mongoHost = mongoHost;
	    }

	    public int getMongoPort() {
	        return mongoPort;
	    }

	    public void setMongoPort(int mongoPort) {
	        this.mongoPort = mongoPort;
	    }

	    public String getMongoDB() {
	        return mongoDB;
	    }

	    public void setMongoDB(String mongoDB) {
	        this.mongoDB = mongoDB;
	    }

	    public String getCollectionName() {
	        return collectionName;
	    }

	    public void setCollectionName(String collectionName) {
	        this.collectionName = collectionName;
	    }
	    
		public String getInfluxDataBase() {
			return influxDataBase;
		}


		public void setInfluxDataBase(String influxDataBase) {
			this.influxDataBase = influxDataBase;
		}

		public String getInfluxHost() {
			return influxHost;
		}

		public void setInfluxHost(String influxHost) {
			this.influxHost = influxHost;
		}
}
