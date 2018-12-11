package me.potato.ribbonhardcodeclient.controller;

import lombok.extern.java.Log;
import me.potato.ribbonhardcodeclient.service.proxy.simple.Simple;
import me.potato.ribbonhardcodeclient.service.proxy.simple.SimpleClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@Log
@RestController
@RequestMapping("/proxy")
public class SimpleProxyController {

	@Autowired
	private SimpleClient simpleClient;

	@GetMapping("/simples")
	public Collection<Simple> getAllSimples(Pageable pageable) throws Exception {

		Collection<Simple> allSimples = simpleClient.getAllSimples(pageable);
		return allSimples;

	}
}
