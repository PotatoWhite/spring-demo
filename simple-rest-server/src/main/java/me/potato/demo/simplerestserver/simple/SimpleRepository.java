package me.potato.demo.simplerestserver.simple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SimpleRepository extends JpaRepository<Simple, Long> {
	@Query(nativeQuery = true, value = "select * from SIMPLE where id = :id")
	Simple selectOne(@Param("id") Long id);

	Optional<Simple> findByDataString(String dataString);
}
