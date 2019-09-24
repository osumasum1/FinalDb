package kudos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
//import systems.composable.dropwizard.cassandra.CassandraFactory;
import systems.composable.dropwizard.cassandra.CassandraFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KudosConfiguration extends Configuration {

     
    @JsonProperty
    @NotEmpty
    public String influxHost;
	
	@JsonProperty
    @NotEmpty
    public String influxDataBase;
    
   
    
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
	
	
	//Kasandra
	@JsonProperty
    @NotEmpty
    public String cassandraContactPoint;
	
	@JsonProperty
    @NotEmpty
    public String cassandraPort;
	
	@JsonProperty
    @NotEmpty
    public String cassandraKeyspace;

	public String getCassandraContactPoint() {
		return cassandraContactPoint;
	}

	public void setCassandraContactPoint(String cassandraContactPoint) {
		this.cassandraContactPoint = cassandraContactPoint;
	}

	public String getCassandraPort() {
		return cassandraPort;
	}

	public void setCassandraPort(String cassandraPort) {
		this.cassandraPort = cassandraPort;
	}




	public String getCassandraKeyspace() {
		return cassandraKeyspace;
	}




	public void setCassandraKeyspace(String cassandraKeyspace) {
		this.cassandraKeyspace = cassandraKeyspace;
	}


	
	
	
	
}
