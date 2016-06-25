package com.sap.alexa;

import java.io.Serializable;
import java.util.List;

import com.sap.alexa.shared.EntityContainer;

@SuppressWarnings("serial")
public class DataCache<K> implements Serializable {

	private EntityContainer<K> entityContainer;
	private transient List<K> workingSet;
	private int index = 0;
	
	public DataCache() {}

	public DataCache(EntityContainer<K> entityContainer) {
		this.entityContainer = entityContainer;
		if (entityContainer != null && entityContainer.getEntities() != null) {
			this.workingSet = this.entityContainer.getEntities();
		}
	}
	
	public void setEntityContainer(EntityContainer<K> entityContainer) {
		this.entityContainer = entityContainer;
	}

	public EntityContainer<K> getEntityContainer() {
		return entityContainer;
	}

	public void addEntityContainer(EntityContainer<K> entityContainer) {
		if (this.entityContainer == null) {
			this.entityContainer = entityContainer;
		} else {
			this.entityContainer.getEntities().addAll(entityContainer.getEntities());
		}
		this.workingSet = entityContainer.getEntities();
		++index;
	}
	
	public void setWorkingSet(List<K> workingSet) {
		this.workingSet = workingSet;
	}

	public List<K> getWorkingSet() {
		return workingSet;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public int skip() {
		return (index + 1) * 5;
	}

	public boolean hasMore() {
		return skip() < this.entityContainer.getCount() ? true : false;
	}
}
