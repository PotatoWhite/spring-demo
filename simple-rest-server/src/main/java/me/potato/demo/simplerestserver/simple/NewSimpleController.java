package me.potato.demo.simplerestserver.simple;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@Log
@RestController
@RequestMapping("/v2")
public class NewSimpleController {

	@Autowired
	private SimpleRepository simpleRepository;


	@GetMapping("/api/simples")
	public Page<Simple> getAllSimples(Pageable pageable) {
		return simpleRepository.findAll(pageable);

	}

	@GetMapping("/api/simples/{id}")
	public ResponseEntity getSimple(@PathVariable Long id) {
		Optional<Simple> byId = simpleRepository.findById(id);

		if (byId.isPresent())
			return ok().body(byId.get());
		else
			return ResponseEntity.noContent().build();
	}

	@PostMapping("/api/simples")
	public ResponseEntity createSimple(@RequestBody Simple simple) {
			return ResponseEntity.status(HttpStatus.CREATED).body(simpleRepository.saveAndFlush(simple));
	}

	@PatchMapping("/api/simples")
	public ResponseEntity patchSimple(@RequestBody Simple simple) {

		Optional<Simple> byId = simpleRepository.findById(simple.getId());
		if (byId.isPresent())
			return ResponseEntity.status(HttpStatus.OK).body(simpleRepository.saveAndFlush(simple));
		else
			return ResponseEntity.status(HttpStatus.CREATED).body(simpleRepository.saveAndFlush(simple));

	}

	@DeleteMapping("/api/simples/{id}")
	public ResponseEntity deleteSimple(@PathVariable Long id) {

		Optional<Simple> byId = simpleRepository.findById(id);

		if (byId.isPresent()) {
			simpleRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).build();
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}

	}
}

