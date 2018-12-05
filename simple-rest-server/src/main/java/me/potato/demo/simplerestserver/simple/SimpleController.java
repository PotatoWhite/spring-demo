package me.potato.demo.simplerestserver.simple;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Log
@RestController
@RequestMapping("/v1")
public class SimpleController {

	@Autowired
	private SimpleRepository simpleRepository;


	@GetMapping("/api/simples")
	public List<Simple> getAllSimples() {
		return simpleRepository.findAll();
	}


	@GetMapping("/api/simples/{id}")
	public Simple getSimple(@PathVariable Long id) {
		Optional<Simple> byId = simpleRepository.findById(id);
		return byId.orElse(null);
 	}

	@PostMapping("/api/simples")
	public Simple createSimple(@RequestBody Simple simple) {
		return simpleRepository.save(simple);
	}

	@PutMapping("/api/simples")
	public Simple patchSimple(@RequestBody Simple simple) {
		return simpleRepository.save(simple);
	}

	@DeleteMapping("/api/simples/{id}")
	public void deleteSimple(@PathVariable Long id) {
		simpleRepository.deleteById(id);
	}
}
