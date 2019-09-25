package kudos.resources;

import com.codahale.metrics.annotation.Timed;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

import io.dropwizard.hibernate.UnitOfWork;
import kudos.core.Kudo;
import kudos.db.KudoDao;
import kudos.influx.InfluxDataBase;
import kudos.rabbit.RPCServer;
import kudos.rabbit.SendCalculateKudos;

import org.bson.Document;
 
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Path("/kudos")
@Produces(MediaType.APPLICATION_JSON)
public class KudosResource {
	

    private InfluxDataBase influx;
    private KudoDao kudoDao;
 
    public KudosResource(KudoDao kudoDao, InfluxDataBase influx) {
        this.influx = influx;
        this.kudoDao = kudoDao; 
    }
    
    @POST
    @Timed
    @Path("/createkudo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response createKudo(@NotNull @Valid final Kudo kudo) throws Exception {
    	kudoDao.createKudo(kudo);
    	
    	Gson gson = new Gson();
    	String json = gson.toJson(kudo);
    	
    	influx.insert("[Create Kudo] Kudo creado ");
    	return Response.ok(json).build();
    }
    
    @DELETE
    @Timed
    @Path("/deleteKudo/{id}")
    @UnitOfWork
    public Response deleteKudo(@PathParam("id") final UUID id) {
    	kudoDao.deleteKudo(id);
    	
    	Map<String, String> response = new HashMap<>();
        response.put("message", "Kudo deleted successfully");
        influx.insert("[Delete Kudo] Kudo Eliminado "+id);// [1/1/2018 10:20:30] [Delete User] Eliminado usuario 10
        return Response.ok(response).build();
    }
    
    @GET
    @Timed
    @Path("/allKudos")
    @UnitOfWork
    public Response getKudos(
    		@QueryParam("page") Optional<Integer> page,
    		@QueryParam("size") Optional<Integer> size
    		) {
    	List<Kudo> resp =  kudoDao.getAllKudos();
    	List<Kudo> resp1 =  new ArrayList<Kudo>();
    	
    	if (page.isPresent() && size.isPresent()) {
    		resp1 = kudoDao.getAllKudosPagination(size.get(), page.get());
    		influx.insert("[Get Kudos] Kudos conseguidos para la pagina"+page +" de tamano "+size);
    		return Response.ok(resp1).build();
    	}
    	else {
    		influx.insert("[GET Kudo] Kudos recuperados");
    		return Response.ok(resp).build();
    	}
    	
        
    }
    
    /*
     @GET
    @UnitOfWork
    public List<User> findByName(@QueryParam("firstName") Optional<String> firstName, 
    		@QueryParam("nickname") Optional<String> nickname) {
        
    	if (firstName.isPresent() && nickname.isPresent()) {
    		influx.insert("User Get By First Name && Nickname: "+firstName.get()+"     "+nickname.get());
    		return userDao.findByFirstNameNickname(firstName.get(),nickname.get());
        }
    	else {
    		influx.insert("User Get All");
    		return userDao.findAll();
    	}
    	
        
    }
     
    /*
    @POST
    @Path("/createKudoForm")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @UnitOfWork
    public Response insertUser(
    		@FormParam("id") UUID id,
    		@FormParam("fuente") int fuente,
    		@FormParam("destino") int destino,
    		@FormParam("tema") String tema,
    		@FormParam("fecha") String fecha,
    		@FormParam("lugar") String lugar,
    		@FormParam("texto") String texto
    		
    		) {
       
    	Kudo kudo = new Kudo(id, fuente, destino, tema, fecha, lugar, texto);
    	kudoDao.createKudo(kudo);
    	Gson gson = new Gson();
    	String json = gson.toJson(kudo);
    	return Response.ok(json).build();
    }
    
    
    /*
    
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/users")
    public String getUserso() {
        try (final Session session = cluster.connect()) {
            final ResultSet resultSet = session.execute("SELECT * FROM kudos");
            return "ok";
        }
        
    }
 
    @POST
    @Timed
    @Path("/createkudo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createKudo(@NotNull @Valid final Kudo kudo) throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(kudo);
        mongoService.insertOne(collection, new Document(BasicDBObject.parse(json)));
        Map<String, String> response = new HashMap<>();
        response.put("message", "Kudo created successfully");
        
        SendCalculateKudos calculateKudos = new SendCalculateKudos();
        calculateKudos.sendMessage(Long.valueOf(kudo.getDestino()));
        influx.insert("Kudo Created: "+json);
        return Response.ok(response).build();
    }
 
    @GET
    @Timed
    public Response getKudos() {
        List<Document> documents = mongoService.find(collection);
        influx.insert("Kudo: GET all kudos");
        return Response.ok(documents).build();
    }
    
    @GET
    @Timed
    @Path("/simpleKudos")
    public Response getSimpleKudos() {
        List<Document> documents = mongoService.findSimple(collection);
        influx.insert("Kudo: Get All Simple Kudos");
        return Response.ok(documents).build();
    }
 
    @GET
    @Timed
    @Path("/destino/{id}")
    public Response getKudoByDestino(@PathParam("id") final int id) {
        List<Document> documents = mongoService.findByKey(collection, "destino", id);
        influx.insert("Kudo: GET by destino "+id);
        return Response.ok(documents).build();
    }
    
    @DELETE
    @Timed
    @Path("{id}")
    public Response deleteKudo(@PathParam("id") final String id) {
        mongoService.deleteOneByObjectId(collection,id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Kudo deleted successfully");
        influx.insert("Kudo Deleted: "+id);
        return Response.ok(response).build();
    }
    
    @DELETE
    @Timed
    @Path("/user/{id}")
    public Response deleteAllKudoUser(@PathParam("id") final String id) throws NumberFormatException, Exception {
        mongoService.deleteAllKudosUser(collection,Long.valueOf(id));
        Map<String, String> response = new HashMap<>();
        response.put("message", "Kudos deleted successfully");
        influx.insert("Kudo Deleted: All kudos where the user appears (fuente and destino)");
        return Response.ok(response).build();
    }
    
 */
}
    
    
