package com.jieshun.ops.util;

import java.util.List;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;

public class OSSUtil {

	private String endpoint = "";
	private String accessKeyId = "";
	private String accessKeySecret = "";
	private String bucketName = "";
	private OSSClient ossClient = null;
	
	public String getBucketName() {
		return bucketName;
	}
	
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public OSSClient getOssClient() {
		return ossClient;
	}
	
	public void init(){
		ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
	}

	public void setOssClient(OSSClient ossClient) {
		this.ossClient = ossClient;
	}
	
	public int ListOSSCounts(String prefix){
		final int maxKeys = 1000;
		String nextMarker = null;
		ObjectListing objectListing=null;
		int counts=0;
		do {
			objectListing = ossClient.listObjects(new ListObjectsRequest(bucketName).withPrefix(prefix).withMarker(nextMarker).withMaxKeys(maxKeys));
		    List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
		   /* for (OSSObjectSummary s : sums) {
		        System.out.println("\t" + s.getKey());
		    }*/
		    nextMarker = objectListing.getNextMarker();
		    counts += sums.size();
		} while (objectListing.isTruncated());
		return counts;
	}
	
	public void shutdown(){
		if(ossClient!=null){
			ossClient.shutdown();
		}
	}

}