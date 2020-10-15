import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Json_Parser {
  private String json_path;
  private String collection_name;

	 /*Il main sarà eliminato
  public static void main (String args[]) {
    Json_Parser blue_growth_json_parser = new Json_Parser("C:\\Users\\Flavio\\Documents\\Workspace Eclipse JEE\\metadata-module", "Blue Growth");
    DocumentListJson collection_data_object = blue_growth_json_parser.parseJson();

    System.out.println(collection_data_object.getCollection());
  }*/

  public Json_Parser(String _json_path, String _collection_name) {
    json_path = _json_path;
    collection_name = _collection_name;
  }
  
  public Json_Parser() {};

	/**
	 * testing deserialization on json files
	 */
	public DocumentListJson parseJson() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			File file_json = new File(json_path + "/" + collection_name + ".json");
			return mapper.readValue(file_json, DocumentListJson.class);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public DocumentListJson parseJson(String json_string) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json_string, DocumentListJson.class);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
