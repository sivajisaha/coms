package coms.bpm.service.workflow;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import coms.bpm.model.Activity;
import coms.bpm.model.ApiResponse;
import coms.bpm.model.Event;
import coms.bpm.model.Gateway;
import coms.bpm.model.GatewayResponseMapperElement;
import coms.bpm.model.HumanTask;
import coms.bpm.model.HumanTaskAssignment;
import coms.bpm.model.JsonMessageConversion;
import coms.bpm.model.ProcessContext;
import coms.bpm.model.ProcessDefinition;
import coms.bpm.model.TransactionMessage;
import coms.bpm.model.UserNotes;
import coms.bpm.model.ProcessElement;
import coms.bpm.model.Rule;
import coms.bpm.model.RuleOutcome;
import coms.bpm.model.Workflow;
import reactor.core.publisher.Mono;

@Service
public class WorkflowService {
	@Autowired
	WorkflowDbService wfdbService;
	@Autowired
	JsonMessageConverterService msgconverterService;
	@Autowired
	RuleService ruleService;
	public void processMessage(TransactionMessage message) throws IllegalAccessException, InvocationTargetException, IOException
	{
		if(message.getContext().getProcessInstanceId()==0)
		 {
			 initiateProcess(message);
		 }
		else
		{
			processWorkflowElement(message.getContext(),message.getData());
		}
	}
	@Async
	public void initiateProcess(TransactionMessage message) throws IllegalAccessException, InvocationTargetException, IOException
	{
		 long processinstanceid= wfdbService.insertProcessInstance(message.getContext().getProcessCode(), message.getContext().getVersion());
		 ProcessDefinition processdefinition = wfdbService.getProcessDef(message.getContext().getProcessCode(),message.getContext().getVersion());
	
		 ArrayList<ProcessElement> pelist = processBPMSchema(processdefinition.getDefinition(),message.getContext().getProcessInstanceId(),processdefinition.getId());
		 for (ProcessElement pe : pelist) {
			 //pe.setHandled_message(message.getData());
			 wfdbService.insertProcessElement(pe, processinstanceid);
			}
		 ProcessElement eventnode = pelist.stream().filter(p->p.getElement_type().equals("Event")).findFirst().orElse(null);
		 if(eventnode!=null)
		 {
			 JsonMessageConversion msgconv = new JsonMessageConversion(processdefinition.getId(),message.getData(),"",eventnode.getResponse_processor_schema());
			 //ApiResponse resp = convertMessage(eventnode,msgconv);
			 String convertedmessage = convertMessage(msgconv);
			 eventnode.setProcess_instance_id(processinstanceid);
			 eventnode.setHandled_message(convertedmessage);
			 eventnode.setResponse_message(convertedmessage);
			 //System.out.println("updating db with message --"+ eventnode.getProcess_instance_id()+"--"+ eventnode.getElement_code()+"--"+eventnode.getHandled_message() );
			 wfdbService.updatehandledmessageProcessElement(eventnode);
			 wfdbService.updatresponsemessageProcessElement(eventnode);
			 processWorkflowElement(new ProcessContext(processinstanceid,eventnode.getElement_code(),"TRIGGER","",""),eventnode.getHandled_message());
		 }
		 else
		 {
			 System.out.println("start event not found");
		 }
		 
	}
	@Async
	public void processWorkflowElement(ProcessContext ctx, String data)
	{
		try
		{
			ProcessElement pe = wfdbService.getProcessElement(ctx.getProcessInstanceId(), ctx.getTransactionCode());
			if(pe!=null)
			{
				pe.setHandled_message(data);
				wfdbService.updatehandledmessageProcessElement(pe);
				if(!pe.getStatus().equals("C") && !pe.getStatus().equals("CR"))//process element is still active
				{
					System.out.println("processing element --"+ pe.getProcess_instance_id()+"--"+ pe.getElement_code());
					if(pe.getElement_type().equals("Event"))//initiate event
					{
						ProcessElement nxtpe = wfdbService.getProcessElement(pe.getProcess_instance_id(), pe.getNext_element_codes().get(0));
						if(nxtpe!=null)
						{
							pe.setStatus("C");
							pe.setLast_transaction(LocalDateTime.now());
							wfdbService.updatestatusProcessElement(pe);
							if(pe.getNext_element_codes().size()>0)
							{
								ProcessContext nxtpc = new ProcessContext(nxtpe.getProcess_instance_id(),pe.getNext_element_codes().get(0),"TRIGGER","",ctx.getTransactionComment());
								processWorkflowElement(nxtpc,pe.getHandled_message());
							}
						}
					}
					else if(pe.getElement_type().equals("Activity"))//initiate activity
					{
						
						if(ctx.getTargetactionCode().equals("TRIGGER"))//TRIGGER means initiated within workflow itself 
						{
							if(pe.getHandler_type().equals("AUTOMATED"))
							{
								pe.setRequest_message("");
			
								ApiResponse resp = triggerAutomatedOperation(pe);
								pe.setResponse_message(new Gson().toJson(resp));
								wfdbService.updatresponsemessageProcessElement(pe);

								if(!pe.getResponse_processor_schema().equals(""))
								{
									JsonMessageConversion msgconv = new JsonMessageConversion(pe.getProcess_definition_id(),pe.getResponse_message(),pe.getHandled_message(), pe.getResponse_processor_schema());
									//ApiResponse resp2 = convertMessage(pe,msgconv);
									pe.setHandled_message(convertMessage(msgconv));
								}
								System.out.println("setting handled message for --"+ pe.getElement_code());
								wfdbService.updatehandledmessageProcessElement(pe);
								if(resp.getResponse_status().equals("success"))
								{
									pe.setStatus("C");
									pe.setLast_transaction(LocalDateTime.now());
									wfdbService.updatestatusProcessElement(pe);
									if(pe.getNext_element_codes().size()>0)
									{
										ProcessContext nxtpc = new ProcessContext(pe.getProcess_instance_id(),pe.getNext_element_codes().get(0),"TRIGGER","",ctx.getTransactionComment());
										processWorkflowElement(nxtpc,pe.getHandled_message());
									}
								}
								else
								{
									System.out.println("error in automated task--"+ pe.getHandler_url()+"---"+ resp.getResponse_message());
								}
								
							}
							else if(pe.getHandler_type().equals("HUMAN_TASK"))//start waiting for external agent to complete the activity
							{
								pe.setHandled_message(data);
								wfdbService.updatehandledmessageProcessElement(pe);
								System.out.println("waiting for external intervention--"+ pe.getHandler_url());
								pe.setStatus("A");
								pe.setLast_transaction(LocalDateTime.now());
								wfdbService.updatestatusProcessElement(pe);
							}
						}
						else if(ctx.getTargetactionCode().equals("COMPLETE")||ctx.getTargetactionCode().equals("APPROVE")||ctx.getTargetactionCode().equals("REJECT")||ctx.getTargetactionCode().equals("SAVE"))//COMPLETE or APPROVE or REJECT or Save mean initiated outside workflow  
						{
							ArrayList<UserNotes> notes;
							if(pe.getUser_notes().equals(""))
							{
								notes = new ArrayList<UserNotes>();	
							}
							else
							{
								notes = new Gson().fromJson(pe.getUser_notes(), new TypeToken<ArrayList<UserNotes>>(){}.getType());
							}
							notes.add(new UserNotes(ctx.getTransactionComment(),"",new Date()));
							pe.setUser_notes(new Gson().toJson(notes));
							if(ctx.getTargetactionCode().equals("COMPLETE")||ctx.getTargetactionCode().equals("APPROVE")||ctx.getTargetactionCode().equals("REJECT"))//COMPLETE or APPROVE or REJECT mean initiated outside workflow  
							{
								String status = ctx.getTargetactionCode().equals("REJECT")?"CR":"C";
								pe.setStatus(status);
								pe.setLast_transaction(LocalDateTime.now());
								wfdbService.updatestatusProcessElement(pe);
								String transactionoutcome =  ctx.getTargetactionCode().equals("REJECT")?"fail":"pass";
								pe.setResponse_message(new Gson().toJson(new ApiResponse(ctx.getTransactionCode(),pe.getUser_notes(),"success",transactionoutcome) ));
								wfdbService.updatresponsemessageProcessElement(pe);
								wfdbService.updateUserNotesProcessElement(pe);
								if(!pe.getResponse_processor_schema().equals(""))
								{
									JsonMessageConversion msgconv = new JsonMessageConversion(pe.getProcess_definition_id(),pe.getResponse_message(),pe.getHandled_message(), pe.getResponse_processor_schema());
									pe.setHandled_message(convertMessage(msgconv));
									wfdbService.updatehandledmessageProcessElement(pe);
								}
								if(pe.getNext_element_codes().size()>0)
								{
									ProcessContext nxtpc = new ProcessContext(pe.getProcess_instance_id(),pe.getNext_element_codes().get(0),"TRIGGER","",ctx.getTransactionComment());
									processWorkflowElement(nxtpc,pe.getHandled_message());
								}
							}
							/*else if(ctx.getTargetactionCode().equals("REJECT"))//invoked from outside workflow
							{
								System.out.println("workflow got rejected");
								pe.setStatus("C");
								pe.setLast_transaction(LocalDateTime.now());
								wfdbService.updatestatusProcessElement(pe);
								pe.setResponse_message(new Gson().toJson(new ApiResponse(ctx.getTransactionCode(),ctx.getTransactionComment(),"success","fail") ));
								wfdbService.updatresponsemessageProcessElement(pe);
								wfdbService.updateUserNotesProcessElement(pe);
								wfdbService.updateInstanceStatus(pe.getProcess_instance_id(),"C");
								//wfdbService.abortWorkflow(pe.getProcess_instance_id());	
							}*/
							else if(ctx.getTargetactionCode().equals("SAVE"))
							{
								System.out.println("comment only - no change of state");
								pe.setLast_transaction(LocalDateTime.now());
								wfdbService.updatestatusProcessElement(pe);
								pe.setResponse_message(data);
								wfdbService.updatresponsemessageProcessElement(pe);
								wfdbService.updateUserNotesProcessElement(pe);
							}
							if(!pe.getResponse_processor_schema().equals(""))
							{
								JsonMessageConversion msgconv = new JsonMessageConversion(pe.getProcess_definition_id(),pe.getResponse_message(),pe.getHandled_message(), pe.getResponse_processor_schema());
								//ApiResponse resp2 = convertMessage(pe,msgconv);
								pe.setHandled_message(convertMessage(msgconv));
							}
							wfdbService.updatehandledmessageProcessElement(pe);
						}
					}
					else if(pe.getElement_type().equals("Gateway"))//initiate gateway
					{
						System.out.println("at gateway--"+ pe.getProcess_instance_id()+"--"+ pe.getElement_code());
						boolean prev_step_completion_status = true;
						for(String prevelementcode: pe.getPrevious_element_codes())
						{
							ProcessElement prevpe = wfdbService.getProcessElement(pe.getProcess_instance_id(), prevelementcode);
							if(prevpe!=null)
							{
								System.out.println("previous element--"+ prevpe.getProcess_instance_id()+"--"+ prevelementcode+"--"+ prevpe.getStatus());
								prev_step_completion_status = prevpe.getStatus().equals("C") ||prevpe.getStatus().equals("CR");
								if(!prev_step_completion_status)
									break;
							}
						}
						if(prev_step_completion_status)
						{
							System.out.println("all prev steps complete");

							if(pe.getGateway_type().equals("AND") && !pe.getResponse_processor_schema().equals(""))
							{
								System.out.println("Processing AND Gateway");
								GatewayResponseMapperElement[] mapper = new Gson().fromJson(pe.getResponse_processor_schema(), GatewayResponseMapperElement[].class);
								
								if(mapper!=null)
								{
									for(String prevelementcode: pe.getPrevious_element_codes())
									{
										
										ProcessElement prevpe = wfdbService.getProcessElement(pe.getProcess_instance_id(), prevelementcode);
										GatewayResponseMapperElement mapperschema = new ArrayList<>(Arrays.asList(mapper)).stream().filter(p->p.getInput_element().equals(prevelementcode)).findFirst().orElse(null);
										if(mapperschema!=null)
										{
											System.out.println("AND Gateway: prevelementcode--"+ prevelementcode);
											//System.out.println("AND Gateway: prevpe.getHandled_message()--"+ prevpe.getHandled_message());
											//System.out.println("AND Gateway: pe.getHandled_message()--"+ pe.getHandled_message());
											System.out.println("AND Gateway: mapperschema.getMapper_schema()--"+ mapperschema.getMapper_schema());
											JsonMessageConversion msgconv = new JsonMessageConversion(prevpe.getProcess_definition_id(),prevpe.getHandled_message(),pe.getHandled_message(), mapperschema.getMapper_schema());
											//ApiResponse resp = convertMessage(pe,msgconv);
											//pe.setHandled_message(resp.getResponse_message());
											pe.setHandled_message(convertMessage(msgconv));
											
										}
										else
										{
											System.out.println("Sivaji--mapperschema is null");
										}
									}
								}
							}
							else
							{
								System.out.println("Processing non-AND Gateway");
								pe.setHandled_message(data);
							}
							wfdbService.updatehandledmessageProcessElement(pe);
							for(String nextelementcode: pe.getNext_element_codes())
							{
								ProcessElement nextpe = wfdbService.getProcessElement(pe.getProcess_instance_id(), nextelementcode);
								if(nextpe!=null)
								{
									//System.out.println("next element--"+ nextpe.getProcess_instance_id()+"--"+ nextelementcode);
									ProcessContext nxtpc = new ProcessContext(nextpe.getProcess_instance_id(),nextpe.getElement_code(),"TRIGGER","",ctx.getTransactionComment());
									processWorkflowElement(nxtpc,pe.getHandled_message());
								}
							}
							pe.setStatus("C");
							pe.setLast_transaction(LocalDateTime.now());
							wfdbService.updatestatusProcessElement(pe);
						}
						else
						{
							System.out.println("waiting for all prev steps to complete");
						}
							
					}
					else if(pe.getElement_type().equals("Rule"))//initiate rule
					{
						System.out.println("Got rule, dont know what to do");
						JsonMessageConversion msgconv = new JsonMessageConversion(pe.getProcess_definition_id(),pe.getHandled_message(),"",pe.getRequest_processor_schema());
						String msgconverted = msgconverterService.convert_json_message(msgconv);
						pe.setResponse_message(msgconverted);
						wfdbService.updatresponsemessageProcessElement(pe);
						System.out.println("message input to rule--"+ msgconverted);
						wfdbService.updatehandledmessageProcessElement(pe);
						Boolean ruleoutcome = ruleService.processRule(msgconverted);
						System.out.println("rule outcome--"+ ruleoutcome);
						RuleOutcome[] rulearr = new Gson().fromJson(pe.getRule_outcomes(), RuleOutcome[].class);
						for(RuleOutcome rule : rulearr)
						{
							if((rule.getOutcome().equals("true") && ruleoutcome) ||(rule.getOutcome().equals("false") && !ruleoutcome))
							{
								System.out.println("rule.next_element_code--"+ rule.next_element_code);
								ArrayList<String> rulenextelementlist = new ArrayList<String>(
									      Arrays.asList(rule.next_element_code));
								pe.setOutput_elements(new Gson().toJson(rulenextelementlist));
								pe.setNext_element_codes(rulenextelementlist);
							}
						}
						wfdbService.updateoutputelementProcessElement(pe);
						pe.setStatus("C");
						pe.setLast_transaction(LocalDateTime.now());
						wfdbService.updatestatusProcessElement(pe);
						
						if(pe.getNext_element_codes().size()>0)
						{
							ProcessContext nxtpc = new ProcessContext(pe.getProcess_instance_id(),pe.getNext_element_codes().get(0),"TRIGGER","",ctx.getTransactionComment());
							processWorkflowElement(nxtpc,pe.getHandled_message());
						}
					}
				}
				else
				{
					System.out.println("loop back in progress");
				}
			}
			else
			{
				System.out.println("pe is null--"+ ctx.getProcessInstanceId()+"--"+ctx.getTransactionCode());
				//check if whole instance can be marked as complete
				List<ProcessElement> wfelements =  wfdbService.getAllProcesselements(ctx.getProcessInstanceId());
				ProcessElement lastelement = wfelements.stream()
					      .max(Comparator.comparing(ProcessElement::getSequence))
					      .orElseThrow(NoSuchElementException::new);
				if(lastelement.getStatus().equals("C")||lastelement.getStatus().equals("CR"))
				{
					System.out.println("Completing process instance");
					wfdbService.updateInstanceStatus(ctx.getProcessInstanceId(),"C");
				}
			}
		}
		catch(Exception ex)
		{
			System.out.println("exception--"+ ctx.getProcessInstanceId()+"--"+ctx.getTransactionCode());
			System.out.println("exception details--"+ ex.toString());
			ex.printStackTrace(System.out);
		}
		
	}

