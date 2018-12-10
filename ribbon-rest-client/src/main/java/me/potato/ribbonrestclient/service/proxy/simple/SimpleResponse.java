package me.potato.ribbonrestclient.service.proxy.simple;

import lombok.Getter;
import lombok.Setter;

import java.awt.print.Pageable;
import java.util.List;

@Getter
@Setter
public class SimpleResponse {
	Pageable pageable;
	List<Simple> contents;
}
