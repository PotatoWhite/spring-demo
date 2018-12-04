package me.potato.demo.simplerestserver.simple;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Simple {

	@Id
	@GeneratedValue
	private Long id;

	private String  dataString;
	private Integer dataInteger;

}
