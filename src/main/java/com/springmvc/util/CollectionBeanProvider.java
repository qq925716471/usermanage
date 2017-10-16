package com.springmvc.util;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public abstract class CollectionBeanProvider<R> {
	
	private SqlRowSet rs = null;

	public CollectionBeanProvider(SqlRowSet rs) {
		super();
		this.rs = rs;
	}
	
	public boolean next(){
		if(rs == null)return false;
		return rs.next();
	}
	
    public  R getBean(){
    	return getBean(this.rs);
    }

	protected abstract R getBean(SqlRowSet rs2) ;

}
