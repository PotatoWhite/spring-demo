package me.potato.ribbonrestclient.service.proxy.simple;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Log
@Service
@RibbonClient("simple-rest-server")
public class SimpleProxyService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;


	public Collection<Simple> getAllSimples(Pageable pageable) {

		String url = "http://simple-rest-server/v2/api/simples";

		Optional<PagedResources<Simple>> response = getFromExtService(url,pageable);

		if (response.isPresent()) {
			PagedResources pagedResources = response.get();
			log.info(pagedResources.toString());
			return pagedResources.getContent();
		}


		return Collections.emptyList();
	}


	private Optional<PagedResources<Simple>> getFromExtService(String url, Pageable page) {

		URI targetUrl= UriComponentsBuilder.fromUriString("http://simple-rest-server")
				.path("/v2/api/simples")
				.queryParam("page", page.getPageNumber())
				.queryParam("size", page.getPageSize())
				.build()
				.toUri();

		log.info(targetUrl.toString());

		ResponseEntity<PagedResources<Simple>> result = restTemplate.exchange(targetUrl, HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Simple>>() {
		});
		if (result.getStatusCode().is2xxSuccessful())
			return Optional.of(result.getBody());

		return Optional.empty();
	}

}