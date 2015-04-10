package com.source.rest.impl;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.sun.xml.txw2.annotation.XmlElement;


@XmlRootElement(name = "responseList")
public class LocationList {

	
	private List<Object> url;
    
	@XmlElement(value="link")
    public List<Object> getList() {
        return url;
    }

    public void setList(List<Object> list) {
        this.url = list;
    }

}