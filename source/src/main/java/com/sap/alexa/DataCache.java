package com.sap.alexa;

import java.io.Serializable;
import java.util.List;

import com.sap.alexa.shared.EntityContainer;

@SuppressWarnings("serial")
public class DataCache<K> implements Serializable {

	private EntityContainer<K> entityContainer;
	private transient List<K> workingSet;
	private int index = 0;
	
	public DataCache(EntityContainer<K> entityContainer) {
		this.entityContainer = entityContainer;
		this.workingSet = this.entityContainer.getEntities();
	}

	public EntityContainer<K> getEntityContainer() {
		return entityContainer;
	}

	public void addEntityContainer(EntityContainer<K> entityContainer) {
		if (this.entityContainer == null) {
			this.entityContainer = entityContainer;
		}else{
			this.entityContainer.getEntities().addAll(entityContainer.getEntities());
		}
		this.workingSet = entityContainer.getEntities();
		++index;
	}
	
	public List<K> getWorkingSet() {
		return workingSet;
	}
		
	public int index(){
		return index;
	}

	public int skip() {
		int skip = index * 5;
		return skip == 0 ? 5 : skip;
	}
	
	public K find(){
		return null;
	}
	
	public boolean hasMore(){
		return skip() < this.entityContainer.getCount()? true : false;
	}
}
