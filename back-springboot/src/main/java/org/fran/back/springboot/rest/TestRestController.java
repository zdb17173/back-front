package org.fran.back.springboot.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.fran.back.springboot.config.aspect.LogAction;
import org.fran.back.springboot.config.swagger.Swagger;
import org.fran.back.springboot.security.UserService;
import org.fran.back.springboot.vo.JsonResult;
import org.fran.back.springboot.vo.RemoveConfigParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/test")
public class TestRestController {
	@Autowired
	private UserService userService;
	@Autowired
	private Swagger swagger;

	static Logger log = LoggerFactory.getLogger(TestRestController.class);

	@GetMapping(value = "/checkLogin", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	public JsonResult<String> checkLogin(){

		UserService.CustomUser user = userService.getCurUser();

		JsonResult<String> res = new JsonResult<>();
		res.setData(user == null ? "none user" : "hello: " + user.getUsername() + "!!");
		res.setDescription(swagger.getHost() + "sahdjhsajdhjsahdjsajdjh");
		res.setStatus(200);
		return res;
	}


	@PostMapping(value = "/selectString", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult<String> firstTest(@RequestBody RemoveConfigParam baseParam){
		log.info("firstTest");
		JsonResult<String> res = new JsonResult<>();
		res.setData("dsadsad");
		res.setDescription("sahdjhsajdhjsahdjsajdjh");
		res.setStatus(200);
		return res;
	}

	@LogAction(name ="selectList")
	@GetMapping(value = "/selectList", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	public JsonResult<List<String>> selectTest(@RequestParam(name="id") int ids){
		log.info("selectAll ids:["+ ids +"]");
		if(ids == 5)
			throw new RuntimeException("error!!!!");

		JsonResult<List<String>> res = new JsonResult<>();
		res.setData(new ArrayList<String>(){
			{
				add("sadsda1");
				add("sadsda2");
				add("sadsda3");
				add("sadsda4");
				add("sadsda5");
			}
		});
		res.setDescription("sahdjhsajdhjsahdjsajdjh");
		res.setStatus(200);
		return res;
	}

	@PostMapping(value = "/upload",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult upload(
			@RequestPart("uploadFile")MultipartFile uploadFile,
		    @RequestPart("description")String description,
			@RequestPart("name")String name){
		FileOutputStream o = null;
		InputStream inputStream = null;
		try {
			inputStream = uploadFile.getInputStream();
			File f = new File("C:\\temp\\aa.jpg");
			o = new FileOutputStream(f);
			byte[] b = new byte[1024];
			while(inputStream.read(b)!= -1){
				o.write(b);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(o!= null)
					o.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(inputStream!= null)
					inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		JsonResult res = new JsonResult();
		res.setData("success");
		res.setDescription("success");
		res.setStatus(200);
		return res;
	}
}
