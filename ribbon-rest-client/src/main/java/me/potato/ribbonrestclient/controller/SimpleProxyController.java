package me.potato.ribbonrestclient.controller;

import lombok.extern.java.Log;
import me.potato.ribbonrestclient.service.proxy.simple.Simple;
import me.potato.ribbonrestclient.service.proxy.simple.SimpleProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collection;

@Log
@RestController
@RequestMapping("/proxy")
public class SimpleProxyController {

	@Autowired
	SimpleProxyService simpleProxyService;

	@GetMapping("/simples")
	public Collection<Simple> getAllSimples(Pageable pageable) throws IOException {

		Collection<Simple> allSimples = simpleProxyService.getAllSimples(pageable);
		allSimples.forEach(simple -> log.info(simple.toString()));
		return allSimples;

	}
}
