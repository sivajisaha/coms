package coms.api.userservice.dbservice;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import coms.api.userservice.model.User;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Component
public class UserService {
	@Autowired
	JdbcTemplate jdbcTemplate;
	public List<User> getallusers()
	{
		  String sql = "select user_id,first_name,middle_name,last_name,email,login_id,user_password,tenant_id from public.users";   
		  List<User> processdefs = jdbcTemplate.query(sql,
		                BeanPropertyRowMapper.newInstance(User.class));
		    return processdefs;
	}
	public User getuserbyloginid(String loginid)
	{
		 String user_sql = "select user_id,first_name,middle_name,last_name,email,login_id,user_password,tenant_id from public.users where login_id=?";   
		  List<User> users = jdbcTemplate.query(user_sql,
		                BeanPropertyRowMapper.newInstance(User.class),new Object[]{loginid});
		if(users.size()>0)
		{
			 String role_sql = "select a.group_name from public.user_groups a,public.user_group_mapping b,public.users c "
				        + "where a.group_id =b.group_id and b.user_id = c.user_id and c.login_id = ?";
			List<String> roles = jdbcTemplate.query(role_sql, new Object[] { loginid },
				        (rs, rowNum) -> new String(rs.getString("group_name"))
				    );
			 /*List<String> roles = jdbcTemplate.query(role_sql,
			                BeanPropertyRowMapper.newInstance(String.class),new Object[]{loginid});
			*/
			users.get(0).setRoles(roles);
			return  users.get(0);
		}
		return null;
	}
	public int register(User user) {
		 return jdbcTemplate.update(
	    	      "insert into public.users(first_name,last_name,login_id,user_password) VALUES (?, ?, ?, ?)", 
	    	      user.getFirst_name(), user.getLast_name(), user.getLogin_id(), user.getUser_password());
    }
	
}
