package users;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import users.auth.UserAuthenticator;
import users.core.Session;
import users.core.User;
import users.db.MongoManaged;
import users.db.MongoService;
import users.db.UserDAO;
import users.influx.InfluxDataBase;
import users.rabbit.Send;
import users.resources.UsersResource;

import org.bson.Document;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class UsersApplication extends Application<UsersConfiguration> {

    public static void main(final String[] args) throws Exception {
        new UsersApplication().run(args);
    }
    
    

    @Override
    public void initialize(final Bootstrap<UsersConfiguration> bootstrap) {
    //	bootstrap.addBundle(hibernateBundle);
    	
    }
    

    @Override
    public void run(final UsersConfiguration configuration,
                    final Environment environment) throws Exception {
    	
    	// Mongo for Users
    	MongoClient mongoClient = new MongoClient(configuration.getMongoHost(), configuration.getMongoPort());
        MongoManaged mongoManaged = new MongoManaged(mongoClient);
        environment.lifecycle().manage(mongoManaged);
        MongoDatabase db = mongoClient.getDatabase(configuration.getMongoDB());
        MongoCollection<Document> collection = db.getCollection(configuration.getCollectionName());
       
        final InfluxDataBase influx = new InfluxDataBase(configuration.getInfluxHost(), configuration.getInfluxDataBase());
        
        UsersResource resource = new UsersResource(collection, new MongoService(), influx);
        environment.jersey().register(new UsersResource(collection, new MongoService(), influx));
        
        /*RPCServer rpc = new RPCServer(resource);
        ReceiverDeleteKudos recv =  new ReceiverDeleteKudos(resource);
        rpc.start();
        recv.start();*/
    }

}
