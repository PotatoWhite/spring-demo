package me.potato.demo.simplerestclient.simple;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Simple {
	private Long id;

	private String  dataString;
	private Integer dataInteger;

}
