package coms.bpm.service.workflow;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import coms.bpm.model.FailedTransaction;
import coms.bpm.model.HumanTask;
import coms.bpm.model.HumanTaskAssignment;
import coms.bpm.model.JsonSchemaDefinition;
import coms.bpm.model.ProcessDefinition;
import coms.bpm.model.ProcessElement;
import coms.bpm.model.ProcessInstance;

@Component
public class WorkflowDbService {
	@Autowired
    JdbcTemplate jdbcTemplate;

	public List<ProcessDefinition> getAllProcessDefs() {
	    String sql = "select id,code,definition,description,status,version from public.bpm_process_definition";
	     
	    List<ProcessDefinition> processdefs = jdbcTemplate.query(sql,
	                BeanPropertyRowMapper.newInstance(ProcessDefinition.class));
	    return processdefs;
	}
	public ProcessDefinition getProcessDef(String code,String version) {
	    String sql = "select id, code, definition,version, description,status from public.bpm_process_definition where code=? and version=? ";   
	    return jdbcTemplate.queryForObject(sql,BeanPropertyRowMapper.newInstance(ProcessDefinition.class),new Object[]{code,version});
	}
	public ProcessDefinition getProcessDef2(Long processdefid) {
	    String sql = "select id, code, definition,version, description,status from public.bpm_process_definition where id=?";   
	    return jdbcTemplate.queryForObject(sql,BeanPropertyRowMapper.newInstance(ProcessDefinition.class),new Object[]{processdefid});
	}
	public ProcessDefinition getProcessDef3(Long processInstanceid) {
	    String sql = "select a.definition from public.bpm_process_definition a,public.bpm_process_instance b where a.id=b.process_definition_id and b.id =?";   
	    return jdbcTemplate.queryForObject(sql,BeanPropertyRowMapper.newInstance(ProcessDefinition.class),new Object[]{processInstanceid});
	}
	public List<ProcessInstance> getAllProcessinstances(Long processdefinitionid) {
	    String sql = "select id,process_definition_id,status from public.bpm_process_instance where process_definition_id=?";
	     
	    List<ProcessInstance> pinstances = jdbcTemplate.query(sql,
	                BeanPropertyRowMapper.newInstance(ProcessInstance.class),new Object[]{processdefinitionid});
	    return pinstances;
	}
	public List<ProcessInstance> getAllProcessinstancesByCode(String code,String version) {
	    String sql = "select id,process_definition_id,status from public.bpm_process_instance where process_definition_id="
	    		+ "(select id from public.bpm_process_definition where code=? and version=?)";
	     
	    List<ProcessInstance> pinstances = jdbcTemplate.query(sql,
	                BeanPropertyRowMapper.newInstance(ProcessInstance.class),new Object[]{code,version});
	    return pinstances;
	}
	public List<ProcessElement> getAllProcesselements(Long processinstanceid) {
	    String sql = "select id,process_instance_id,element_code,element_type,gateway_type,handler_type,sequence,handled_message,handler_url,"
	    		+ "input_elements,output_elements,request_processor_schema,response_processor_schema,"
	    		+ "CASE WHEN status = 'I' THEN 'Not started' WHEN status = 'C' THEN 'Completed' WHEN status = 'A' THEN 'In progress' WHEN status = 'CR' THEN 'Rejected' WHEN status = 'H' THEN 'On hold' END status,"
	    		+ "request_message,response_message,TO_CHAR(last_transaction,'MON-DD-YYYY HH12: MIPM') last_transaction_str,user_notes, last_transaction,transaction_log"
	    		+ " from public.bpm_process_instance_element"
	    		+ " where process_instance_id =? order by sequence";
	     
	    List<ProcessElement> pelems = jdbcTemplate.query(sql,
	                BeanPropertyRowMapper.newInstance(ProcessElement.class),new Object[]{processinstanceid});
	    return pelems;
	}
	
