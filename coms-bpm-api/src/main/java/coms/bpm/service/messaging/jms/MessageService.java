package coms.bpm.service.messaging.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import coms.bpm.model.TransactionMessage;
import coms.bpm.service.workflow.WorkflowService;

@Component
public class MessageService {
	@Value( "${INPUT_QUEUE}" )
	private String inputQueue;
    @Autowired
    private JmsTemplate jmsTemplate;
    
    @Autowired
    WorkflowService workflowService;
    public void sendMessage(TransactionMessage message)
    {
    	jmsTemplate.convertAndSend(inputQueue, message);
    }
	@JmsListener(destination = "${INPUT_QUEUE}", concurrency = "3-5")
	public void receiveMessage(TransactionMessage message)
	{
		System.out.println("Received " + message );	
		//workflowService.processMessage(message); 
	}
}