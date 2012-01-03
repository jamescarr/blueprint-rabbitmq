package com.carfax.blueprint.amqp;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class CacheQueue implements Queue<UnsentMessage> {
	private Cache cache;
	public void setCache(Cache cache) {
		this.cache = cache;
	}


	public void clear() {
		// TODO Auto-generated method stub
		
	}

	public boolean contains(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean containsAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEmpty() {
		return cache.getSize() > 0;
	}


	public boolean remove(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean removeAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean retainAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public int size() {
		// TODO Auto-generated method stub
		return cache.getSize();
	}

	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T[] toArray(T[] arg0) {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean addAll(Collection<? extends UnsentMessage> arg0) {
		// TODO Auto-generated method stub
		return false;
	}


	public Iterator<UnsentMessage> iterator() {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean add(UnsentMessage unsentMessage) {
		cache.put(new Element(unsentMessage.getMessage().getMessageProperties().getMessageId(), unsentMessage));
		return true;
	}


	public UnsentMessage element() {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean offer(UnsentMessage arg0) {
		// TODO Auto-generated method stub
		return false;
	}


	public UnsentMessage peek() {
		// TODO Auto-generated method stub
		return null;
	}


	public UnsentMessage poll() {
		if(cache.getKeys().size() > 0){
			UnsentMessage objectValue = (UnsentMessage) cache.removeAndReturnElement(cache.getKeys().get(0)).getObjectValue();
			return objectValue;			
		}
		return null;
	}


	public UnsentMessage remove() {
		// TODO Auto-generated method stub
		return null;
	}


}
