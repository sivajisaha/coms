package coms.bpm.model;

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
	private String transactionOutcome;
	private String transactionComment;
	public ProcessContext(Long processinstanceid,String transactioncode,String targetactioncode,String transactionoutcome, String transactioncomment)
	{
		setProcessInstanceId(processinstanceid);
		setTransactionCode(transactioncode);
		setTargetactionCode(targetactioncode);
		setTransactionOutcome(transactionoutcome);
		setTransactionComment(transactioncomment);
	}
}
