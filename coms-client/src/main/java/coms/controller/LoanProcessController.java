package coms.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import com.google.gson.Gson;

import coms.model.ApiResponse;
import coms.model.ProcessContext;
import coms.model.TransactionMessage;

import reactor.core.publisher.Mono;
@RestController
@RequestMapping("/loan")
public class LoanProcessController {
	@Value("${coms.bpmservice.uri}")
	private String bpmserviceUri;
	@Value("${coms.bpmservice.loanprocess.code}")
	private String bpmloanprocesscode;
	@Value("${coms.bpmservice.loanprocess.version}")
	private String bpmloanprocessversion;

	@PostMapping("/apply_loan")
    public String apply_loan(@RequestBody String loan) {
		System.out.println(loan);
		WebClient webClient = WebClient.create(bpmserviceUri);
		TransactionMessage msg = new TransactionMessage();
		ProcessContext pc = new ProcessContext();
		pc.setProcessCode(bpmloanprocesscode);
		pc.setVersion(bpmloanprocessversion);
		pc.setProcessInstanceId(Long.valueOf(0));
		pc.setTransactionComment("");
		msg.setContext(pc);
		msg.setData(loan);
		String response = webClient.post()
				.uri("/postmessage")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(Mono.just(new Gson().toJson(msg)), String.class)
				.retrieve()
				.bodyToMono(String.class).block();
    	return new Gson().toJson(new ApiResponse("loan_submission",response,"Loan application was submiited successfully","success"));
    }
	@PostMapping("/completeness_check")
    public String check_completeness(@RequestBody String msgstr) {
		ApiResponse response = new ApiResponse("check_completeness","check_completeness completed successfully","success","pass");
    	return new Gson().toJson(response);
    }
	@PostMapping("/correctness_check")
    public String check_correctness(@RequestBody String msgstr) {
		ApiResponse response = new ApiResponse("check_correctness","check_correctness completed successfully","success","pass");
    	return new Gson().toJson(response);
    }
	
	@PostMapping("/employment_check")
    public String employment_check(@RequestBody String msgstr) {
		ApiResponse response = new ApiResponse("employment_check","employment_check completed successfully","success","pass");
    	return new Gson().toJson(response);
    }
	@PostMapping("/credit_check")
    public String credit_check(@RequestBody String msgstr) {
		ApiResponse response = new ApiResponse("credit_check","credit_check completed successfully","success","pass");
    	return new Gson().toJson(response);
    }
	@PostMapping("/property_check")
    public String validate_property(@RequestBody String msgstr) {
		ApiResponse response = new ApiResponse("property_check","property_check completed successfully","success","pass");
    	return new Gson().toJson(response);
    }
	@PostMapping("/all_checks_done")
    public String all_checks_done(@RequestBody String msgstr) {
		ApiResponse response = new ApiResponse("all_checks_done","all_checks_done completed successfully","success","pass");
    	return new Gson().toJson(response);
    }
	@PostMapping("/automatic_decision")
    public String automatic_decision(@RequestBody String msgstr) {
		ApiResponse response = new ApiResponse("automatic_decision","automatic_decision completed successfully","success","pass");
    	return new Gson().toJson(response);
    }
	@PostMapping("/review_result")
    public String review_result(@RequestBody String msgstr) {
		ApiResponse response = new ApiResponse("review_result","review_result completed successfully","success","pass");
    	return new Gson().toJson(response);
    }
	@PostMapping("/notify_customer")
    public String notify_customer(@RequestBody String msgstr) {
		ApiResponse response = new ApiResponse("notify_customer","notify_customer completed successfully","success","success");
    	return new Gson().toJson(response);
    }
	@PostMapping("/notify_customer_reject")
    public String notify_customer_reject(@RequestBody String msgstr) {
		ApiResponse response = new ApiResponse("notify_customer_reject","notify_customer_reject completed successfully","success","success");
    	return new Gson().toJson(response);
    }
}
