package com.teaminology.hp.web.utils;

public class Pair<K,V> {

	 K key;
	 V value;

	public  Pair(K key, V value){
		this.key = key;
		this.value = value;
	 }

	 
	public String toString() {
		return String.format("%n %s = %s ", key==null?"NUll":key, value==null?"NULL":value);

	}
}
