package com.jieshun.ops.comm.aop.user;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.jieshun.ops.comm.LoginInfoCache;

/**
 * App接口访问拦截器，判断访问是否已经登录过 
 * 规则：请求是否包含 empId参数，empId是否已经存在缓存，如果是正常调用，否则返回异常信息。
 */
@Component
@Aspect
public class AppLoginVerifier {
	private static final Logger logger = Logger
			.getLogger(AppLoginVerifier.class.getName());

	/*@Around("execution(* com.jd.pims.pem.controller.*.* (..))")*/
	public Object process(ProceedingJoinPoint joinPoint) throws Throwable {

		
		Object[] args = joinPoint.getArgs();
		HttpServletRequest request = null;
		for (Object param : args) {
			if (param instanceof HttpServletRequest) {
				request = (HttpServletRequest) param;
				break;
			}
			
		}
		if (request != null ) {
			String empId = request.getParameter("empId");
			if (empId == null || !LoginInfoCache.isLogin(empId)) {
				String tmp = joinPoint.getSignature().toString();
				logger.debug("您没有得到授权！" + tmp);
				JsonObject retMsg = new JsonObject();
				retMsg.addProperty("returnCode", -1);
				retMsg.addProperty("message", "您没有得到授权！");
				return retMsg.toString();
			}
		}else{
			JsonObject retMsg = new JsonObject();
			retMsg.addProperty("returnCode", -2);
			retMsg.addProperty("message", "您的请求无效！");
			return retMsg.toString();
		}

		Object object = joinPoint.proceed();
		return object;
	}

}
