package coms.block.ui.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import coms.block.ui.service.HelperService;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	HelperService helper;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	  //List<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();
	  coms.block.ui.model.CustomUser  user = helper.GetUserByLoginid(username);
	
	  List<SimpleGrantedAuthority> roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	   
	  if(user!=null)
	  {
		  //System.out.println("password retrieved--"+ user.getUser_password());
		  return new User(user.getLogin_id(),user.getUser_password(),roles);
		  //return User.withUsername(user.getLogin_id()).password(user.getUser_password()).authorities("ROLE_USER").build();
	  }
	  else
		  throw new UsernameNotFoundException("User not found with the name "+ username);
	
	}

}
