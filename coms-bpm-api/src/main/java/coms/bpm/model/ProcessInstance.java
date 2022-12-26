package coms.bpm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ProcessInstance {
	private Long id;
	private Long processDefinitionId;
	private String status;
}
