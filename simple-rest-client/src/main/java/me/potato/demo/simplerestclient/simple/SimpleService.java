package me.potato.demo.simplerestclient.simple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

@Service
@RibbonClient(name = "simple-proxy")
public class SimpleService {

	@LoadBalanced
	@Bean
	RestTemplate restTemplate(){
		return new RestTemplate();
	}

	@Autowired
	RestTemplate restTemplate;


	public Collection getSimples(){
		return restTemplate.getForObject("http://simple-proxy/api/simples", Collection.class);
	}
}
