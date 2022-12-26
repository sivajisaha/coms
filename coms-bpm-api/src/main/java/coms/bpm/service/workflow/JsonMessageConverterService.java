package coms.bpm.service.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;

import coms.bpm.model.JsonMessageConversion;
import coms.bpm.model.message.mapper.SimpleJsonMapper;
import coms.bpm.model.message.mapper.SimpleJsonNodeMapper;
@Component
public class JsonMessageConverterService {
	@Autowired
	WorkflowDbService wfdbService;
	 public String convert_json_message(JsonMessageConversion msgconv) throws JsonMappingException, JsonProcessingException  { 
		 System.out.println("conversion mapper --"+ msgconv.getMessage_conversion_schema());
		 	
		 	//String mapperstr = new String(Files.readAllBytes(new ClassPathResource(msgconv.getMessage_conversion_schema()).getFile().toPath()));
		 
		 String mapperstr = wfdbService.getJsonSchemaDef(msgconv.getProcess_definition_id(),msgconv.getMessage_conversion_schema()).getSchema_definition(); 
		 SimpleJsonMapper mapper = new Gson().fromJson(mapperstr, SimpleJsonMapper.class);
			String srcjsonmsgstr = "";
			String destjsonmsgstr = "";
			if(!msgconv.getSource_json_message().isEmpty())
			{
				srcjsonmsgstr = msgconv.getSource_json_message();
			}
			else
			{
				//srcjsonmsgstr =  new String(Files.readAllBytes(new ClassPathResource(mapper.getSource_json_schema()).getFile().toPath()));
				srcjsonmsgstr = wfdbService.getJsonSchemaDef(msgconv.getProcess_definition_id(),mapper.getSource_json_schema()).getSchema_definition(); 
			}
			if(!msgconv.getDestination_json_message().isEmpty())
			{
				destjsonmsgstr = msgconv.getDestination_json_message();
			}
			else
			{
				//destjsonmsgstr = new String(Files.readAllBytes(new ClassPathResource(mapper.getDestination_json_schema()).getFile().toPath()));
				destjsonmsgstr = wfdbService.getJsonSchemaDef(msgconv.getProcess_definition_id(),mapper.getDestination_json_schema()).getSchema_definition(); 
			}
			
			JsonNode rootm1node = new ObjectMapper().readTree(srcjsonmsgstr);
			JsonNode rootm2node = new ObjectMapper().readTree(destjsonmsgstr);
			for(SimpleJsonNodeMapper map :mapper.getMappings())
			{
				if(map.getMapping_type().equals("simple"))
				{
					String srcnodeval = rootm1node.at(map.getSource_node()).asText();
					System.out.println("mapper source--"+ map.getSource_node());
					//System.out.println("srcnodeval--"+ srcnodeval);
					ObjectNode conv = ((ObjectNode)rootm2node.at(map.getDestination_path())).put(map.getDestination_node(), srcnodeval);
				}
			}
			return rootm2node.toPrettyString();
		}

}
