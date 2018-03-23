package cn.tnar.parkingupload.tool;

/*
 * String tool
 * */
public class StringTool {

	//String array
	public static boolean isNullOrEmpty(String... values) {
		for (String value : values) {
			if (isNullOrEmpty(value))
				return true;
		}
		return false;
	}

	//String
	public static boolean isNullOrEmpty(String value) {
		if (null == value || "".equals(value.trim())) {
			return true;
		}
		return false;
	}

}
