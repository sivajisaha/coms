package coms.bpm.model;

import java.util.ArrayList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class Rule {
	 public String rule_code;
	 public String rule_title;
	 public ArrayList<RuleOutcome> rule_outcomes;
	 private String  message_converter_url;
	 String request_processor_schema;
	 String response_processor_schema;
}
