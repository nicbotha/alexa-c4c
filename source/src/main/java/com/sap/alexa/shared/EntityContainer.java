package com.sap.alexa.shared;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class EntityContainer<K> implements Serializable{
	private final int count;
	private final List<K> entities;
	
	public EntityContainer(List<K> entities, int count){
		this.entities = entities;
		this.count = count;
	}

	public int getCount() {
		return count;
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
