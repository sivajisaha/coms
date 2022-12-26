package coms.bpm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class HumanTaskAssignment {
	Long id;
	String task_code;
	Long process_definition_id;
	Long task_assignment_entity_id;
	String task_assignment_entity_type;
}
