package coms.bpm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class JsonMessageConversion {
	private Long process_definition_id;
	private String source_json_message;
	private String destination_json_message;
	private String message_conversion_schema;
	public JsonMessageConversion(Long process_definition_id,String src_msg,String dest_msg, String conv_schema)
	{
		setProcess_definition_id(process_definition_id);
		setSource_json_message(src_msg);
		setDestination_json_message(dest_msg);
		setMessage_conversion_schema(conv_schema);
	}
}
