package coms.bpm.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;

import coms.bpm.model.ApiResponse;
import coms.bpm.model.FailedTransaction;
import coms.bpm.model.HumanTask;
import coms.bpm.model.JsonSchemaDefinition;
import coms.bpm.model.ProcessDefinition;
import coms.bpm.model.ProcessElement;
import coms.bpm.model.ProcessInstance;
import coms.bpm.service.workflow.RuleService;
import coms.bpm.service.workflow.WorkflowDbService;
import coms.bpm.service.workflow.WorkflowService;

import io.swagger.v3.oas.annotations.Operation;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/process")
public class ProcessController {
	@Autowired
	public WorkflowService workflowService;
	@Autowired
	WorkflowDbService wfdbService;
	@Autowired
	RuleService ruleService;
	@GetMapping("/processdef/all")
	@Operation(summary="Get all process definition")
	public Iterable<ProcessDefinition> getProcessDefs(@RequestHeader(value="logged_userid")String userid) {
		
		return wfdbService.getAllProcessDefs();
	}
	@PostMapping("/processdef/add")
	@Operation(summary="Create a new process definition object")
	public String createProcessDef(@RequestHeader(value="logged_userid")String userid,@RequestBody ProcessDefinition pdef) {
		//ProcessDefinition p = new ProcessDefinition(processCode,version,def,"","");
		int result = wfdbService.addProcessDefs(pdef);
		System.out.println("createProcessDef--"+ result);
		ApiResponse response = new ApiResponse("create_process_definition",result==1?"process definition added successfully":"failed to add process definition",result==1?"success":"failure",result==1?"success":"failure");
    	return new Gson().toJson(response);
	}
	@PostMapping("/processdef/edit")
	@Operation(summary="Edits existing process definition object")
	public String editProcessDef(@RequestHeader(value="logged_userid")String userid,@RequestBody ProcessDefinition def) {
		int result = wfdbService.editProcessDefs(def);
		ApiResponse response = new ApiResponse("edit_process_definition",result==1?"process defintion edited successfully":"failed to edit json schema definition",result==1?"success":"failure",result==1?"success":"failure");
    	return new Gson().toJson(response);
		//return "process defintion edited successfully";
	}
	@GetMapping("/processdef/{processCode}/{version}")
	@Operation(summary="Get a process definition by its code and version")
	public ProcessDefinition getProcessDef(@RequestHeader(value="logged_userid")String userid,@PathVariable String processCode, @PathVariable String version) {
		return wfdbService.getProcessDef(processCode, version);
	}
	@GetMapping("/taskdef/{processDefinitionid}")
	@Operation(summary="Get list of tasks by its code and version")
	ArrayList<HumanTask> getTaskList(@RequestHeader(value="logged_userid")String userid,@PathVariable Long processDefinitionid)
	{
		return workflowService.getTaskList(processDefinitionid);
	}
	@GetMapping("/processdef/{processDefinitionid}")
	@Operation(summary="Get a process definition by its id")
	public ProcessDefinition getProcessDef2(@RequestHeader(value="logged_userid")String userid,@PathVariable Long processDefinitionid) {
		return wfdbService.getProcessDef2(processDefinitionid);
	}
	@GetMapping("/instance/{processDefinitionid}")
	@Operation(summary="Get all process instances by definition id")
	public Iterable<ProcessInstance> getProcessInstance(@RequestHeader(value="logged_userid")String userid,@PathVariable Long processDefinitionid) {
		return wfdbService.getAllProcessinstances(processDefinitionid);
	}
	
