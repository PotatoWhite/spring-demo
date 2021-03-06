package me.potato.ribbonhardcodeclient.service.proxy.simple;

import lombok.extern.slf4j.Slf4j;
import me.potato.ribbonhardcodeclient.service.proxy.util.RestResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
public class SimpleClient {

	@Autowired
	private RestTemplate restTemplate;

	public Collection<Simple> getAllSimples(Pageable page) {

		Optional<PagedSimpleResponse> response = getFromExtService(page);

		if (response.isPresent()) {
			RestResponsePage<Simple> pagedResources = response.get();
			return pagedResources.getContent();
		}

		return Collections.emptyList();
	}

	private Optional<PagedSimpleResponse> getFromExtService(Pageable page) throws RestClientException {

		String target = UriComponentsBuilder.fromUriString("http://simple-rest-server/v2/api/simples")
				.queryParam("page", page.getPageNumber())
				.queryParam("size", page.getPageSize())
				.toUriString();


		try {
			ResponseEntity<PagedSimpleResponse> response = restTemplate.getForEntity(target, PagedSimpleResponse.class);
			if (response.getStatusCode().is2xxSuccessful())
				return Optional.of(response.getBody());
		} catch (RestClientException ex) {
			log.error("an error occur : " + ex.getMessage());
			throw ex;
		}

		return Optional.empty();
	}


}