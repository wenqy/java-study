package com.wenqy.common.vo;

import java.util.ArrayList;
import java.util.List;

public class Foo {
	public String name;
	public List<Bar> bars = new ArrayList<>();

    public Foo(String name) {
        this.name = name;
    }
}