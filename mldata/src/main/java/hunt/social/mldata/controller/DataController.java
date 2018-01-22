package hunt.social.mldata.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

import hunt.social.mldata.util.MongoUtil;

@Controller
public class DataController {
	
	@RequestMapping(value="/api/{collName}/getNextBatch", method = RequestMethod.GET)
	public ResponseEntity<?> getNextBatch(@RequestParam("count") int count, @PathVariable("collName") String collName) {
		
		MongoClient client = MongoUtil.getClient();

		MongoDatabase database = client.getDatabase("mldata");
		MongoCollection<Document> mgdbcollection = database.getCollection(collName);
		
		BasicDBObject query = new BasicDBObject();
		query.put("status", 0);
		
		MongoCursor<Document> cursor = mgdbcollection.find(query).limit(count).iterator();
		UpdateOptions options = new UpdateOptions().upsert(true);
		
		List<Document> docs = new ArrayList<Document>();
		
		try {
			while (cursor.hasNext()) {
				Document d = cursor.next();
				docs.add(d);
				d.put("status", 1);
				if(d.containsKey("updateDate")){
					d.put("updateDate", new Date());
				}else{
					d.append("updateDate", new Date());
				}
				mgdbcollection.updateOne(new Document("_id",d.getString("_id")), new Document("$set", d), options);			
			}
		} finally {
			cursor.close();
			client.close();
		}

		return new ResponseEntity<Object>(docs, new HttpHeaders(), HttpStatus.OK);

	}
	
	@RequestMapping(value="/api/{collName}/getNextCheckBatch", method = RequestMethod.GET)
	public ResponseEntity<?> getNextCheckBatch(@RequestParam("count") int count, @RequestParam("uId") String uId, @PathVariable("collName") String collName) {
		
		MongoClient client = MongoUtil.getClient();

		MongoDatabase database = client.getDatabase("mldata");
		MongoCollection<Document> mgdbcollection = database.getCollection(collName);
		BasicDBObject userQuery = new BasicDBObject("$ne", uId);

		BasicDBObject query = new BasicDBObject();
		query.put("status", 2);
		query.put("tagBy", userQuery);
		
		MongoCursor<Document> cursor = mgdbcollection.find(query).limit(count).iterator();
		UpdateOptions options = new UpdateOptions().upsert(true);
		
		List<Document> docs = new ArrayList<Document>();
		
		try {
			while (cursor.hasNext()) {
				Document d = cursor.next();
				docs.add(d);
				d.put("status", 3);
				if(d.containsKey("updateDate")){
					d.put("updateDate", new Date());
				}else{
					d.append("updateDate", new Date());
				}
				mgdbcollection.updateOne(new Document("_id",d.getString("_id")), new Document("$set", d), options);			
			}
		} finally {
			cursor.close();
			client.close();
		}

		return new ResponseEntity<Object>(docs, new HttpHeaders(), HttpStatus.OK);

	}
	
	@RequestMapping(value="/api/{collName}/getCheckResult", method = RequestMethod.GET)
	public ResponseEntity<?> getCheckResult(@RequestParam("uId") String uId, @PathVariable("collName") String collName) {
		
		MongoClient client = MongoUtil.getClient();

		MongoDatabase database = client.getDatabase("mldata");
		MongoCollection<Document> mgdbcollection = database.getCollection(collName);
		
		
		
		try {
			BasicDBObject query = new BasicDBObject();
			//query.put("status", 2);
			query.put("tagBy", uId);
			long totalTag = mgdbcollection.count(query);
			query.put("check", 0);
			long wrongTag = mgdbcollection.count(query);
			query.put("check", 1);
			long correctTag = mgdbcollection.count(query);
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("totalTag", totalTag);
			res.put("wrongTag", wrongTag);
			res.put("correctTag", correctTag);
			res.put("totalCheck", correctTag+wrongTag);
			return new ResponseEntity<Object>(res, new HttpHeaders(), HttpStatus.OK);
		} finally {
			client.close();
		}
	}
	