	String convertMessage(JsonMessageConversion msgconv) throws IllegalAccessException, InvocationTargetException, IOException
	{
		return msgconverterService.convert_json_message(msgconv);
	}
	ApiResponse triggerAutomatedOperation(ProcessElement pe)
	{
		return invokeWebpostapi(pe.getHandler_url(),pe.getRequest_message(),"",pe.getElement_code(),pe.getProcess_instance_id());
	}
	public ArrayList<HumanTask> getTaskList(Long processdefid)
	{
		ProcessDefinition pdef = wfdbService.getProcessDef2(processdefid);
		List<HumanTaskAssignment> alltaskassignments = wfdbService.getTaskassignments(processdefid);
		Workflow workflow = new Gson().fromJson(pdef.getDefinition(), Workflow.class);
		List<Activity> tasks = workflow.getActivities().stream().filter(p->p.getHandler_type().equals("HUMAN_TASK")).collect(Collectors.toList());
		ArrayList<HumanTask> tasklist = new ArrayList<HumanTask>();
		for(Activity task: tasks)
		{
			List<HumanTaskAssignment> taskassignments = alltaskassignments.stream().filter(p->p.getTask_code().equals(task.getActivity_code())).collect(Collectors.toList());
			tasklist.add(new HumanTask(processdefid,task.getActivity_code(),taskassignments));
		}
		return tasklist;
	}
	public ArrayList<ProcessElement> processBPMSchema(String workflowschema,Long processinstid,Long processdefid) {
		ArrayList<ProcessElement> vobjlist = new ArrayList<ProcessElement>();
		 Workflow workflow = new Gson().fromJson(workflowschema, Workflow.class);
		 
		 Event startevent = workflow.getEvents().stream().filter(p->p.getEvent_code().equals(workflow.getStart_event())).findFirst().orElse(null);
		 ArrayList<String> nxt_element_codes = new ArrayList<>();
		 nxt_element_codes.add(startevent.getNext_element_code());
		 ProcessElement startnode = new ProcessElement(0,0,processinstid,processdefid,startevent.getEvent_code(),startevent.getEvent_title(),"Event","", 0,null,nxt_element_codes,"","", "","", workflow.getProxy_endpoint()+ startevent.getMessage_converter_url(), startevent.getRequest_processor_schema(),startevent.getResponse_processor_schema(),"","A");
		 vobjlist.add(startnode);
		 System.out.println(workflow.getStart_event()+":"+ startevent.getEvent_code());
		 ProcessNode(workflow,vobjlist,startnode);
		 
		 for(ProcessElement el :vobjlist)
		 {
			 if(el.getNext_element_codes()!=null)
			 {
				 if(el.getNext_element_codes().size()>1)
				 {
					 int curryidx = el.getYindex()-(Math.round(el.getNext_element_codes().size()/2));
					 System.out.println("index of current element:"+ el.getYindex());
					 //int curryidx = el.getYindex()-1;//wrong logic
					 System.out.println("setting curryidx:"+ curryidx);
					 for(String nxtelmcode: el.getNext_element_codes())
					 {
						 ProcessElement nxtelm = vobjlist.stream().filter(p->p.getElement_code().equals(nxtelmcode)).findFirst().orElse(null);
						 if(nxtelm!=null)
						 {
							 nxtelm.setYindex(curryidx++);
							 System.out.println("1.setting y index--"+ nxtelm.getElement_code()+"-"+ nxtelm.getElement_type()+"-"+ nxtelm.getGateway_type() +":"+ nxtelm.getYindex());
						 }
					 }
				 }
				 else
				 {
					 ProcessElement nxtelm = vobjlist.stream().filter(p->p.getElement_code().equals(el.getNext_element_codes().get(0))).findFirst().orElse(null);
					 if(nxtelm!=null)
					 {
						 if(nxtelm.getElement_type().equals("Gateway")&&nxtelm.getGateway_type().equals("AND"))
						 {
							 int ysum = 0;
							 System.out.println("gateway's previous elements--"+ nxtelm.getPrevious_element_codes().toString());
							 for(String prvelcode : nxtelm.getPrevious_element_codes())
							 {
								 ProcessElement pelm = vobjlist.stream().filter(p->p.getElement_code().equals(prvelcode)).findFirst().orElse(null);
								 if(pelm !=null)
								 {
									 ysum += pelm.getYindex(); 
								 }
							 }
							 int avgy = ysum/nxtelm.getPrevious_element_codes().size();
							 //System.out.println("y for -"+ nxtelm.getElement_code()+" should actually be--"+ avgy);
							 nxtelm.setYindex(avgy);
						 }
						 else if(nxtelm.getElement_type().equals("Rule"))
						 {
							 nxtelm.setYindex(el.getYindex());
							 System.out.println("rule's next elements--"+ nxtelm.getNext_element_codes().toString());
						 }
						 else
						 {
							 nxtelm.setYindex(el.getYindex());
						 }
						 System.out.println("2.setting y index:"+ nxtelm.getElement_code()+"--"+ nxtelm.getElement_type()+"--"+ nxtelm.getGateway_type() +":"+ nxtelm.getYindex());
					 }
				 }
			 }
		 }
		 int offset = 0 - vobjlist.stream().mapToInt(v->v.getYindex()).min().orElseThrow(NoSuchElementException::new);
		 System.out.println("offset--"+ offset);
		 for(ProcessElement el :vobjlist)
		 {
			 el.setYindex(el.getYindex()+ offset);
		 }
		 //assign status for workflow instance
		 if(processinstid !=0)
		 {
			 List<ProcessElement> elements = wfdbService.getAllProcesselements(processinstid);
			 for(ProcessElement el :vobjlist)
			 {
				 ProcessElement matchelm = elements.stream().filter(p->p.getElement_code().equals(el.getElement_code())).findFirst().orElse(null);
				 if(matchelm!=null)
				 {
					 el.setStatus(matchelm.getStatus());
				 }
			 }
		 }
    	return vobjlist;
	}
	void ProcessNode(Workflow workflow,ArrayList<ProcessElement> vobjlist,ProcessElement prevnode)
	{	
		String proxy_endpoint = workflow.getProxy_endpoint();
		for(String nextitemcode: prevnode.getNext_element_codes())
		{
			if(!nextitemcode.equals(""))
			{
				int sequence = prevnode.getSequence()+1;
				Activity curr_act = workflow.getActivities().stream().filter(p->p.getActivity_code().equals(nextitemcode)).findFirst().orElse(null);
				if(curr_act!=null)
				{
					ArrayList<String> nxt_element_codes = new ArrayList<>();
					nxt_element_codes.add(curr_act.getNext_element_code());
					ProcessElement currnode = new ProcessElement(sequence,0,prevnode.getProcess_instance_id(),prevnode.getProcess_definition_id(), curr_act.getActivity_code(),curr_act.getActivity_title(),"Activity","",sequence,null,nxt_element_codes,"",curr_act.getHandler_type(),curr_act.getHandler_data(),proxy_endpoint + curr_act.getHandler_url(),proxy_endpoint+ curr_act.getMessage_converter_url(),curr_act.getRequest_processor_schema(),curr_act.getResponse_processor_schema(),"","I");
					if(!(vobjlist.stream().filter(o -> o.getElement_code().equals(currnode.getElement_code())).findFirst().isPresent()))
					{
						System.out.println("Activity--"+ curr_act.getActivity_code());
						vobjlist.add(currnode);
					}
					ProcessNode(workflow,vobjlist,currnode);
				}
				else
				{
					Gateway curr_gtwy = workflow.getGateways().stream().filter(p->p.getGateway_code().equals(nextitemcode)).findFirst().orElse(null);
					if(curr_gtwy!=null)
					{
						ProcessElement currnode = new ProcessElement(sequence,0,prevnode.getProcess_instance_id(),prevnode.getProcess_definition_id(), curr_gtwy.getGateway_code(),curr_gtwy.getGateway_title(),"Gateway",curr_gtwy.getGateway_type(), sequence,curr_gtwy.getInput_elements(), curr_gtwy.getOutput_elements(),"","","","",proxy_endpoint + curr_gtwy.getMessage_converter_url(),curr_gtwy.getRequest_processor_schema(),curr_gtwy.getResponse_processor_schema(),"", "I");
						if(!(vobjlist.stream().filter(o -> o.getElement_code().equals(currnode.getElement_code())).findFirst().isPresent()))
						{
							System.out.println("Gateway--"+ curr_gtwy.getGateway_code());
							vobjlist.add(currnode);
						}
						ProcessNode(workflow,vobjlist,currnode);
						
					}
					else
					{
						Rule curr_rule = workflow.getRules().stream().filter(p->p.getRule_code().equals(nextitemcode)).findFirst().orElse(null);
						if(curr_rule !=null)
						{
							ArrayList<String> nxt_element_codes = new ArrayList<>();
							for(RuleOutcome outcome : curr_rule.getRule_outcomes())
							{
								if(outcome.getMovement_type().equals("forward"))
								{
									nxt_element_codes.add(outcome.next_element_code);
								}
								else if(outcome.getMovement_type().equals("forward-negative"))
								{
									nxt_element_codes.add(outcome.next_element_code);
								}
							}
							String strruleoutcomes= new Gson().toJson(curr_rule.getRule_outcomes());
							ProcessElement currnode = new ProcessElement(sequence,0,prevnode.getProcess_instance_id(),prevnode.getProcess_definition_id(),curr_rule.getRule_code(),curr_rule.getRule_title(),"Rule","", sequence,null, nxt_element_codes,strruleoutcomes,"","","",proxy_endpoint + curr_rule.getMessage_converter_url(),curr_rule.getRequest_processor_schema(),curr_rule.getResponse_processor_schema(),"", "I");
							if(!(vobjlist.stream().filter(o -> o.getElement_code().equals(currnode.getElement_code())).findFirst().isPresent()))
							{
								System.out.println("Rule --"+ curr_rule.getRule_code());
								vobjlist.add(currnode);
							}
							ProcessNode(workflow,vobjlist,currnode);
						}
						else
							return;
					}
				}
			}
			else
			{
				return;
			}
		}
	}
	ApiResponse invokeWebpostapi(String url, String requestdata,String authinfo,String pecode,Long pinsid)
	{
		System.out.println("Invoking post api--"+ url+"--"+ requestdata);
		try {
			WebClient webClient = WebClient.create(url);
			String reqdata = requestdata.equals("")?"test_data": requestdata;
			String response = webClient.post()
					.uri("")
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.body(Mono.just(reqdata), String.class)
					.retrieve()
					.bodyToMono(String.class).block();
			System.out.println("Response from post api--"+ response);
			return new Gson().fromJson(response, ApiResponse.class);
		}
		catch(Exception ex)
		{
			System.out.println("post api failed --"+ ex.toString());
			wfdbService.insertFailedTransaction(pecode, pinsid);
			return new ApiResponse(pecode,ex.toString(),"error","fail");
		}
	}
}
