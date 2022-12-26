package coms.bpm.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;

import coms.bpm.scheduler.model.FailedTransaction;
import coms.bpm.scheduler.model.ProcessContext;
import coms.bpm.scheduler.model.TransactionMessage;
import reactor.core.publisher.Mono;

@Component
public class ScheduledTasks {
	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	@Value("${coms.bpmservice.uri}")
	private String bpmserviceUri;
	/*@Scheduled(fixedRate = 5000)
	public void reportCurrentTime() {
		log.info("The time is now {}", dateFormat.format(new Date()));
	}*/
	@Scheduled(fixedRate = 10000)
	public void pollFailedBpmTransactionQueuee() {
		WebClient webClient = WebClient.create(bpmserviceUri);
		FailedTransaction[] translist = webClient.get()
				.uri("/process/instance/element/failed")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.retrieve()
				.bodyToMono(FailedTransaction[].class).block();
		
		for(FailedTransaction trans: translist)
		{
			TransactionMessage msg = new TransactionMessage();
			ProcessContext pc = new ProcessContext();
			pc.setTransactionCode(trans.getElement_code());
			pc.setProcessInstanceId(trans.getProcess_instance_id());
			pc.setTargetactionCode("TRIGGER");
			msg.setContext(pc);
			String response = webClient.post()
					.uri("/postmessage")
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.body(Mono.just(new Gson().toJson(msg)), String.class)
					.retrieve()
					.bodyToMono(String.class).block();
			log.info("response from posting message: {}", response);
			response =  webClient.get()
					.uri("/process/instance/element/failed/delete/"+ trans.getId())
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.retrieve()
					.bodyToMono(String.class).block();
			log.info("response from deleting transaction: {}", response);
		}
		log.info("Response from failed transaction queue: {}", translist.length);
	}
}
