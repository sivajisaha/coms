package coms.bpm.model;
import java.util.ArrayList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter @NoArgsConstructor
public class Workflow {
	  private String process_code;
	  private String start_event;
	  private String proxy_endpoint;
	  ArrayList<Activity> activities = new ArrayList<Activity>();
	  ArrayList<Event> events = new ArrayList<Event>();
	  ArrayList<Gateway> gateways = new ArrayList<Gateway>();
	  public ArrayList<Rule> rules= new ArrayList<Rule>();
	}
	
