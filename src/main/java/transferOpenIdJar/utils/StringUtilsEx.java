package transferOpenIdJar.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class StringUtilsEx {

	public static String A2Z = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static String A2Z(int index) {
		return String.valueOf(A2Z.toCharArray()[index]);
	}
	
	public static int abc2int(String letter){
		letter = letter.toUpperCase();
		if (letter.length() == 1){
			char c = letter.toCharArray()[0];
			return c - 'A'; 
		}else{
			return 0;
		}			
	}

	public static int containCount(String text, char c) {
		int result = 0;
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == c)
				result++;
		}
		return result;
	}

	public static boolean isNumeric(Object str) {
		boolean result = false;
		if (str != null) {
			result = true;
			try {
				new BigDecimal(str.toString());
			} catch (Exception e) {
				result = false;
			}
		} else {
			result = false;
		}
		return result;
	}

	public static boolean isInteger(Object o) {
		try {
			Integer.valueOf(o.toString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String leftStr(String text, int len) {
		return text.substring(0, len);
	}

	public static String rightStr(String text, int len) {
		String result = "";
		for (int i = text.length() - 1; i <= 0; i--) {
			if (i + len >= text.length())
				result = text.charAt(i) + result;
			else
				break;
		}
		return result;
	}

	public static String leftStr(String text, String splitStr) {
		int iPos = text.indexOf(splitStr);
		if (iPos >= 0)
			return text.substring(0, iPos);
		else
			return text;

	}

	public static String rightStr(String text, String splitStr) {
		int iPos = text.lastIndexOf(splitStr);
		if (iPos >= 0) {
			return text.substring(iPos + splitStr.length());
		} else {
			return text;
		}
	}

	public static boolean sameText(String text1, String text2) {
		if (text1 == null)
			text1 = "";
		if (text2 == null)
			text2 = "";
		return text1.equalsIgnoreCase(text2);
	}

	public static boolean isEmpty(String text) {
		return (text == null || "".equals(text.trim()));
	}

	public static boolean isEmpty(Object obj) {
		if (obj == null)
			return true;
		if(obj instanceof String){
			return isEmpty(String.valueOf(obj));
		}
		if(obj instanceof List){
			if(null != obj && ((List) obj).size()>0){
				return false;
			}
            return true;
		}
		return false;
	}

	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}
	
	public static String mergeStrings(String text1, String text2, String div) {
		String result = text1;
		if (!isEmpty(text2)) {
			if (!isEmpty(result))
				result += div + text2;
			else
				result = text2;
		}
		return result;
	}

	public static String copyString(String item, int copyCount) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < copyCount; i++) {
			sb.append(item);
		}
		return sb.toString();
	}

	// 截取指定字段对包括内的字符串
	public static String subStringByDiv(String text, String beginDiv,
			String endDiv) {
		int begin = text.indexOf(beginDiv) + beginDiv.length();
		int end = text.indexOf(endDiv);
		String result = "";
		if (begin > -1 && end > -1 && end > begin)
			result = text.substring(begin, end);
		return result;
	}

	/**
	 * 首字母大写
	 * 
	 * @param item
	 * @return
	 */
	public static String firstWordUpCase(String item) {
		if (isEmpty(item))
			return "";
		else {
			StringBuilder sb = new StringBuilder();
			sb.append(item.toUpperCase().toCharArray()[0]);
			sb.append(item.substring(1));
			return sb.toString();
		}
	}

	/**
	 * 首字母小写
	 * 
	 * @param item
	 * @return
	 */
	public static String firstWordLowerCase(String item) {
		if (isEmpty(item))
			return "";
		else {
			StringBuilder sb = new StringBuilder();
			sb.append(item.toLowerCase().toCharArray()[0]);
			sb.append(item.substring(1));
			return sb.toString();
		}
	}

	public static ArrayList<String> splitString(String str, String div) {
		String[] strs = str.split(div);
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < strs.length; i++) {
			result.add(strs[i]);
		}
		return result;
	}

	/**
	 * 次函数正确性未得到验证
	 * 
	 * @param origin
	 *            原始字符串
	 * @param start
	 *            起始位置
	 * @param len
	 *            截取长度(一个汉字长度按2算的) ,长度超过取到原始字符串末
	 * @return 返回的字符串
	 */
	public static String subCnString(String origin, int start, int len) {
		class SubCnString {

			/**
			 * 截取一段字符的长度,不区分中英文,如果数字不正好，则少取一个字符位
			 * 
			 * 
			 * @param origin
			 *            原始字符串
			 * @param start
			 *            起始位置
			 * @param len
			 *            截取长度(一个汉字长度按2算的) ,长度超过取到原始字符串末
			 * @return 返回的字符串
			 */
			public String substring(String origin, int start, int len) {
				/*if (origin == null || origin.equals("") || len < 1)*/
				/*edit by yjh 20190212 代码走查优化 参数不要作为equals()的调用方 begin*/
				if (origin == null || "".equals(origin) || len < 1)
				/*edit by yjh 20190212 代码走查优化 参数不要作为equals()的调用方 end*/
					return "";
				byte[] strByte = new byte[len];
				if (start + len > length(origin)) {
					len = length(origin) - start;
				}
				try {
					System.arraycopy(origin.getBytes("UTF-8"), start, strByte, 0,
							len);
					int count = 0;
					for (int i = 0; i < len; i++) {
						int value = (int) strByte[i];
						if (value < 0) {
							count++;
						}
					}
					if (count % 2 != 0) {
						len = (len == 1) ? ++len : --len;
					}
					return new String(strByte, 0, len, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			}
		}

		return (new SubCnString()).substring(origin, start, len);
	}
	
	/**
	 * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
	 * 
	 * @param c
	 *            需要判断的字符
	 * @return 返回true,Ascill字符
	 */

	public static boolean isLetter(char c) {
		int k = 0x80;
		return c / k == 0 ? true : false;
	}

	/**
	 * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
	 * 
	 * @param s
	 *            需要得到长度的字符串
	 * @return i得到的字符串长度
	 */
	public static int length(String s) {
		if (s == null)
			return 0;
		char[] c = s.toCharArray();
		int len = 0;
		for (int i = 0; i < c.length; i++) {
			len++;
			if (!isLetter(c[i])) {
				len++;
			}
		}
		return len;
	}	
	
	public static List<String> arrToList(String[] arr){
		List<String> paramlist = new ArrayList<String>();
		for (String string : arr) {
			paramlist.add(string);
		}
		return paramlist;
	}
	
	public static String encodeString(String s, String charset) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes(charset);
				} catch (Exception ex) {
					System.out.println(ex);
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}
	
	public static boolean isLong(Object o) {
		try {
			Long.valueOf(o.toString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isDouble(Object o) {
		try {
			Double.valueOf(o.toString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static String inc(String billNo){
		if (billNo.length() > 0){
			char c = billNo.toCharArray()[billNo.length() - 1];		
			String left = leftStr(billNo, billNo.length() - 1);
			if ((c >= '0' && c <= '8')||(c >= 'A' && c <= 'Y')||(c >= 'a' && c <= 'y')){
				c++;
				return left + c;
			}else if (c == '9'){
				left = inc(left);
				return left + '0';
			}else if (c == 'Z'){
				left = inc(left);
				return left + 'A';
			}else if (c == 'z'){
				left = inc(left);
				return left + 'a';
			}	
		}		
		return billNo;
	}
	
	public static String subStr(String str, int start, int length) {
		if(str == null)
			return "";
		int endIndex = start + length;
		if(endIndex > str.length())
			endIndex = str.length();
		return str.substring(start, endIndex);
	}

	/**
	 * 合并字符串数组
	 *
	 * @param a
	 *            字符串数组0
	 * @param b
	 *            字符串数组1
	 * @return 返回合并后的字符串数组
	 */
	public static String[] mergeStringArray(String[] a, String[] b) {
		if (a.length == 0 || isEmpty(a))
			return b;
		if (b.length == 0 || isEmpty(b))
			return a;
		String[] c = new String[a.length + b.length];
		for (int m = 0; m < a.length; m++) {
			c[m] = a[m];
		}
		for (int i = 0; i < b.length; i++) {
			c[a.length + i] = b[i];
		}
		return c;
	}

	public static String arr2text(String[] arrs, String div){
		StringBuilder result = new StringBuilder();
		for (String item : arrs) {
			if (result.length() > 0){
				result.append(div);
			}
			result.append(item);
		}
		return result.toString();
	}
	
	/*public static void main(String[] args) {
		String aa = "我是字符串";
		System.out.println(subStr(aa, 0, 6));
		String fileName ="abc.text";
		System.out.println(getFileSuffix(fileName));
		System.out.println(genFileName(getFileSuffix(fileName)));
	}*/

	public static boolean isCN(String str) {

		char[] chars = str.toCharArray();
		boolean isGB2312 = false;
		for (int i = 0; i < chars.length; i++) {
			byte[] bytes = ("" + chars[i]).getBytes();
			if (bytes.length > 1) {
				int[] ints = new int[2];
				ints[0] = bytes[0] & 0xff;
				ints[1] = bytes[1] & 0xff;
				if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40
						&& ints[1] <= 0xFE) {
					isGB2312 = true;
					break;
				}
			}
		}
		return isGB2312;
	}

	public static String getRandCode(){
		Random random = new Random();
		return String.valueOf(random.nextInt(89999999)+10000000);
	}

	public static String getRandCodeSix(){
		Random random = new Random();
		return String.valueOf(random.nextInt(899999)+100000);
	}

	public static String genFileName(String suffix){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssS");
		return sdf.format(new Date()) + suffix;
	}

	public static String getFileSuffix(String fileName){
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/*private final static long minute = 60 * 1000;// 1分钟
	private final static long hour = 60 * minute;// 1小时
	private final static long day = 24 * hour;// 1天
	private final static long month = 31 * day;// 月
	private final static long year = 12 * month;// 年
*/	
	/*edit by yjh 20190212 代码走查优化 常量命名应该全部大写，单词间用下划线隔开，力求语义表达完整清楚 begin*/
	private final static long MINUTE = 60 * 1000;// 1分钟
	private final static long HOUR = 60 * MINUTE;// 1小时
	private final static long DAY = 24 * HOUR;// 1天
	private final static long MONTH = 31 * DAY;// 月
	private final static long YEAR = 12 * MONTH;// 年
	/*edit by yjh 20190212 代码走查优化 常量命名应该全部大写，单词间用下划线隔开，力求语义表达完整清楚 end*/
	/**
	 * 返回文字描述的日期
	 *
	 * @param date
	 * @return
	 */
	public static String getTimeFormatText(Date date) {
		if (date == null) {
			return null;
		}
		long diff = new Date().getTime() - date.getTime();
		long r = 0;
		/*if (diff > year) {
		r = (diff / year);
		return r + "年前";
	}
	if (diff > month) {
		r = (diff / month);
		return r + "个月前";
	}
	if (diff > day) {
		r = (diff / day);
		return r +"天前";
	}
	if (diff > hour) {
		r = (diff / hour);
		return r + "小时前";
	}
	if (diff > minute) {
		r = (diff / minute);
		return r + "分钟前";
	}*/
	/*edit by yjh 20190212 代码走查优化 常量命名应该全部大写，单词间用下划线隔开，力求语义表达完整清楚 begin*/
	if (diff > YEAR) {
		r = (diff / YEAR);
		return r + "年前";
	}
	if (diff > MONTH) {
		r = (diff / MONTH);
		return r + "个月前";
	}
	if (diff > DAY) {
		r = (diff / DAY);
		return r +"天前";
	}
	if (diff > HOUR) {
		r = (diff / HOUR);
		return r + "小时前";
	}
	if (diff > MINUTE) {
		r = (diff / MINUTE);
		return r + "分钟前";
	}
	/*edit by yjh 20190212 代码走查优化 常量命名应该全部大写，单词间用下划线隔开，力求语义表达完整清楚 end*/
		return "刚刚";
	}

	/**
	 * 分隔符第一个字串
	 * @param content
	 * @param split
	 * @return
	 * @author lijibin
	 */
	public static String splitFirst(String content,String split){
		ArrayList<String> arr = splitString(content,split);
		if(isNotEmpty(arr)){
			return arr.get(0);
		}
		return content;
	}
	//add by yjh begin
	//判断是否以汉字结尾
	public static boolean checkEndWithChineseCharacter(String str) {
		if(StringUtilsEx.isNotEmpty(str)) {	
			String endStr = str.substring(str.length()-1,str.length());
			Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]"); //判断是否存在汉字
			Matcher isNum = p.matcher(endStr);
	        if (isNum.find()) {
	            return true;
	        }
		}
        return false; 
	}
	
	/**
     * 去掉bean中所有属性为字符串的前后空格
     * @param bean
     * @throws Exception
     */
    public static void beanAttributeValueTrim(Object bean) throws Exception {
        if(bean!=null){
            //获取所有的字段包括public,private,protected,private
            Field[] fields = bean.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                if (("java.lang.String").equals(f.getType().getName())) {
                    String key = f.getName();//获取字段名
                    Object value = getFieldValue(bean, key);
                    
                    if (value == null)
                        continue;
                    
                    setFieldValue(bean, key, value.toString().trim());
                }
            }
        }
    }
    /**
     * 利用反射通过get方法获取bean中字段fieldName的值
     * @param bean
     * @param fieldName
     * @return
     * @throws Exception
     */
    private static Object getFieldValue(Object bean, String fieldName)
            throws Exception {
        StringBuffer result = new StringBuffer();
        String methodName = result.append("get")
                .append(fieldName.substring(0, 1).toUpperCase())
                .append(fieldName.substring(1)).toString();

        Object rObject = null;
        Method method = null;

        @SuppressWarnings("rawtypes")
        Class[] classArr = new Class[0];
        method = bean.getClass().getMethod(methodName, classArr);
        rObject = method.invoke(bean, new Object[0]);

        return rObject;
    }
    /**
     * 利用发射调用bean.set方法将value设置到字段
     * @param bean
     * @param fieldName
     * @param value
     * @throws Exception
     */
    private static void setFieldValue(Object bean, String fieldName, Object value) throws Exception {
        StringBuffer result = new StringBuffer();
        String methodName = result.append("set")
                .append(fieldName.substring(0, 1).toUpperCase())
                .append(fieldName.substring(1)).toString();

        /**
         * 利用发射调用bean.set方法将value设置到字段
         */
        Class[] classArr = new Class[1];
        classArr[0]="java.lang.String".getClass();
        Method method=bean.getClass().getMethod(methodName,classArr);
        method.invoke(bean,value);
    } 
}
