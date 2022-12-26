package coms.block.ui.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter @NoArgsConstructor
public class CustomUser {
	private long user_id;
	private String first_name;
	private String middle_name;
	private String last_name;
	private String email;
	private String login_id;
	private String user_password;
	private List<String> roles;
}