	public int addProcessDefs(ProcessDefinition processdef) {
	     
	    return jdbcTemplate.update(
	    	      "insert into public.bpm_process_definition(code,definition,description,status,version) VALUES (?, ?, ?, ?,?)", 
	    	      processdef.getCode(), processdef.getDefinition(), processdef.getDescription(), processdef.getStatus(),processdef.getVersion());
	}
	public int editProcessDefs(ProcessDefinition processdef) {
	     
	    return jdbcTemplate.update(
	    	      "update public.bpm_process_definition set code=? ,version=?, definition=?, description=? where id=?", 
	    	      processdef.getCode(), processdef.getVersion(), processdef.getDefinition(), processdef.getDescription(), processdef.getId());
	}
	public int insertProcessInstance(String process_code,String process_version) {    
	    KeyHolder keyHolder = new GeneratedKeyHolder();
	    String INSERT_MESSAGE_SQL 
	    = "insert into public.bpm_process_instance(process_definition_id,status) "
	    		+ "values((select id from bpm_process_definition where code =? and version=?),'ACTIVE')";
	    
	  try
	  {
	    jdbcTemplate.update(connection -> {
	        PreparedStatement ps = connection
	          .prepareStatement(INSERT_MESSAGE_SQL,Statement.RETURN_GENERATED_KEYS);
	          ps.setString(1, process_code);
	          ps.setString(2, process_version);
	          return ps;
	        }, keyHolder);
	        return Integer.parseInt(keyHolder.getKeys().get("id").toString());
	    }
	  catch(Exception e)
	  {
		  System.out.println("Exception--"+ e.toString());
		 return 0; 
	  }
	}
	public ProcessElement getProcessElement(Long processinstanceid,String elementcode) {
	    String sql = "select a.id, a.process_instance_id,a.element_code,b.process_definition_id, a.element_type,a.gateway_type,a.sequence,a.handler_type,a.handled_message,a.handler_url,a.message_converter_url,a.input_elements,a.output_elements,a.rule_outcomes,a.request_processor_schema,"
	    		+ "a.response_processor_schema,a.status,a.request_message,a.response_message,a.user_notes,a.transaction_log "
	    		+ "from public.bpm_process_instance_element a,public.bpm_process_instance b where a.process_instance_id = b.id and a.process_instance_id = ? and a.element_code = ?"; 
	    if(!elementcode.isEmpty())
	    {
	    	ProcessElement pe= jdbcTemplate.queryForObject(sql,BeanPropertyRowMapper.newInstance(ProcessElement.class),new Object[]{processinstanceid,elementcode});
		    if(pe!=null)
		    {
		    	if(!pe.getInput_elements().isEmpty())
			    {
			    	ArrayList<String> prev_element_codes = new ArrayList<String>();
			   
			    	String[] arr = new Gson().fromJson(pe.getInput_elements(), String[].class);
			    	for(String item: arr)
			    	{
			    		prev_element_codes.add(item);
			    	}
			    	pe.setPrevious_element_codes(prev_element_codes);
			    }
			    if(!pe.getOutput_elements().isEmpty())
			    {
			    	ArrayList<String> nxt_element_codes = new ArrayList<String>();
			   
			    	String[] arr = new Gson().fromJson(pe.getOutput_elements(), String[].class);
			    	for(String item: arr)
			    	{
			    		nxt_element_codes.add(item);
			    	}
			    	pe.setNext_element_codes(nxt_element_codes);
			    }
			   
			    return pe;
		    }
		    else
		    	return null;
	    }
	    else
	    	return null;
	    
	}
	public ProcessElement getProcesselement(Long instanceelementid) {
		 String sql = "select id, process_instance_id,element_code,element_type,gateway_type,sequence,handler_type,handled_message,handler_url,input_elements,output_elements,rule_outcomes,status,request_message,response_message,transaction_log,TO_CHAR(last_transaction,'MON-DD-YYYY HH12: MIPM') last_transaction_str,last_transaction from public.bpm_process_instance_element where id = ?"; 

	    	ProcessElement pe= jdbcTemplate.queryForObject(sql,BeanPropertyRowMapper.newInstance(ProcessElement.class),new Object[]{instanceelementid});
		    if(pe!=null)
		    {
		    	if(!pe.getInput_elements().isEmpty())
			    {
			    	ArrayList<String> prev_element_codes = new ArrayList<String>();
			   
			    	String[] arr = new Gson().fromJson(pe.getInput_elements(), String[].class);
			    	for(String item: arr)
			    	{
			    		prev_element_codes.add(item);
			    	}
			    	pe.setPrevious_element_codes(prev_element_codes);
			    }
			    if(!pe.getOutput_elements().isEmpty())
			    {
			    	ArrayList<String> nxt_element_codes = new ArrayList<String>();
			   
			    	String[] arr = new Gson().fromJson(pe.getOutput_elements(), String[].class);
			    	for(String item: arr)
			    	{
			    		nxt_element_codes.add(item);
			    	}
			    	pe.setNext_element_codes(nxt_element_codes);
			    }
			   
			    return pe;
		    }
		    else
		    	return null;
			  
	}
	public String getTransactionLog(Long instanceelementid) {
		String sql = "select transaction_log from public.bpm_process_instance_element where id = ?"; 
	    return jdbcTemplate.queryForObject(sql,BeanPropertyRowMapper.newInstance(String.class),new Object[]{instanceelementid});   
	}
	public int updatestatusProcessElement(ProcessElement pe)
	{
		String sql = "update public.bpm_process_instance_element set status=? , last_transaction=? where process_instance_id = ? and element_code = ?";
		return jdbcTemplate.update(sql, pe.getStatus(), pe.getLast_transaction(), pe.getProcess_instance_id(),pe.getElement_code());
	}
	public int updatrequestmessageProcessElement(ProcessElement pe)
	{
		String sql = "update public.bpm_process_instance_element set request_message =? where process_instance_id = ? and element_code = ?";
		return jdbcTemplate.update(sql, pe.getRequest_message(), pe.getProcess_instance_id(),pe.getElement_code());
	}
	public int updatresponsemessageProcessElement(ProcessElement pe)
	{
		String sql = "update public.bpm_process_instance_element set response_message =? where process_instance_id = ? and element_code = ?";
		return jdbcTemplate.update(sql, pe.getResponse_message(), pe.getProcess_instance_id(),pe.getElement_code());
	}
	public int updatehandledmessageProcessElement(ProcessElement pe)
	{
		String sql = "update public.bpm_process_instance_element set handled_message =? where process_instance_id = ? and element_code = ?";
		return jdbcTemplate.update(sql, pe.getHandled_message(), pe.getProcess_instance_id(),pe.getElement_code());
	}
	public int updatetransactionlogProcessElement(ProcessElement pe)
	{
		String sql = "update public.bpm_process_instance_element set transaction_log =? where process_instance_id = ? and element_code = ?";
		return jdbcTemplate.update(sql, pe.getTransaction_log(), pe.getProcess_instance_id(),pe.getElement_code());
	}
	public int updateoutputelementProcessElement(ProcessElement pe)
	{
		String sql = "update public.bpm_process_instance_element set output_elements =? where process_instance_id = ? and element_code = ?";
		return jdbcTemplate.update(sql, pe.getOutput_elements(), pe.getProcess_instance_id(),pe.getElement_code());
	}
	public int updateUserNotesProcessElement(ProcessElement pe)
	{
		String sql = "update public.bpm_process_instance_element set user_notes=? where id = ?";
		return jdbcTemplate.update(sql,pe.getUser_notes(), pe.getProcess_instance_id());
	}
	public int insertProcessElement(ProcessElement pe, long processinstanceid)
	{
		String inputelements = pe.getElement_type().equals("Gateway")?new Gson().toJson(pe.getPrevious_element_codes()):"";
		return jdbcTemplate.update(
	    	      "INSERT INTO bpm_process_instance_element(process_instance_id,element_code,element_title,element_type,gateway_type,sequence,"
	    	      + "handler_type,handler_url,message_converter_url,handled_message,input_elements,output_elements,rule_outcomes,"
	    	      + "status,request_processor_schema,response_processor_schema,request_message,response_message,user_notes,transaction_log) "
	    	      + "VALUES (?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", 
	    	      processinstanceid,pe.getElement_code(),pe.getElement_title(),pe.getElement_type(),pe.getGateway_type(),pe.getSequence(),
	    	      pe.getHandler_type(), pe.getHandler_url(),pe.getMessage_converter_url(), pe.getHandled_message(),
	    	      inputelements,new Gson().toJson(pe.getNext_element_codes()),pe.getRule_outcomes(),
	    	      pe.getStatus(),pe.getRequest_processor_schema(),pe.getResponse_processor_schema(),pe.getRequest_message(),
	    	      pe.getResponse_message(), pe.getUser_notes(),pe.getTransaction_log());
	}
	public int updateInstanceStatus(Long instanceid, String status)
	{
		String sql = "update public.bpm_process_instance set status=? where id = ?";
		return jdbcTemplate.update(sql,status, instanceid);
	}
	
