package com.example.myapp;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Spinner;
import com.example.myapp.model.City;
import com.example.myapp.model.District;
import com.example.myapp.model.Province;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 15-8-24.
 */

/**
 * 天气查询的Activity
 */
public class WeatherActivity extends Activity {
    //三级联动
    private Spinner spProvince,spCity,spDistrict;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
         initViews();

    }
    private List parseCity() {
        List<Province> provinces = null;
        Province province = null;
        City city = null;
        List<City> citys = null;
        District district = null;
        List<District> districts = null;
        //        getResources()方法相当于获取res目录
        Resources res=getResources();
        InputStream inputStream = res.openRawResource(R.raw.citys_weather);
        try {
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            XmlPullParser parser=factory.newPullParser();
            //解析XML
            parser.setInput(inputStream,"UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        provinces = new ArrayList<Province>();
                        break;
                    case XmlPullParser.START_TAG:
                        String tagName = parser.getName();
                        if("p".equals(tagName))
                        {
                            province = new Province();
                            citys = new ArrayList<City>();

                            int count = parser.getAttributeCount();
                            for(int i=0;i<count;i++)
                            {
                                String attName = parser.getAttributeName(i);
                                String attValue = parser.getAttributeValue(i);
                                if("p_id".equals(attName))
                                {
                                    province.setId(attValue);
                                }
                            }
                        }
                        if("pn".equals(tagName))
                        {
                            province.setName(parser.nextText());
                        }
                        if("c".equals(tagName))
                        {
                            city = new City();
                            districts = new ArrayList<District>();

                            int count = parser.getAttributeCount();
                            for(int i=0;i<count;i++)
                            {
                                String attName = parser.getAttributeName(i);
                                String attValue = parser.getAttributeValue(i);
                                if("c_id".equals(attName))
                                {
                                    city.setId(attValue);
                                }
                            }
                        }
                        if("cn".equals(tagName))
                        {
                            city.setName(parser.nextText());
                        }
                        if("d".equals(tagName))
                        {
                            district = new District();
                            int count = parser.getAttributeCount();
                            for(int i=0;i<count;i++)
                            {
                                String attName = parser.getAttributeName(i);
                                String attValue = parser.getAttributeValue(i);
                                if("d_id".equals(attName))
                                {
                                    district.setId(attValue);
                                }
                            }
                            district.setName(parser.nextText());
                            districts.add(district);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if("c".equals(parser.getName()))
                        {
                            city.setDistricts(districts);
                            citys.add(city);
                        }
                        if("p".equals(parser.getName()))
                        {
                            province.setCitys(citys);
                            provinces.add(province);
                        }
                        break;
                }
                event = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return provinces;
    }
    private  void initViews()
    {
        spProvince= (Spinner) findViewById(R.id.sp_province);
        spCity= (Spinner) findViewById(R.id.sp_city);
        spDistrict= (Spinner) findViewById(R.id.sp_district);
    }
}














