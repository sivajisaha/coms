package coms.bpm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class JsonSchemaDefinition {
	private Long id;
	private Long process_definition_id;
	private String schema_name;
	private String schema_category;
	private String schema_definition;
	public JsonSchemaDefinition(Long process_definition_id,String schema_name,String schema_category,String schema_definition)
	{
		this.process_definition_id = process_definition_id;
		this.schema_name = schema_name;
		this.schema_category = schema_category;
		this.schema_definition = schema_definition;
	}
}
