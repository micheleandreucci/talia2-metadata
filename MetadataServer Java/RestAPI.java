import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.ParseException;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import databaseObjects.*;

//@Path("v1")
public class RestAPI {
	
	@PUT
	@Path("loadJsonData")
	public Response loadJsonData(final String json_string) {
		Json_Parser jParser = new Json_Parser();
		
		DocumentListJson documents_json = jParser.parseJson(json_string); //parse Json string into DocumentListJson object
		
		multipleDBRequest DBLoader;
		
		try {
			
			DBLoader = new multipleDBRequest();
			
			DBLoader.loadAllJsonData(documents_json); //send multiple requests to DB exploring all contents of DocumentListJson object
		
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).type("text/plain").build();
		
		} catch (NumberFormatException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).type("text/plain").build();
		
		} catch (UnsupportedEncodingException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).type("text/plain").build();
		
		} catch (ParseException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).type("text/plain").build();
		
		} catch (UnknownDeliverableException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).type("text/plain").build();
		}
		
		return Response.ok().build();
	}
	
	@GET
	@Path("getDeliverableData")
	public Response getDeliverableData(final int docid) {
		
		DeliverableDB deliverableData = new DeliverableDB();
		ObjectMapper mapper = new ObjectMapper();
		
		try {
		
			DatabaseInterface db = new DatabaseInterface();
			
			deliverableData = db.getDeliverableData(docid);
			
			String deliverableJson = mapper.writeValueAsString(deliverableData);
			
			return Response.ok(deliverableJson).build();
			
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).type("text/plain").build();
		
		} catch (JsonProcessingException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).type("text/plain").build();
		}	
	}
}