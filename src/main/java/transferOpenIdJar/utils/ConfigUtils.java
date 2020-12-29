package transferOpenIdJar.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

/**
 * @Author: suahe.
 * @Date:Created in 2020/5/8 15:49.
 * @Description: 读取配置工具类
 */
public class ConfigUtils {
	
	public static String getProperties(String key) {
        String value = "";
        Properties prop = new Properties();
        InputStream in = null;
        try {
            in = ConfigUtils.class.getClassLoader().getResourceAsStream("jdbc.properties") ;
            prop.load(in); // 加载属性列表
            value = prop.getProperty(key);
        } catch (Exception e) {
            System.out.println(e);
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }
	
	public static HashMap getPropertiesMap() {
		HashMap map =new HashMap<String, String>();
        Properties prop = new Properties();
        InputStream in = null;
        try {
            in = ConfigUtils.class.getClassLoader().getResourceAsStream("jdbc.properties") ;
            prop.load(in);     ///加载属性列表
            Iterator<String> it=prop.stringPropertyNames().iterator();
            while(it.hasNext()){
                String key=it.next();
                map.put(key, prop.getProperty(key));
                System.out.println(key+":"+prop.getProperty(key));
            }
        } catch (Exception e) {
            System.out.println(e);
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
	
	public static void savePrpperties(HashMap<String, String> map) {
		Properties prop = new Properties();     
        try{
            ///保存属性到jdbc.properties文件
            FileOutputStream oFile = new FileOutputStream("src/main/resources/jdbc.properties");//true表示追加打开
            for(String key:map.keySet()){
            	String value = map.get(key).toString();
            	prop.setProperty(key,value);
            }
            prop.store(oFile, "The New properties file");
            oFile.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
	
	public static void main(String[] args) {
		HashMap map = getPropertiesMap();
		System.out.println(map);
	}

}
