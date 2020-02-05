package com.wenqy.common.vo;

import com.wenqy.annotation.Hint;

/**
 * @Hint 可重复注解（新方法）
 * 使用注解容器（老方法）: @Hints({@Hint("hint1"), @Hint("hint2")})
 * @version V5.0
 * @author wenqy
 * @date   2020年1月16日
 */
@Hint("hint1")
@Hint("hint2")
//@Hints({@Hint("hint1"), @Hint("hint2")})
public class Person {
	
    String firstName;
    String lastName;
    int age;
    
    public Person() {}

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Person(String name, int age) {
        this.firstName = name;
        this.age = age;
    }
    
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Person [firstName=" + firstName + ", lastName=" + lastName + ", age=" + age + "]";
	}
    
}