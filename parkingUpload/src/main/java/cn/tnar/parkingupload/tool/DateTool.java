package cn.tnar.parkingupload.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTool {
	
	public static String getDateFormater(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date());
	}
	
	public static String getDateFormater() {
		return getDateFormater("yyyyMMddHHmmss");
	}

}
