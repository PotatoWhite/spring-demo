package me.potato.ribbonrestclient.service.proxy.simple;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

@Data
@JsonDeserialize(as = Simple.class)
public class Simple {

	private Long id;

	private String dataString;
	private Integer dataInteger;


}
