package coms.bpm.model;

import java.util.ArrayList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter @NoArgsConstructor
public class Activity {
	private int process_instance_id;
	private String activity_code;
	private String activity_title;
	private String handler_type;
	private String handler_url;
	private String  message_converter_url;
	private String handler_data;
	private String request_processor_schema;
	private String response_processor_schema;
	private ArrayList<String> assignment = new ArrayList<String>();
	private String next_element_code;
    private String status;
	 
}
