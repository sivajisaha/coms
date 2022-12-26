package coms.block.ui.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import coms.block.ui.model.MediatorRequestBody;
import coms.block.ui.service.DbService;
import coms.block.ui.service.HelperService;
import coms.block.ui.model.CustomUser;
import coms.block.ui.model.KeyValue;

@RestController
public class ServletController {
	@Autowired
	HelperService helper;
	@Autowired
	DbService dbService;
	@RequestMapping("/hellouser")
	public String getUser()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
		    String currentUserName = authentication.getName();
		    return "Hello " + currentUserName;
		}
		return "You are not authorized";
	}
	@RequestMapping(value = "/getuser", produces ="application/json") 
	public CustomUser GetUserDetails()
	{
		CustomUser userdto = new CustomUser();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
		    String currentUserName = authentication.getName();
		    userdto = helper.GetUserByLoginid(currentUserName);
		    userdto.setUser_password("");
		}
		return userdto;
	}
	
	@PostMapping(value = "/invoke", consumes = "application/json", produces ="application/json") 
	public String invoke(@RequestBody MediatorRequestBody request) 
	{ 
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String logged_userid= "";
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			logged_userid = authentication.getName();
		}
		String targetserviceuri = dbService.getConfigUris().get(request.getService());
		if(targetserviceuri!=null)
		{
		  String response ="";
		  WebClient client = WebClient.create(targetserviceuri); 
		  switch(request.getRequesttype())
		  {
		  	case "get":
		  		response = client.get()
		  		.uri(request.getOperation())
		  		.header("logged_userid", logged_userid)
		  		.retrieve()
		  		.bodyToMono(String.class)
		  		.block();
		  	break;
		  	case "post":
		  		response = client.post()
		  		.uri(request.getOperation())
		  		.header("logged_userid", logged_userid)
		  		.contentType(MediaType.APPLICATION_JSON)
		  		.body(BodyInserters.fromValue(request.getRequestbody()))
		  		.retrieve()
		  		.bodyToMono(String.class)
		  		.block();
		  	break;
		  }
		  return response; 
		 }
		 else 
			return "error";
	 }
}
