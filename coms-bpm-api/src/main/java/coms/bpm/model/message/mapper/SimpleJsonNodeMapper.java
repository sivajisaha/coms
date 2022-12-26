package coms.bpm.model.message.mapper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class SimpleJsonNodeMapper {
	String mapping_type;
    String source_node;
    String destination_path;
    String destination_node;

}
