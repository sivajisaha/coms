package coms.bpm.model;

import java.util.ArrayList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter @NoArgsConstructor
public class Event {
	 private String event_code;
	 private String event_title;
	  private String handler_type;
	  private String  message_converter_url;
	  private String request_processor_schema;
      private String response_processor_schema;
	  private ArrayList<Object> assignment = new ArrayList<Object>();
	  private String next_element_code;

}
