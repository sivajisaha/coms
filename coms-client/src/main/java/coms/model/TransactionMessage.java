package coms.model;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter @NoArgsConstructor
public class TransactionMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	private ProcessContext context;
	private String data;
	
	 @Override
	  public String toString() {
	    return "Message [context=" + context.getTransactionCode() + ", data=" + data + "]";
	  }
}