	@GetMapping("/instance/2/{processCode}/{version}")
	@Operation(summary="Get all process instances by process code and version")
	public Iterable<ProcessInstance> getProcessInstanceByCode(@RequestHeader(value="logged_userid")String userid,@PathVariable String processCode, @PathVariable String version) {
		return wfdbService.getAllProcessinstancesByCode(processCode,version);
	}
	@GetMapping("/instance/elements/{processInstanceid}")
	@Operation(summary="Get all process steps by instance id")
	public Iterable<ProcessElement> getProcessInstanceElements(@RequestHeader(value="logged_userid")String userid,@PathVariable Long processInstanceid) {
		return wfdbService.getAllProcesselements(processInstanceid);
	}
	@GetMapping("/instance/element/{processInstanceid}/{elementCode}")
	@Operation(summary="Get a process instance element by process instance id and element code")
	public ProcessElement getProcessInstance(@RequestHeader(value="logged_userid")String userid,@PathVariable Long processInstanceid, @PathVariable String elementCode) {
		return wfdbService.getProcessElement(processInstanceid, elementCode);
	}
	@GetMapping("/instance/element/2/{processinstanceElementid}")
	@Operation(summary="Get specific process instance element by instance element id")
	public ProcessElement getProcessInstanceElement(@RequestHeader(value="logged_userid")String userid,@PathVariable Long processinstanceElementid) {
		return wfdbService.getProcesselement(processinstanceElementid);
	}
	@GetMapping("/instance/elements/visualization/{processInstanceid}")
    public List<ProcessElement> getProcessVisualization(@RequestHeader(value="logged_userid")String userid,@PathVariable Long processInstanceid) {
		return workflowService.processBPMSchema(wfdbService.getProcessDef3(processInstanceid).getDefinition(),processInstanceid,(long) 0);
    }
	@GetMapping("/definition/elements/visualization/byid/{processDefinitionid}")
    public List<ProcessElement> getProcessVisualizationfromProcessDefinitionId(@RequestHeader(value="logged_userid")String userid,@PathVariable Long processDefinitionid) {
		return workflowService.processBPMSchema(wfdbService.getProcessDef2(processDefinitionid).getDefinition(),(long) 0,(long) 0);
    }
	@PostMapping("/definition/elements/visualization/byschema")
    public List<ProcessElement> getProcessVisualizationfromSchema(@RequestHeader(value="logged_userid")String userid,@RequestBody String workflowSchema) {
		return workflowService.processBPMSchema(workflowSchema,(long) 0,(long) 0);
    }
	@GetMapping("/instance/element/failed")
	@Operation(summary="Get failed transactions")
	public Iterable<FailedTransaction> getFailedTransactions() {
		return wfdbService.getAllFailedTransactions();
	}
	@GetMapping("/instance/element/failed/delete/{transactionid}")
	@Operation(summary="Deleted a failed transaction")
	public int deleteFailedTransaction(@PathVariable Long transactionid) {
		return wfdbService.deleteFailedTransaction(transactionid);
	}
	@PostMapping("/rule/process")
	@Operation(summary="Basic function for rule processing")
	public Boolean processRule(@RequestHeader(value="logged_userid")String userid,@RequestBody String jsonrulestr) throws JsonMappingException, JsonProcessingException {
		return ruleService.processRule(jsonrulestr);
	}
	@GetMapping("/jsonschemadef/all")
	@Operation(summary="Get all json schema definitions")
	public Iterable<JsonSchemaDefinition> getJsonSchemaDefs(@RequestHeader(value="logged_userid")String userid) {
		return wfdbService.getAllJsonSchemaDefs();
	}
	@PostMapping("/jsonschemadef/add")
	@Operation(summary="Create a new json schema definition object")
	public String createJsonSchemaDef(@RequestHeader(value="logged_userid")String userid,@RequestBody JsonSchemaDefinition jsondef) {
		int result = wfdbService.addJsonSchemaDef(jsondef);
		ApiResponse response = new ApiResponse("create_process_definition",result==1?"json schema defintion added successfully":"failed to add json schema definition",result==1?"success":"failure",result==1?"success":"failure");
    	return new Gson().toJson(response);
	}
	@PostMapping("/jsonschemadef/edit")
	@Operation(summary="Edits existing json schema definition object")
	public String editJsonSchemaDef(@RequestHeader(value="logged_userid")String userid,@RequestBody JsonSchemaDefinition jsondef) {
		int result = wfdbService.editJsonSchemaDef(jsondef);
		ApiResponse response = new ApiResponse("edit_json_schema_definition",result==1?"json schema defintion edited successfully":"failed to edit json schema definition",result==1?"success":"failure",result==1?"success":"failure");
    	return new Gson().toJson(response);
	}
	@GetMapping("/jsonschemadef/{process_def_id}/{version}")
	@Operation(summary="Get a json schema by its process_id and schama name")
	public JsonSchemaDefinition getProcessDef(@RequestHeader(value="logged_userid")String userid,@PathVariable long process_definition_id, @PathVariable String schema_name) {
		return wfdbService.getJsonSchemaDef(process_definition_id, schema_name);
	}
	@GetMapping("/jsonschemadef/{id}")
	@Operation(summary="Get a json schema by its process_id and schama name")
	public JsonSchemaDefinition getProcessDef2(@RequestHeader(value="logged_userid")String userid,@PathVariable long id) {
		return wfdbService.getJsonSchemaDef2(id);
	}
	@GetMapping("/task/active/{userlid}")
	@Operation(summary="Get assigned active tasks")
	public Iterable<HumanTask> getJsonSchemaDefs(@RequestHeader(value="logged_userid")String userid,@PathVariable long userlid) {
		return wfdbService.getAssignedActiveTasks(userlid);
	}
}
