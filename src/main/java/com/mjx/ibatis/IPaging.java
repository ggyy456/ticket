package com.mjx.ibatis;

import java.util.Collection;


/***
 * 分页对象接口
 * 
 * 
 * @author zhangyi
 *
 * @param <T>  对象
 */
public interface IPaging<T> {
	
	/***
	 * 取得分页数据
	 * @return    Collection<对象>     
	 */
	public Collection<T> getData() ;
	
	public void setData(Collection<T> data);

	/***
	 * 取得当前页码
	 * 
	 * @return
	 */
	public int getCurrPage() ;

	/***
	 * 取得总页数
	 * 
	 * @return
	 */
	public int getPageCount() ;

	/***
	 * 取得总行数
	 * 
	 * @return
	 */
	public int getTotalSize();
	
	public void setTotalSize(int totalSize);
	/***
	 * 取得每页行数
	 * @return
	 */
	public int getSizePerPage();
	
	public void setSizePerPage(int sizePerPage);
	/**
	 * 获取起始索引
	 * 
	 */
	public int getStartIndex();
	
	public void setStartIndex(int startIndex);

	public int getLimit();

	public void setLimit(int limit);

	public void computePage();
}
