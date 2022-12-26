package coms.bpm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import coms.bpm.model.TransactionMessage;
import coms.bpm.model.ProcessElement;
import coms.bpm.service.messaging.jms.MessageService;
import coms.bpm.service.messaging.kafka.KafkaService;
import coms.bpm.service.workflow.WorkflowDbService;
import coms.bpm.service.workflow.WorkflowService;
@RestController
public class WorkflowController {
	@Autowired
	public MessageService msgService;
	@Autowired
	KafkaService kafkaService;
	@Autowired
	public WorkflowService workflowService;
	@Autowired
	WorkflowDbService wfdbService;
	@PostMapping("/processbpmschema")
    public List<ProcessElement> processBPMSchema(@RequestHeader(value="logged_userid")String userid,@RequestBody String workflowschema) {
		return workflowService.processBPMSchema(workflowschema,(long) 0,(long) 0);
    }
	@GetMapping("/workflow/schema/{processCode}/{version}")
    public List<ProcessElement> processBPMSchema2(@RequestHeader(value="logged_userid")String userid,@PathVariable String processCode, @PathVariable String version) {
		return workflowService.processBPMSchema(wfdbService.getProcessDef(processCode, version).getDefinition(),(long) 0,(long) 0);
    }
	@PostMapping("/postmessage")
    public String postMessage(@RequestBody TransactionMessage message) {
		//msgService.sendMessage(message);
		kafkaService.send(message);
		return "message posted successfully";
    }
	@PostMapping("/message/kafka/post")
    public String postMessageKafka(@RequestBody TransactionMessage message) {
		kafkaService.send(message);
		return "Message sent to the Kafka Topic java_in_use_topic Successfully";
    }
}
