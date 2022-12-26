package coms.bpm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class RuleOutcome {
	public String outcome;
    public String outcome_message;
    public String next_element_code;
    public String movement_type;

}
