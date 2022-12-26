package coms.bpm.scheduler.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class FailedTransaction {
	private Long id;
	private Long process_instance_id;
	private String element_code;
}