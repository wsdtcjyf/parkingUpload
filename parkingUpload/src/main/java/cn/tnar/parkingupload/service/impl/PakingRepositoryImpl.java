package cn.tnar.parkingupload.service.impl;

import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import net.sf.json.JSONArray;
import cn.tnar.parkingupload.model.REQ_COMM_DATA;
import cn.tnar.parkingupload.service.PakingRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;


@Repository
public class PakingRepositoryImpl implements PakingRepository{
	
	@Value("${app.mobilefly.parkcount.sql}")
	private String parkCountSql;
	
	@Value("${app.mobilefly.parking.sql}")
	private String parkingSql;
	
	//入场流水
	@Value("${app.mobilefly.parkingStream.sql}")
	private String parkingStreamsql;
	
	//初始化最大intime
	@Value("${app.mobilefly.parkingMaxIntime.sql}")
	private String parkingMaxIntimesql;
	
	//初始化最大intime
	@Value("${app.mobilefly.parkingMaxOutTime.sql}")
	private String parkingMaxOutTimesql;
	
	//初始化最大intime
	@Value("${app.mobilefly.parkingOutStream.sql}")
	private String parkingOutStreamsql;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Transactional(readOnly=true)
	public Integer getParkCount() {
		//String sql = PropertyTool.getParkCount();
		return jdbcTemplate.queryForObject(parkCountSql, null, Integer.class);
	}
	
	@Transactional(readOnly=true)
	public Integer getParkingCount() {
		return jdbcTemplate.queryForObject(parkingSql, null, Integer.class);
	}
	
	@Transactional(readOnly=true)
	public List getParkingStream(String intime1,String intime2) {
		//intime1,2动态参数
		List streams = jdbcTemplate.queryForList(parkingStreamsql,new Object[] {intime1,intime2});
		return streams;
	}
	
	@Transactional(readOnly=true)
	public String getMaxIntime() {
		String maxIntime = jdbcTemplate.queryForObject(parkingMaxIntimesql,null,String.class);
		return maxIntime;
	}
	
	@Transactional(readOnly=true)
	public List getParkingOutStream(String intime1,String intime2) {
		//intime1,2动态参数
		List streams = jdbcTemplate.queryForList(parkingOutStreamsql,new Object[] {intime1,intime2});
		return streams;
	}
	
	@Transactional(readOnly=true)
	public String getMaxOuttime() {
		String maxIntime = jdbcTemplate.queryForObject(parkingMaxOutTimesql,null,String.class);
		return maxIntime;
	}
}
