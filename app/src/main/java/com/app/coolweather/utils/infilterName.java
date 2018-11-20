package com.app.coolweather.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

public class infilterName {
    private static String name;
    private static Context context;
    private static String xmldata;

    /*
     * 解析具体位置的地址代码，从citylist.xml文件中获取
     */
    public static String infilterName(String PlaceName){
        context = globalContext.getContext();
        AssetManager assetManager = context.getAssets();
        try{
            InputStream inputStream = assetManager.open("citylist.xml");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line=bufferedReader.readLine()) != null){
                xmldata += line;
            }
            if(xmldata.contains("null")) {
                xmldata = xmldata.substring(4);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
         * 开始解析xml数据
         */
        try {
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            //xml解析器
            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            //当前解析状态
            int eventType = xmlPullParser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                String nodename = xmlPullParser.getName();
                switch (eventType){
                    //开始解析
                    case XmlPullParser.START_TAG:
                        if (nodename.equals("d")){
                            String text = xmlPullParser.getAttributeValue(1);
                            int placenamelenght = PlaceName.length();
                            for(int i=2; i<placenamelenght; i++){
                                String newplacename = PlaceName.substring(0,i);
                                if(newplacename.equals(text)){
                                    return xmlPullParser.getAttributeValue(0);
                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                //解析下一个节点
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }
}
