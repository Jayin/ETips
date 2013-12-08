package com.meizhuo.etips.db;

import java.util.List;

import android.content.ContentValues;
import android.test.AndroidTestCase;

import com.meizhuo.etips.model.MsgRecord;

public class MyTestM extends AndroidTestCase {

	public static void main(String[] args) {

	}

	public void add() {
		MsgCenterDAO dao = new MsgCenterDAO(getContext());
	for(int i=0;i<=10;i++){
		
	
		ContentValues cv = new ContentValues();
		cv.put("id", i);
		cv.put("content","lo22222");
		cv.put("type", "notify	");
		cv.put("addTime", "12312312312");
		MsgRecord m = new MsgRecord();
		m.id = 3;
		m.content = "lo22222";
		m.type = "notify	";
		m.addTime = "12312312312";
	//	System.out.println("--->"+dao.add(MsgCenterDAO.getContentValues(m)));
		System.out.println("--->"+dao.add(cv));
	}
	}
	public void update(){
		MsgCenterDAO dao = new MsgCenterDAO(getContext());
		MsgRecord m = new MsgRecord();
		m.id = 56;
		m.content = "lo22222";
		m.type = "notify	";
		m.addTime = "12312312312";
		ContentValues cv =dao.getContentValues(m);
		System.out.println("--->"+dao.update(cv, "id = ? and addTime = ?", new String[]{"3","12312312312"}));
	}
	
	public void delete(){
		MsgCenterDAO dao = new MsgCenterDAO(getContext());
		System.out.println("--->"+dao.delete("id = ?",new String[]{"56"}));
		
	}
	public void query(){
	 
		MsgCenterDAO dao = new MsgCenterDAO(getContext());
//		List<MsgRecord> list =dao.query("addTime = ?", new String[]{"12312312312"});
//		System.out.println("--->"+list.toString());
		List<MsgRecord> list  = dao.queryAll();
		System.out.println("--->"+list.toString());
	}
	
	public void deleteAll(){
		MsgCenterDAO dao = new MsgCenterDAO(getContext());
		System.out.println("-->"+dao.deleteAll());
	}
	
	

}
