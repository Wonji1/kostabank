package com.my.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice(assignableTypes = com.my.controller.BoardController.class)
public class BoardControllerAdviser {
	@ExceptionHandler
	@ResponseBody
	public Object except(Exception e) {
		Map<String, Object> map = new HashMap<>();
		e.printStackTrace();
		map.put("status",-1);
		map.put("msg", e.getMessage());
		return map;
	}
}
