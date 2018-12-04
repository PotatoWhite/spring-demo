package me.potato.demo.simplerestclient.simple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class SimpleProxyController {

	@Autowired
	SimpleService simpleService;


	@GetMapping("/proxy/simples")
	public Collection getSimples(){
		return simpleService.getSimples();
	}
}
