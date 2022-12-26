package coms.bpm.model.message.mapper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class SimpleJsonMapper {
	String source_json_schema;
	String destination_json_schema;
	SimpleJsonNodeMapper[] mappings;

}