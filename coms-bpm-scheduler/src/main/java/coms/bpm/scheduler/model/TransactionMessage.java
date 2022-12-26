package coms.bpm.scheduler.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter @NoArgsConstructor
public class TransactionMessage {
	private ProcessContext context;
	private String data;
}
