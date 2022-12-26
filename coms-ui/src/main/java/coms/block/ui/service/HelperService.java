package coms.block.ui.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import coms.block.ui.model.CustomUser;
import coms.block.ui.model.KeyValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Component
@Getter @Setter 
public class HelperService {
	@Autowired
	DbService dbService;
	//@Value("${coms.userservice.uri}")
	//private String userserviceUri;
	 public CustomUser GetUserByLoginid(String loginid)
	 {
		 String userserviceUri = dbService.getConfigUris().get("COMS-USER-API");
		 WebClient client = WebClient.create(userserviceUri); 
		  CustomUser user = client.get()
				  .uri(uriBuilder -> uriBuilder
					.path("/getuserbyloginid/{loginid}")
					.build(loginid))
				  .retrieve()
				  .bodyToMono(CustomUser.class).block();
		  return user;
	 }
	
}
