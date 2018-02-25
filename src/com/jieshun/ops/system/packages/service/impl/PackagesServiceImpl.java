package com.jieshun.ops.system.packages.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jieshun.ops.system.packages.dao.PackagesDao;
import com.jieshun.ops.system.packages.service.IPackagesService;
import com.jieshun.ops.util.FTPUtil;
import com.jieshun.ops.util.StringUtil;

@Service("packagesService")
public class PackagesServiceImpl implements IPackagesService {

	@Autowired
	private PackagesDao packagesDao;
	@Autowired
	private DataSourceTransactionManager transactionManager;


	private static final Logger logger = Logger.getLogger(PackagesServiceImpl.class.getName());

	@Override
	public String addPackage(HttpServletRequest request) {
		JsonObject response = new JsonObject();
		TransactionStatus status = null;
		try {
			String componentId = StringUtil.Undefined2Str(request.getParameter("componentId"));
			String versionDate = StringUtil.Undefined2Str(request.getParameter("versionDate"));
			String principal = StringUtil.Undefined2Str(request.getParameter("principal"));
			String releaseNote = StringUtil.Undefined2Str(request.getParameter("releaseNote"));
			String remark = StringUtil.Undefined2Str(request.getParameter("remark"));
			String random = UUID.randomUUID().toString().replace("-", "").substring(22);
			ArrayList<String> fileNames = new ArrayList<>();
			
			// 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
					request.getSession().getServletContext());
			// 检查form中是否有enctype="multipart/form-data"
			if (multipartResolver.isMultipart(request)) {
				// 将request变成多部分request
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				// 获取multiRequest 中所有的文件名
				Iterator iter = multiRequest.getFileNames();
				
				FTPUtil ftp = new FTPUtil("10.10.202.2", 21, "bs", "bs");
				
				while (iter.hasNext()) {
					System.out.println(request.getSession().getServletContext()
							.getRealPath("")
							+ System.getProperty("file.separator") );
					// 一次遍历所有文件
					MultipartFile file = multiRequest.getFile(iter.next().toString());
					if (file != null) {
						fileNames.add(file.getOriginalFilename());
						String path = request.getSession().getServletContext().getRealPath("")
								+ System.getProperty("file.separator") + versionDate + componentId
								+ file.getOriginalFilename();
						// 上传
						file.transferTo(new File(path));
						// ftp.mkdir("/jsops/");
						ftp.mkdir("/jsops/" + versionDate);
						ftp.mkdir("/jsops/" + versionDate + "/" + componentId);
						ftp.mkdir("/jsops/" + versionDate + "/" + componentId+"/"+random);
						ftp.put("/jsops/" + versionDate + "/" + componentId +"/"+random + "/" + file.getOriginalFilename(), path,
								true);
					}

				}

			}
			
			HashMap<String, Boolean> map = checkFileName(fileNames);
			
			if(!map.get("zip")){
				response.addProperty("returnCode", 1);
				response.addProperty("message", "外发包需使用.zip文件");
				return response.toString();
			}
			
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			status = transactionManager.getTransaction(def);

			String id = UUID.randomUUID().toString().replace("-", "");
			packagesDao.addPackage(id, componentId, versionDate, principal, releaseNote, map.get("sql").toString(),
					map.get("properties").toString(), map.get("zip").toString(), remark,"/jsops/" + versionDate + "/" + componentId+"/"+random+"/");
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "上传成功");
			transactionManager.commit(status);
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			transactionManager.rollback(status);
			logger.error(e);
			response.addProperty("returnCode", 1);
			response.addProperty("message", "上传异常");
			return response.toString();
		}
	}

	@Override
	public String queryPackageList(int pageIndex, int pageSize, String componentId, String versionDate,
			String principal, String hasDbScript, String hasParaFile) {
		JsonObject response = new JsonObject();
		try {
			HashMap<String, Object> packagesCounts = packagesDao.queryPackagesCounts(componentId,versionDate,principal,hasDbScript,hasParaFile);
			if(pageSize==0){
				pageSize=Integer.parseInt(packagesCounts.get("PACKAGECOUNTS").toString());
			}
			List<Map<String, Object>> packages = packagesDao.queryPackageList(pageIndex*pageSize, pageSize, componentId,versionDate,principal,hasDbScript,hasParaFile);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			response.addProperty("packageCounts", packagesCounts.get("PACKAGECOUNTS").toString());
			
			JsonArray result = new JsonArray();
			
			for (Map<String, Object> map : packages) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", map.get("ID").toString());
				jsonObject.addProperty("componentId", map.get("COMPONENTTYPE").toString());
				jsonObject.addProperty("versionDate", map.get("VERSION").toString());
				jsonObject.addProperty("principal", map.get("PRINCIPAL").toString());
				jsonObject.addProperty("hasDbScript", "true".equals(map.get("DB_SCRIPT").toString())?"有":"无");
				jsonObject.addProperty("hasParaFile", "true".equals(map.get("PARA_FILE").toString())?"有":"无");
				jsonObject.addProperty("createTime", map.get("CREATE_TIME").toString());
				jsonObject.addProperty("remark", map.get("REMARK")==null?"":map.get("REMARK").toString());
				jsonObject.addProperty("keep_path", map.get("KEEP_PATH")==null?"":map.get("KEEP_PATH").toString());
				result.add(jsonObject);
			}
			
			response.add("result", result);
			
			return response.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			response.addProperty("returnCode", 1);
			response.addProperty("message", "查询异常");
			return response.toString();
		}
	}

	public HashMap<String, Boolean> checkFileName(ArrayList<String> fileNames) {
		HashMap<String, Boolean> map = new HashMap<>();
		map.put("zip", false);
		map.put("sql", false);
		map.put("properties", false);
		for (String name : fileNames) {
			if (name.endsWith(".zip")) {
				map.put("zip", true);
			}
			if (name.endsWith(".sql")) {
				map.put("sql", true);
			}
			if (name.endsWith(".properties")) {
				map.put("properties", true);
			}
		}
		return map;
	}
	
}
