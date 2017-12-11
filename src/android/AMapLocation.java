package com.joyo.cordova.amaplocation;

import java.util.Locale;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationClientOption.AMapLocationProtocol;
import com.amap.api.location.AMapLocationListener;


/**
 * Cordova高德地图定位插件
 * @ClassName AMapLocation
 * @author JoyoDuan
 * @date 2017年12月7日
 * Add by JoyoDuan on 2017-12-07
 */
public class AMapLocation extends CordovaPlugin {

    //action触发方法名
    private static final String ACTION_GETLOCATION = "getLocation";

	//声明AMapLocationClient类对象
	private AMapLocationClient locationClient = null;
	//声明定位参数
    private AMapLocationClientOption locationOption = null;
    
	//js回调接口
	private CallbackContext callbackContext = null;
    
    
	/**
	 * 程序入口
	 * @param action
	 * @param args
	 * @param callbackContext
	 * @return
	 * @throws JSONException
	 */
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    	
    	
        if (ACTION_GETLOCATION.equals(action)) 
        {
        	this.callbackContext = callbackContext;
        	
        	PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
        	pluginResult.setKeepCallback(true);
        	callbackContext.sendPluginResult(pluginResult);
        	this.getLocation();
            return true;
        }
        return false;
    }
    
    
    /**
     * 获取定位
     * @author JoyoDuan
     */
    private void getLocation () 
    {
    	if (locationClient == null) 
    	{
    		this.initLocation();
    	}
    	this.startLocation();
    }
    
    
    /**
     * 初始化定位
     * @author JoyoDuan
     */
    private void initLocation ()
    {
    	//初始化client
    	locationClient = new AMapLocationClient(this.webView.getContext());
    	//设置定位参数
    	locationOption = getDefaultOption();
    	locationClient.setLocationOption(locationOption);
    	//设置定位监听
    	locationClient.setLocationListener(locationListener);
    }
    
    
    /**
	 * 默认的定位参数
	 * @since 2.8.0
	 * @author JoyoDuan
	 *
	 */
    private AMapLocationClientOption getDefaultOption() 
    {
    	AMapLocationClientOption mOption = new AMapLocationClientOption();
    	
    	//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
    	//低功耗   Battery_Saving, 高精度   Hight_Accuracy, GPS    Device_Sensors
		mOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
		mOption.setGpsFirst(false);
		//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
		mOption.setHttpTimeOut(30000);
		//可选，设置定位间隔。默认为2秒
		mOption.setInterval(2000);
		//可选，设置是否返回逆地理地址信息。默认是true
		mOption.setNeedAddress(true);
		//可选，设置是否单次定位。默认是false
		mOption.setOnceLocation(false);
		//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
		mOption.setOnceLocationLatest(false);
		//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
		AMapLocationClientOption.setLocationProtocol(AMapLocationProtocol.HTTP);
		//可选，设置是否使用传感器。默认是false
		mOption.setSensorEnable(false);
		//可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
		mOption.setWifiScan(true); 
		//可选，设置是否使用缓存定位，默认为true
		mOption.setLocationCacheEnable(true); 
		return mOption;
    }
    
    
    /**
	 * 定位监听
	 * @author JoyoDuan
	 */
    AMapLocationListener locationListener = new AMapLocationListener() {
    	@Override
    	public void onLocationChanged(AMapLocation amapLocation) 
    	{
    		try 
    		{
    			JSONObject json = new JSONObject();
    			
    			//解析定位结果
    			//errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
    			if (null != amapLocation && amapLocation.getErrorCode() == 0) 
    			{
    				json.put("status", "定位成功");
    				//定位类型
                    json.put("type", amapLocation.getLocationType());
                    //纬度
                    json.put("latitude", amapLocation.getLatitude());
                    //经度
                    json.put("longitude", amapLocation.getLongitude());
                    
                    json.put("hasAccuracy", amapLocation.hasAccuracy());
                    //精度
                    json.put("accuracy", amapLocation.getAccuracy());
                    //角度
                    json.put("bearing", amapLocation.getBearing());
                    // 获取当前提供定位服务的卫星个数
                    //星数
                    json.put("satellites", amapLocation.getSatellites());
                    //国家
                    json.put("country", amapLocation.getCountry());
                    //省
                    json.put("province", amapLocation.getProvince());
                    //市
                    json.put("city", amapLocation.getCity());
                    //城市编码
                    json.put("citycode", amapLocation.getCityCode());
                    //区
                    json.put("district", amapLocation.getDistrict());
                    //区域码
                    json.put("adcode", amapLocation.getAdCode());
                    //地址
                    json.put("address", amapLocation.getAddress());
                    //路
                    json.put("road", amapLocation.getRoad());
                    //速度
                    json.put("speed", amapLocation.getSpeed());
                    //兴趣点
                    json.put("poi", amapLocation.getPoiName());
                    //兴趣点
                    json.put("time", amapLocation.getTime());
    			}
    			else 
    			{
    				json.put("status", "定位失败");
                    json.put("errcode", amapLocation.getErrorCode());
                    json.put("errinfo", amapLocation.getErrorInfo());
                    json.put("detail", amapLocation.getLocationDetail());
    			}
    			json.put("backtime", System.currentTimeMillis());
    			
    			PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, json);
    			pluginResult.setKeepCallback(true);
    			callbackContext.sendPluginResult(pluginResult);
    		}
    		catch (JSONException e) 
    		{
    			PluginResult pluginResult = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
                pluginResult.setKeepCallback(true);
                callbackContext.sendPluginResult(pluginResult);
			}
    		finally 
    		{
    			locationClient.stopLocation();
    		}
    	}
    };
    
    
    /**
     * 开始定位
     * @author JoyoDuan
     */
    private	void startLocation () 
    {
    	//启动定位
    	locationClient.startLocation();
    }
    
    
    /**
     * 停止定位
     * @author JoyoDuan
     */
    private void stopLocation () 
    {
    	//停止定位
    	locationClient.stopLocation();
    }
    
    
    /**
	 * 销毁定位
	 * @since 2.8.0
	 * @author JoyoDuan
	 *
	 */
    private void destroyLocation()
    {
    	if (null != locationClient) 
    	{
    		/**
			 * 如果AMapLocationClient是在当前Activity实例化的，
			 * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
			 */
			locationClient.onDestroy();
			locationClient = null;
			locationOption = null;
    	}
    }

//auto create
//    private void coolMethod(String message, CallbackContext callbackContext) {
//        if (message != null && message.length() > 0) {
//            callbackContext.success(message);
//        } else {
//            callbackContext.error("Expected one non-empty string argument.");
//        }
//    }
}
