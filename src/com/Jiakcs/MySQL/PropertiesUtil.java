package com.Jiakcs.MySQL;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	private static final String PROPERTIES_DEFAULT_PATH = "/JiakcsConfig.properties";
    
    public static String getProp(String key){
        String result = "";
        Properties prop = new Properties();
        InputStream in = PropertiesUtil.class.getResourceAsStream(PROPERTIES_DEFAULT_PATH);
        try {
            prop.load(in);
            if(prop.containsKey(key)){
                result = (prop.get(key)+"").trim();
            }else{
                result = "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
        return result;
    }
     
    public static String getProp(String key,String path){
        String result = "";
        Properties prop = new Properties();
        InputStream in = PropertiesUtil.class.getResourceAsStream(path);
        try {
            prop.load(in);
            if(prop.containsKey(key)){
                result = (prop.get(key)+"").trim();
            }else{
                result = "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
        return result;
    }
     
}
