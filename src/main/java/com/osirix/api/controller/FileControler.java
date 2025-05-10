package com.osirix.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.osirix.api.minio.AppFileType;
import com.osirix.api.minio.MinioServiceImpl;

import io.minio.http.Method;

@RestController
@RequestMapping("/api/v1") 
public class FileControler {
	
	@Autowired
	MinioServiceImpl minioService;
	
	@GetMapping("/app")
	@PreAuthorize("authentication.principal.user instanceof T(com.osirix.api.entity.User)")
	public String getMethodName() {
		try {
			return minioService.getPresignedUrlForObject("1", AppFileType.FILES, "cpu-z_2.15-en.zip", Method.GET, 1, "GET");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	
	

}
