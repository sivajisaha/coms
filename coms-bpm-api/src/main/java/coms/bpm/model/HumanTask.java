package coms.bpm.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class HumanTask {
	Long process_task_id;
	Long process_definition_id;
	Long process_instance_id;
	String activity_code;
	List<HumanTaskAssignment> assignments;
	public HumanTask(Long pdefid,String actcode,List<HumanTaskAssignment> assignments)
	{
		setProcess_definition_id(pdefid);
		setActivity_code(actcode);
		setAssignments(assignments);
	}
}
