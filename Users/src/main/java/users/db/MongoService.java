package users.db;

import com.google.common.base.Optional;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;

import users.core.User;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
 
import static com.mongodb.client.model.Filters.*;
 
public class MongoService {
 
    public void insertOne(MongoCollection<Document> collection, Document document) {
        collection.insertOne(document);
    }
 
    public void insertMany(MongoCollection<Document> collection, List<Document> documents) {
        collection.insertMany(documents);
    }
 
    public List<Document> find(MongoCollection<Document> collection) {
        return collection.find().into(new ArrayList<>());
    }

	public Document findById(MongoCollection<Document> collection, int value) {
		return collection.find(eq("id", value)).first();
	}
	
	public void deleteOneByObjectId(MongoCollection<Document> collection, int value) {
		collection.deleteOne(new Document("id", value));
	}
   
	public int getNextSequence(MongoCollection<Document> collection) {
		FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
		options.returnDocument(ReturnDocument.AFTER);
		options.upsert(true);
		Bson query = Filters.eq("id", "userid");
		Bson update =  new Document("$inc", new Document("seq", 1));
		List<Document> list = collection.find(query).into(new ArrayList<>());
		if (list.size() > 0) {
			Document result = collection.findOneAndUpdate(query, update, options);
			return result.getInteger("seq");
		} else {
			Document document = new Document();
			document.put("id", "userid");
			document.put("seq", 0);
			collection.insertOne(document);
			return document.getInteger("seq");
		}
	}
	
	public List<Document> findSimple(MongoCollection<Document> collection, int pageNumber, int pageSize) {
        return collection.find(Filters.not(Filters.eq("id", "userid")))
        		.projection(Projections.exclude("_id"))
        		.skip(pageSize*(pageNumber-1))
        		.limit(pageSize)
        		.into(new ArrayList<>());
    }

	public List<Document> findByNameOrNickname(MongoCollection<Document> collection,
												String firstName,String nickname, int pageNumber, int pageSize) {
		
		return collection.find(Filters.or(Filters.eq("firstName", firstName), Filters.eq("nickname", nickname)))
		.projection(Projections.exclude("_id"))
		.skip(pageSize*(pageNumber-1))
		.limit(pageSize)
		.into(new ArrayList<>());
	}
}