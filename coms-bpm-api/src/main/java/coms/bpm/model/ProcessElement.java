package coms.bpm.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter @NoArgsConstructor
public class ProcessElement {
	int xindex;
	int yindex;
	Long id;
	Long process_instance_id;
	Long process_definition_id;
	String element_code;
	String element_title;
	String element_type;
	String gateway_type;
	int sequence;
	String input_elements;
	String output_elements;
	ArrayList<String> previous_element_codes;
	ArrayList<String> next_element_codes;
	String rule_outcomes;
	String handler_type;
	String handler_url;
	String  message_converter_url;
	String status;
	String request_processor_schema;
	String response_processor_schema;
	ArrayList<GatewayResponseMapperElement> gateway_response_mapper_scheama_arr;
	String handled_message;
	String request_message;
	String response_message;
	String transaction_log;
	LocalDateTime last_transaction;
	String last_transaction_str;
	String user_notes;
	public ProcessElement(int x,int y,Long pid, Long pdefid, String code,String title, String type,String gtytype, int sequence,ArrayList<String> prev_element_codes, ArrayList<String> next_element_codes,String rule_outcomes_str, String handlertype, String handled_data,String handler_url,String converter_url,String req_proc_schema, String res_proc_schema,String user_notes,String status)
	{
		setXindex(x); 
		setYindex(y);
		setProcess_instance_id(pid);
		setProcess_definition_id(pdefid);
		setElement_code(code);
		setElement_title(title);
		setElement_type(type);
		setGateway_type(gtytype);
		setSequence(sequence);
		setPrevious_element_codes(prev_element_codes);
		setNext_element_codes(next_element_codes);
		setRule_outcomes(rule_outcomes_str);
		setHandler_url(handler_url);
		setMessage_converter_url(converter_url);
		setHandler_type(handlertype);
		setHandled_message(handled_data);
		setRequest_processor_schema(req_proc_schema);
		setResponse_processor_schema(res_proc_schema);
		setUser_notes(user_notes);
		setStatus(status);
	}
	public ProcessElement(String code,String type)
	{
		setElement_code(code);
		setElement_type(type);
	}
	
}
