package com.hummingbird.common.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hummingbird.common.ext.AppIniter;
import com.hummingbird.common.face.HeartBreakIniter;
import com.hummingbird.common.util.PropertiesUtil;
import com.hummingbird.common.util.StrUtil;
import com.hummingbird.paas.util.MacUtil;

/**
 * 启动初始化
 * @author huangjiej_2
 * 2014年9月27日 上午9:15:06
 */
public class StartLoadListener implements ServletContextListener {
	private static final Log log = LogFactory.getLog(StartLoadListener.class);

	public void contextDestroyed(ServletContextEvent sce) {
		// System.out.println("contextDestroyed.....");
	}

	public void contextInitialized(ServletContextEvent sce) {
		//检查钥匙
		checkKey();
		ApplicationContext ac = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
		com.hummingbird.common.util.SpringBeanUtil.init(ac);
		
		Map param=new HashMap();
		String classes = new PropertiesUtil().getProperty("app.init.classes");
		if(StrUtil.isNotBlank(classes))
		{
			String[] zlasses = classes.split(",");
			for (int i = 0; i < zlasses.length; i++) {
				String zlass = zlasses[i];
				AppIniter initer;
				try {
					initer = (AppIniter) Class.forName(zlass).newInstance();
					initer.init(param);
				} catch (Exception e) {
					log.error(String.format("初始化出错"),e);
				}
				
			}
		}
		//心跳初始化
		if (log.isDebugEnabled()) {
			log.debug(String.format("心跳初始化"));
		}
		new HeartBreakIniter().initHeartBreak();
		
	}
	
     private void checkKey(){ 
    	 log.debug("开始检查key:");
         String hasKey = "false";
    	 PropertiesUtil pu=new PropertiesUtil();
    	 String requiredMac = pu.getProperty("whoareu");
    	 if(StringUtils.isBlank(requiredMac)){
    		 //没有配置mac加密锁
    		 hasKey = "true"; 
    	 }else{
    		 String localMac = "";
 			try {
 				localMac = MacUtil.getMacAddress();
 			} catch (Exception e) {
 				log.debug("获取mac地址失败");
 				e.printStackTrace();
 			}
 	    	if(StringUtils.isBlank(localMac)){
 	    		hasKey = "false";
 	    	}else{
 	    		com.hummingbird.paas.util.MD5 md5 = new com.hummingbird.paas.util.MD5();
 	    		//md5加密获取的mac地址
 	    		String md5Mac = md5.getMD5ofStr(localMac+"maiquan");
 	    		if(!md5Mac.equals(requiredMac)){
 	    			hasKey = "false";
 	    			log.debug("当前服务器未授权运行此程序");
 	    			System.exit(0);
 	    		}else{
 	    			hasKey = "true";  
 	    		}
 	    	}
    	 }
    	 System.setProperty("hasKey", hasKey); 
    	 log.debug("结束检查key，key值为："+hasKey);
    }

}
