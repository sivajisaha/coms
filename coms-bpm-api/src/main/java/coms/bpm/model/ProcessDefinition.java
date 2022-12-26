package coms.bpm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ProcessDefinition {
	private Long id;
	private String code;
	private String title;
	private String version;
	private String description;
	private String definition;
	private String status;
	public ProcessDefinition(String code,String version,String definition,String description,String status)
	{
		this.code = code;
		this.version = version;
		this.definition = definition;
		this.description = description;
		this.status = status;
	}
}
