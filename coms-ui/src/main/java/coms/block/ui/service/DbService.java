package coms.block.ui.service;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import coms.block.ui.model.KeyValue;
import lombok.Getter;
import lombok.Setter;
@Component
@Getter @Setter 
public class DbService {
	@Autowired
    JdbcTemplate jdbcTemplate;
	@Value("${coms.app.code}")
	private String appCode;
	@Getter
	private HashMap<String,String> configUris;
	@PostConstruct
	private void init() {
		List<KeyValue> urisfromdb = getConfigUrisfromDb(appCode);
		HashMap<String,String> uris =new HashMap<String,String>();
		for(KeyValue kv: urisfromdb)
		{
			uris.put(kv.getKey(), kv.getKey_Value());
		}
		configUris = uris;
		System.out.println("running init() for the first time");
	}
	private List<KeyValue> getConfigUrisfromDb(String app_code) {
	    String sql = "select key,key_value from public.config_settings where config_type='uri' and app_code=?";
	    return jdbcTemplate.query(sql,
	                BeanPropertyRowMapper.newInstance(KeyValue.class),new Object[]{app_code});
	}
}
