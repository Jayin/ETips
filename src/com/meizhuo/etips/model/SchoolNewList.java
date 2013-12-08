package com.meizhuo.etips.model;


import java.util.ArrayList;
import java.util.List;

public class SchoolNewList extends SchoolNews {
	private static ArrayList<SchoolNews> list;
	public static ArrayList<SchoolNews> getInstance(List<SchoolNews> _list) {
		return  list = new ArrayList<SchoolNews>(_list);
	}
}