	@RequestMapping(value="/api/{collName}/getCheckDetail", method = RequestMethod.GET)
	public ResponseEntity<?> getCheckDetail(@RequestParam("count") int count, @RequestParam("uId") String uId, @PathVariable("collName") String collName) {
		
		MongoClient client = MongoUtil.getClient();

		MongoDatabase database = client.getDatabase("mldata");
		MongoCollection<Document> mgdbcollection = database.getCollection(collName);
		BasicDBObject query = new BasicDBObject();
		//query.put("status", 2);
		query.put("tagBy", uId);
		query.put("check", 0);
			
		
		MongoCursor<Document> cursor = mgdbcollection.find(query).limit(count).iterator();
		
		List<Document> docs = new ArrayList<Document>();
		
		try {
			while (cursor.hasNext()) {
				Document d = cursor.next();
				docs.add(d);	
			}
		} finally {
			cursor.close();
			client.close();
		}

		return new ResponseEntity<Object>(docs, new HttpHeaders(), HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/api/{collName}/getQuizData", method = RequestMethod.GET)
	public ResponseEntity<?> getQuizData(@PathVariable("collName") String collName) {
		
		MongoClient client = MongoUtil.getClient();

		MongoDatabase database = client.getDatabase("mldata");
		MongoCollection<Document> mgdbcollection = database.getCollection(collName+"_quiz");
		
		BasicDBObject query = new BasicDBObject();
		
		MongoCursor<Document> cursor = mgdbcollection.find(query).limit(20).iterator();
		
		List<Document> docs = new ArrayList<Document>();
		
		try {
			while (cursor.hasNext()) {
				Document d = cursor.next();
				docs.add(d);			
			}
		} finally {
			cursor.close();
			client.close();
		}

		return new ResponseEntity<Object>(docs, new HttpHeaders(), HttpStatus.OK);

	}
	
	@PostMapping("/api/{collName}/updateData")
	public ResponseEntity<?> updateData(@PathVariable("collName") String collName, @RequestBody List<Map<String, String>> postData) {
		MongoClient client = null;
		try {
				client = MongoUtil.getClient();
				MongoDatabase database = client.getDatabase("mldata");
				MongoCollection<Document> mgdbcollection = database.getCollection(collName);
				
				for(Map<String, String> data : postData){
					Document updateDoc = new Document();
					updateDoc.append("status", 2);
					updateDoc.append("tagBy", data.get("tagBy"));
					updateDoc.append("label", data.get("label"));
					mgdbcollection.updateOne(new Document("_id", data.get("_id")), new Document("$set", updateDoc));
				}
				return new ResponseEntity<String>("update success", new HttpHeaders(), HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<String>("update failed", new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}finally{
				if(client != null){
					client.close();
				}
			}
	}
	
	@PostMapping("/api/{collName}/updateCheckData")
	public ResponseEntity<?> updateCheckData(@PathVariable("collName") String collName, @RequestBody List<Map<String, String>> postData) {
		MongoClient client = null;
		try {
				client = MongoUtil.getClient();
				MongoDatabase database = client.getDatabase("mldata");
				MongoCollection<Document> mgdbcollection = database.getCollection(collName);
				
				for(Map<String, String> data : postData){
					Document updateDoc = new Document();
					updateDoc.append("status", 4);
					updateDoc.append("check", data.get("check"));
					updateDoc.append("checkBy", data.get("checkBy"));
					mgdbcollection.updateOne(new Document("_id", data.get("_id")), new Document("$set", updateDoc));
				}
				return new ResponseEntity<String>("update success", new HttpHeaders(), HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<String>("update failed", new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}finally{
				if(client != null){
					client.close();
				}
			}
	}
	
	
	@PostMapping("/api/{collName}/cancelData")
	public ResponseEntity<?> cancelData(@PathVariable("collName") String collName, @RequestBody List<Map<String, String>> postData) {
		MongoClient client = null;
		try {
				client = MongoUtil.getClient();
				MongoDatabase database = client.getDatabase("mldata");
				MongoCollection<Document> mgdbcollection = database.getCollection(collName);
				
				for(Map<String, String> data : postData){
					Document updateDoc = new Document();
					if(!"1".equals(data.get("status"))) {
						continue;
					}
					updateDoc.append("status", 0);
					mgdbcollection.updateOne(new Document("_id", data.get("_id")), new Document("$set", updateDoc));
				}
				return new ResponseEntity<String>("cancel success", new HttpHeaders(), HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<String>("cancel failed", new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}finally{
				if(client != null){
					client.close();
				}
			}
	}
	
	@PostMapping("/api/{collName}/cancelCheckData")
	public ResponseEntity<?> cancelCheckData(@PathVariable("collName") String collName, @RequestBody List<Map<String, String>> postData) {
		MongoClient client = null;
		try {
				client = MongoUtil.getClient();
				MongoDatabase database = client.getDatabase("mldata");
				MongoCollection<Document> mgdbcollection = database.getCollection(collName);
				
				for(Map<String, String> data : postData){
					Document updateDoc = new Document();
					if(!"3".equals(data.get("status"))) {
						continue;
					}
					updateDoc.append("status", 2);
					mgdbcollection.updateOne(new Document("_id", data.get("_id")), new Document("$set", updateDoc));
				}
				return new ResponseEntity<String>("cancel success", new HttpHeaders(), HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<String>("cancel failed", new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}finally{
				if(client != null){
					client.close();
				}
			}
	}
	
	@PostMapping("/api/{collName}/createQuiz")
	public ResponseEntity<?> createQuiz(@RequestParam("type") int type, @PathVariable("collName") String collName, @RequestBody List<Map<String, String>> postData) {
		MongoClient client = null;
		try {
			 	UpdateOptions options = new UpdateOptions().upsert(true);
				client = MongoUtil.getClient();
				MongoDatabase database = client.getDatabase("mldata");
				MongoCollection<Document> mgdbcollection = database.getCollection(collName+"_quiz");
				
				for(Map<String, String> data : postData){
					Document updateDoc = new Document();
					System.out.println(data);
					if(type == 1) {
						updateDoc.append("content", data.get("content"));
					}else {
						updateDoc.append("src", data.get("content"));
					}
					updateDoc.append("_id", data.get("_id"));
					updateDoc.append("type", type);
					updateDoc.append("label", data.get("label"));
					mgdbcollection.updateOne(new Document("_id", data.get("_id")), new Document("$set", updateDoc), options);
				}
				return new ResponseEntity<String>("create quiz data success", new HttpHeaders(), HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<String>("create quiz data failed", new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}finally{
				if(client != null){
					client.close();
				}
			}
	}

}
