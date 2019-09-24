package users.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

import io.dropwizard.hibernate.UnitOfWork;
import users.core.User;
import users.db.MongoService;
import users.influx.InfluxDataBase;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {
	
	private MongoCollection<Document> collection;
    private final MongoService mongoService;
	private InfluxDataBase influx;
    
    public UsersResource(MongoCollection<Document> collection, MongoService mongoService, InfluxDataBase influx) {
    	this.collection = collection;
        this.mongoService = mongoService;
    	this.influx = influx;
    }
    
    @GET
    @Path("/{id}")
    @UnitOfWork
    public String findById(@PathParam("id") int id) throws Exception {
    	
    	System.out.println(String.format(" ID to find in database:%s", id));
    	Document user = mongoService.findById(collection, id);
    	if (user != null) {
    		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        	String json = ow.writeValueAsString(user);
        	return json;
    	} else {
    		return String.format("User with ID %s not found", id);
    	}
    	
    	/*
        String response = null;
    	try (RPCClient rpcClient = new RPCClient()) {
    		String i_str = Long.toString(id.get());
            System.out.println(" [x] Requesting kudos(" + i_str + ")");
            response = rpcClient.call(i_str);
            System.out.println(" [.] Got '" + response + "'");
            
            response = response.replaceAll("Document", "kudo");
            
            List<Document> preview  =  mongoService.findById(collection, id.get());
        	Document user = preview.get(0);
        	//user.setKudosList(response);
        	
        	ArrayList<Object> resp = new ArrayList<Object>();
        	//JsonObject jsonObject = new JsonParser().parse("\"kudos\":"+response).getAsJsonObject();
        	
        	ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        	String json = ow.writeValueAsString(user);
        	//json.replaceAll("[", "")
        	
        	resp.add(json);
        	//resp.add("\"kudos\":"+response);
        	//resp.add("\"kudos\":"+response);
        	//resp.add(jsonObject);
        	influx.insert("User Get By ID: "+id.get());
        	return json;
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
            influx.insert("User Get By ID Error: "+e);
            return null;
        }
    	*/
    }
    
    @GET
    @Path("/allUsers")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @UnitOfWork
    public Response getAllUsers(
    		@QueryParam("pagenumber") int pageNumber,
    		@QueryParam("pagesize") int pageSize) throws Exception {
    	List<Document> documents = mongoService.findSimple(collection, pageNumber ,pageSize);
        influx.insert("Users: Get All Simple Users");
        return Response.ok(documents).build();
    }
    

    @GET
    @Path("/searchUsers")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @UnitOfWork
    public Response findByName(
    		@QueryParam("firstName") String firstName, 
    		@QueryParam("nickname")  String nickname,
    		@QueryParam("pagenumber") int pageNumber,
    		@QueryParam("pagesize") int pageSize) {
        
    	influx.insert(String.format("User Get By First Name OR Nickname: %s %s", firstName, nickname));
    	List<Document> documents = mongoService.findByNameOrNickname(collection, firstName, nickname, pageNumber, pageSize);
    	return Response.ok(documents).build();
    }
    
    
    @POST
    @Path("/createUser")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @UnitOfWork
    public Response insertUser(
    		@FormParam("nickname") String nickname,
    		@FormParam("username") String userName,
    		@FormParam("firstName") String firstName,
    		@FormParam("lastName") String lastName
    		) {
          	
    	System.out.println(" Testing create user endpoint...");
    	Gson gson = new Gson();
        String json = gson.toJson(
        		new User(mongoService.getNextSequence(collection),nickname, userName, firstName, lastName));
        System.out.println(" Converting to json: " + json.toString());
        mongoService.insertOne(collection, new Document(BasicDBObject.parse(json)));
        Map<String, String> response = new HashMap<>();
        response.put("message", "User created successfully");
        
        /*SendCalculateKudos calculateKudos = new SendCalculateKudos();
        calculateKudos.sendMessage(Long.valueOf(kudo.getDestino()));
        */
        influx.insert("User Created: "+json);
        return Response.ok(response).build();
      
    }
    
    @DELETE
    @Path("delete/{id}")
    @UnitOfWork
    public Response deleteUserById(@PathParam("id") int id) throws Exception {
    	mongoService.deleteOneByObjectId(collection, id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        influx.insert("User Deleted: "+id);
        return Response.ok(response).build();
        /*if (resp=true) {
    		Send send = new Send();
    		send.sendMessage(id.get());
    		influx.insert("User Deleted:"+ id.get());
    	}
    	return resp;*/
    }

}
