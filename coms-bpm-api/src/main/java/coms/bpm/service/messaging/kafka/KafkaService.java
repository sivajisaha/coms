package coms.bpm.service.messaging.kafka;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import coms.bpm.model.TransactionMessage;
import coms.bpm.service.workflow.WorkflowService;

@Service
public class KafkaService {
	@Autowired
	private KafkaTemplate<String, TransactionMessage> kafkaTemplate;
	@Autowired
	WorkflowService workflowService;
	String kafkaTopic = "coms-bpm-topic";
	
	public void send(TransactionMessage message) {
	    
	    kafkaTemplate.send(kafkaTopic, message);
	}
	@KafkaListener(topics = "coms-bpm-topic", groupId = "group-id",containerFactory="kafkaListenerContainerFactory")
	public void listen(TransactionMessage message) throws IllegalAccessException, InvocationTargetException, IOException {
	   System.out.println("Received Messasge in group - group-id: " + message);
	   workflowService.processMessage(message); 
	}

}
