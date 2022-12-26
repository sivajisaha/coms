package coms.bpm.scheduler.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ProcessContext {
	private Long processInstanceId;
	private String processCode;
	private String version;
	private String transactionCode;
	private String targetactionCode;//"TRIGGER" or "COMPLETE" or "HOLD" or "REJECT" or "CANCEL"
	private String transactionComment;
}
