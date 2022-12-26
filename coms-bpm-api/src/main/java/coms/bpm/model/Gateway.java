package coms.bpm.model;

import java.util.ArrayList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter @NoArgsConstructor
public class Gateway {
	 String gateway_code;
	 String gateway_title;
	 ArrayList<String> input_elements = new ArrayList<String>();
	 ArrayList<String> output_elements = new ArrayList<String>();
	 String gateway_type;
	 String  message_converter_url;
	 String request_processor_schema;
	 String response_processor_schema;
	 
}
