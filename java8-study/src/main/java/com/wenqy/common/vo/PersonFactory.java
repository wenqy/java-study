package com.wenqy.common.vo;

public interface PersonFactory<P extends Person> {
	
    P create(String firstName, String lastName);
}