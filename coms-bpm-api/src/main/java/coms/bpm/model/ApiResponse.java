package coms.bpm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ApiResponse {
	String transaction_code;
	String transaction_status;
	String response_message;
	String response_status;
	public ApiResponse(String tran_code,String response_message,String response_status,String tran_status)
	{
		setTransaction_code(tran_code);
		setTransaction_status(tran_status);
		setResponse_message(response_message);
		setResponse_status(response_status);
	}
}