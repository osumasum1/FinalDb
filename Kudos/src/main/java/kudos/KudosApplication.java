package kudos;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
//import com.datastax.*;
//import com.datastax.driver.core.Cluster;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import io.dropwizard.Application;
//import systems.composable.dropwizard.cassandra.CassandraFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import kudos.db.KudoDao;
import kudos.influx.InfluxDataBase;
import kudos.rabbit.RPCServer;
import kudos.rabbit.ReceiverDeleteKudos;
import kudos.resources.KudosResource;

public class KudosApplication extends Application<KudosConfiguration> {


    public static void main(String[] args) throws Exception {
        new KudosApplication().run("server", args[0]);
    }

    @Override
    public void initialize(Bootstrap<KudosConfiguration> bootstrap) {
    	/*
    	
    	bootstrap.addBundle(new CassandraBundle<KudosConfiguration>() {
            @Override
            public CassandraFactory getCassandraFactory(KudosConfiguration configuration) {
                return configuration.getCassandraFactory();
            }
         });
         */
         
    }

    @Override
    public void run(KudosConfiguration config, Environment env)
            throws Exception {
    	
    	
    	/*
    	Builder b = Cluster.builder().addContactPoint("localhost");
    	b.withPort(9042);
    	b.withoutMetrics();
    	
    	Cluster cluster = b.build();
    	Session session = cluster.connect("kudos");
    	
    	ResultSet results = session.execute("SELECT * FROM kudos");
    	for (Row row : results) {
    		   String value = row.getString("lugar");
    		   System.out.println("============================================"+value);
    		}
    	
    	
    	//Cluster cassandra = config.getCassandraFactory().build(env);
    	/*
        MongoClient mongoClient = new MongoClient(config.getMongoHost(), config.getMongoPort());
        MongoManaged mongoManaged = new MongoManaged(mongoClient);
        env.lifecycle().manage(mongoManaged);
        MongoDatabase db = mongoClient.getDatabase(config.getMongoDB());
        MongoCollection<Document> collection = db.getCollection(config.getCollectionName());
        
        */
      //  Cluster cassandra = config.getCassandraFactory().build(env);

    	/*
      Cluster cluster = Cluster.builder().addContactPoint("192.168.99.100").withPort(9091).build();
        final Metadata metadata = cluster.getMetadata();
        System.out.printf("Connected to cluster: %s\n", metadata.getClusterName());
        for (final Host host : metadata.getAllHosts())
        {
        	  System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n",
              host.getDatacenter(), host.getAddress(), host.getRack());
        }
        Session session  = session = cluster.connect();
        
        //http://192.168.99.100:9091
        /*
        Cluster cluster;
        Builder b = Cluster.builder().addContactPoint(node);
        
        /*
        Cluster cassandra = Cluster.builder().addContactPoints("172.17.0.2").withPort(9042).build();
        Session sesion = cassandra.connect("kudos");
        /*
       final Cluster cassandra = Cluster.builder()
        		.withClusterName("my-dse")
                .withPort(9042)
                .withRetryPolicy(DefaultRetryPolicy.INSTANCE)
               // .withLoadBalancingPolicy(new TokenAwarePolicy(DCAwareRoundRobinPolicy.builder().build()))
                .addContactPoint("172.17.0.2")
                .build();//config.getCassandraFactory().build(env);
        
        /*
         Cluster.builder()
            .withClusterName(clusterName)
            .withPort(port)
            .withRetryPolicy(DefaultRetryPolicy.INSTANCE)
            // TokenAware requires query has routing info (e.g. BoundStatement with all PK value bound).
            .withLoadBalancingPolicy(new TokenAwarePolicy(DCAwareRoundRobinPolicy.builder().build()))
            .addContactPoints(contactPoints.toArray(new String[contactPoints.size()]))
            .build();
         */
       
        final InfluxDataBase influx = new InfluxDataBase(config.getInfluxHost(), config.getInfluxDataBase());
        
        Builder b = Cluster.builder().addContactPoint(config.getCassandraContactPoint());
    	b.withPort(Integer.valueOf(config.getCassandraPort()));
    	b.withoutMetrics();
    	
    	Cluster cluster = b.build();
    	Session session = cluster.connect(config.getCassandraKeyspace());
        
        KudoDao kudoDao = new KudoDao(session);
       
       KudosResource resource = new KudosResource(kudoDao,influx);
       env.jersey().register(new KudosResource(kudoDao,influx));
       
       RPCServer rpc = new RPCServer(resource);
       ReceiverDeleteKudos recv =  new ReceiverDeleteKudos(resource);
       rpc.start();
       recv.start();
    }

}