	public List<FailedTransaction> getAllFailedTransactions() {
	    String sql = "select id,process_instance_id,element_code from public.bpm_process_failed_instance_element";
	     
	    List<FailedTransaction> fltns = jdbcTemplate.query(sql,
	                BeanPropertyRowMapper.newInstance(FailedTransaction.class));
	    return fltns;
	}
	public int insertFailedTransaction(String elementcode, long processinstanceid)
	{
		return jdbcTemplate.update(
	    	      "INSERT INTO bpm_process_failed_instance_element(process_instance_id,element_code) "
	    	      + "VALUES (?, ?)", 
	    	      processinstanceid,elementcode);
	}
	public int deleteFailedTransaction(long transactionid)
	{
		return jdbcTemplate.update(
	    	      "delete from bpm_process_failed_instance_element where id=? ", 
	    	      transactionid);
	}
	public int abortWorkflow(long processinstanceid)
	{
		return jdbcTemplate.update(
	    	      "delete from bpm_process_instance_element where status = 'I' and process_instance_id=? ", 
	    	      processinstanceid);
	}
	public List<JsonSchemaDefinition> getAllJsonSchemaDefs() {
	    String sql = "select id,process_definition_id,schema_name,schema_category,schema_definition from public.bpm_process_json_repo";
	    List<JsonSchemaDefinition> schemadefs = jdbcTemplate.query(sql,
	                BeanPropertyRowMapper.newInstance(JsonSchemaDefinition.class));
	    return schemadefs;
	}
	public int addJsonSchemaDef(JsonSchemaDefinition schemadef) {   
	    return jdbcTemplate.update(
	    	      "insert into public.bpm_process_json_repo(process_definition_id,schema_name,schema_category,schema_definition) VALUES (?, ?, ?, ?)", 
	    	      schemadef.getProcess_definition_id(), schemadef.getSchema_name(), schemadef.getSchema_category(), schemadef.getSchema_definition());
	}
	public int editJsonSchemaDef(JsonSchemaDefinition schemadef) {
	     
	    return jdbcTemplate.update(
	    	      "update public.bpm_process_json_repo set process_definition_id=? ,schema_name =?,schema_category=?, schema_definition=? where id=?", 
	    	      schemadef.getProcess_definition_id(), schemadef.getSchema_name(), schemadef.getSchema_category(), schemadef.getSchema_definition(), schemadef.getId());
	}
	public JsonSchemaDefinition getJsonSchemaDef(Long process_definition_id,String schema_name) {
	    String sql = "select id,process_definition_id,schema_name,schema_category,schema_definition from public.bpm_process_json_repo where process_definition_id=? and schema_name=? ";   
	    return jdbcTemplate.queryForObject(sql,BeanPropertyRowMapper.newInstance(JsonSchemaDefinition.class),new Object[]{process_definition_id,schema_name});
	}
	public JsonSchemaDefinition getJsonSchemaDef2(Long id) {
	    String sql = "select id,process_definition_id,schema_name,schema_category,schema_definition from public.bpm_process_json_repo where id=?";   
	    return jdbcTemplate.queryForObject(sql,BeanPropertyRowMapper.newInstance(JsonSchemaDefinition.class),new Object[]{id});
	}
	public List<HumanTaskAssignment> getTaskassignments(Long processdefinitionid) {
	    String sql = "select id, task_code,task_assignment_entity_id,task_assignment_entity_type from public.bpm_process_task_assignment where process_definition_id =?";
	    List<HumanTaskAssignment> taskassignments = jdbcTemplate.query(sql,
	                BeanPropertyRowMapper.newInstance(HumanTaskAssignment.class),new Object[]{processdefinitionid});
	    return taskassignments;
	}
	public List<HumanTask> getAssignedActiveTasks(Long userid)
	{
		String sql = "select distinct a.id as process_task_id,a.process_instance_id,a.element_code as activity_code,e.process_definition_id "
				+ "from public.bpm_process_instance_element a ,"
				+ "public.bpm_process_task_assignment b,"
				+ "public.user_group_mapping c, public.users d,public.bpm_process_instance e "
				+ "where a.element_type ='Activity' and a.handler_type='HUMAN_TASK' and a.status='C' "
				+ "and a.element_code = b.task_code and a.process_instance_id = e.id "
				+ "and ((b.task_assignment_entity_type='U' and b.task_assignment_entity_id=?) or  "
				+ "(b.task_assignment_entity_type='G' and b.task_assignment_entity_id =c.group_id and c.user_id= d.user_id and c.user_id=? ))";
		
		List<HumanTask> tasks = jdbcTemplate.query(sql,
	                BeanPropertyRowMapper.newInstance(HumanTask.class),new Object[]{userid,userid});
	    return tasks;
	}
}