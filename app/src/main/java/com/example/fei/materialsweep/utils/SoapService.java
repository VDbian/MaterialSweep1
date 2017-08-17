package com.example.fei.materialsweep.utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by fei on 2017/7/18.
 */

public class SoapService implements ISoapService {

    private static final String NameSpace = "http://tempuri.org/";
    private static final String URL = "http://118.123.249.81:8068/Service1.svc";
//    private static final String URL = "http://192.168.0.101:1010/Service1.svc";
    private static final String SOAP_ACTION = "http://tempuri.org/IService1/DoDHSPSelf";
    private static final String MethodName = "DoDHSPSelf";

    private String inParam;

    public SoapService(String inParam) {
        this.inParam = inParam;
    }

    public  String LoadResult() {
        SoapObject soapObject = new SoapObject(NameSpace, MethodName);
        soapObject.addProperty("inParam", inParam);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // 版本
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE trans = new HttpTransportSE(URL);
        trans.debug = true; // 使用调试功能
        try {
            trans.call(SOAP_ACTION, envelope);
//            Log.e("VD","Call Successful!");
        } catch (IOException e) {
//            Log.e("VD","IOException:"+e.getMessage());
            e.printStackTrace();
        } catch (XmlPullParserException e) {
//            Log.e("VD","XmlPullParserException");
            e.printStackTrace();
       }
        SoapObject  result = (SoapObject)envelope.bodyIn;
//        Log.e("VD",result);
        return result.getProperty(0).toString();
    }
}
