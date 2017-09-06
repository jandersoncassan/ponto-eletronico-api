package com.br.jande.pontoeletronico.response;

import java.util.ArrayList;
import java.util.List;

public class Response<T> {

	private T data;
	private List<String> errors;
	
	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}
	
	/**
	 * @param data the data to set
	 */
	public void setData(T data) {
		this.data = data;
	}
	
	/**
	 * @return the errors
	 */
	public List<String> getErrors() {
		if(null == errors) {
			this.errors =  new ArrayList<String>();
		}
		return errors;
	}
	
	/**
	 * @param errors the errors to set
	 */
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
}
