package org.fran.back.springboot.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.fran.back.springboot.vo.JsonResult;
import org.fran.back.springboot.vo.RemoveConfigParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/test")
public class TestRestController {
	
	@PostMapping(value = "/firstTest", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult<String> firstTest(@RequestBody RemoveConfigParam baseParam){
		
		JsonResult<String> res = new JsonResult<>();
		res.setData("dsadsad");
		res.setDescription("sahdjhsajdhjsahdjsajdjh");
		res.setStatus(200);
		return res;
	}
	
	@GetMapping(value = "/selectAll", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	public JsonResult<List<String>> selectTest(@RequestParam(name="id") int ids){

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
