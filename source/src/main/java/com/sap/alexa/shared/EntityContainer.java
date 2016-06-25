package com.sap.alexa.shared;

import java.beans.Transient;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityContainer<K> implements Serializable{
	private int count;
	private List<K> entities;
	
	public EntityContainer() {
	}
	
	public EntityContainer(List<K> entities, int count){
		this.entities = entities;
		this.count = count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}
	
	public void setEntities(List<K> entities) {
		this.entities = entities;
	}

	public List<K> getEntities() {
		return entities;
	}	
	
	public boolean isEmpty(){
		if(this.entities == null){
			return true;
		}
		
		return this.entities.isEmpty();
	}
}
