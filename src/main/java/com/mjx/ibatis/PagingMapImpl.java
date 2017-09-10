package com.mjx.ibatis;

import com.mjx.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

/***
 * 分页对象实现类
 * 
 * currPage：当前页码，通过startIndex和sizePerPage算出来的，不能set；
 * pageCount：总页数，通过totalSize和sizePerPage算出来的，不能set； totalSize：总条数，后台查询数据后设置；
 * sizePerPage：每页显示的数据条数，前台设置； startIndex：开始于第几条，前台设置
 * 
 * sortedByKey：排序条件设置，查询结果中的一个属性名； sortedByDir： 排序顺序，升序还是降序;
 * 
 * 查询条件对象通过构造函数传入
 * 
 * @author zhangyi
 * 
 * @param <T>
 */
public class PagingMapImpl<T> extends HashMap<String, Object> implements IPaging<T> {

	private static final long serialVersionUID = -8016509433592827315L;
	private static final Logger logger = LoggerFactory.getLogger(PagingMapImpl.class);

	private boolean smartClient=false;

	private static final String DATA = "data";

	private static final String LIMIT = "limit";

	private static final String TOTAL_SIZE = "totalSize";

	private static final String SIZE_PER_PAGE = "sizePerPage";

	private static final String START_INDEX = "startIndex";

	private static final String STATEMENT_PREFIX = "statementPrefix";

	private static final String CURR_PAGE ="currPage";
	private static final String PAGE_COUNT ="pageCount";
	private static List<String> ALL_INNER_KEY = null;

	static {
		ALL_INNER_KEY = new ArrayList<String>(9);
		ALL_INNER_KEY.add(DATA);
		ALL_INNER_KEY.add(LIMIT);
		ALL_INNER_KEY.add(TOTAL_SIZE);
		ALL_INNER_KEY.add(SIZE_PER_PAGE);
		ALL_INNER_KEY.add(START_INDEX);
		ALL_INNER_KEY.add(STATEMENT_PREFIX);
	}

	/**
	 * 查询条件为空
	 */
	public PagingMapImpl() {
		this(null, 0);// 默认页大小0

	}

	/**
	 * 构造函数 默认页大小0 表示不分页
	 * 
	 * @param t
	 *            查询条件对象
	 */
	public PagingMapImpl(Object t) {
		this(t, 0);// 默认页大小0

	}

	/***
	 * 构造函数
	 * 
	 * @param t
	 *            查询条件对象
	 * @param sizePerPage
	 *            每页行大小
	 */
	public PagingMapImpl(Object t, int sizePerPage) {
		super();
		this.put(DATA, new java.util.ArrayList<T>());
		this.put(TOTAL_SIZE, 0);
		this.put(SIZE_PER_PAGE, sizePerPage);
		this.put(START_INDEX, 0);

		setValues(t);
	}

	private void setValues(Object t) {
		if (t == null) {
			return;
		}

		HashMap<String, Method> methodMap = ReflectUtil.getAllValueGetMethod(t.getClass());

		String name = null;
		Object ret = null;
		for (Iterator<String> it = methodMap.keySet().iterator(); it.hasNext();) {
			name = it.next();
			if (ALL_INNER_KEY.contains(name)) {
				throw new RuntimeException("对象属性" + name + "与分页对象属性重名，不可用.data,limit,totalSize,sizePerPage,startIndex,sortedByKey,sortedByDir为分页保留属性.");
			}

			try {
				ret = methodMap.get(name).invoke(t, null);
			} catch (Exception e) {
				throw new RuntimeException("设置参数" + name + "发生错误" + e);

			}

			this.put(name, ret);

			name = null;
			ret = null;
		}

	}

	/***
	 * 取得分页数据
	 */
	public Collection<T> getData() {
		return (Collection<T>) this.get(DATA);
	}

	/***
	 * 设置分页数据
	 * 
	 * @param data
	 */
	public void setData(Collection<T> data) {
		this.put(DATA, data);
	}

	/***
	 * 添加数据
	 * 
	 * @param t
	 */
	final public void addData(T t) {
		((Collection<T>) this.get(DATA)).add(t);

	}

	/***
	 * 添加数据
	 * 
	 * @param t
	 */
	final public void addDatas(T... t) {
		for (T obj : t) {
			((Collection<T>) this.get(DATA)).add(obj);
		}
	}

	/***
	 * 取得当前页
	 */
	final public int getCurrPage() {
		return (Integer)this.get(CURR_PAGE);
	}

	/***
	 * 取得总页数
	 */
	final public int getPageCount() {
		return (Integer)this.get(PAGE_COUNT);
	}

	/***
	 * 取得数据总行数
	 */
	final public int getTotalSize() {
		return (Integer) this.get(TOTAL_SIZE);
	}

	/***
	 * 设置总行数
	 * 
	 * @param totalSize
	 */
	final public void setTotalSize(int totalSize) {
		this.put(TOTAL_SIZE, totalSize);
	}

	/***
	 * 取得每页行数
	 */
	final public int getSizePerPage() {
		return (Integer) this.get(SIZE_PER_PAGE);
	}

	/***
	 * 设置每页行数
	 * 
	 * @param sizePerPage
	 */
	final public void setSizePerPage(int sizePerPage) {
		this.put(SIZE_PER_PAGE, sizePerPage);
	}

	final public int getStartIndex() {
		return (Integer) this.get(START_INDEX);
	}

	final public void setStartIndex(int startIndex) {
		this.put(START_INDEX, startIndex);
	}

	final public int getLimit() {
		return (Integer) this.get(LIMIT);
	}

	final public void setLimit(int limit) {
		this.put(LIMIT, limit);
	}

	public String toString() {
		return super.toString();
	}

	final public boolean getSmartClient() {
		return smartClient;
	}

	final public void setSmartClient(boolean smartClient) {
		this.smartClient = smartClient;
	}

	public void computePage() {
		int currPage=0;
		int pageCount=0;
		if (this.getTotalSize() == 0 && this.getSizePerPage() == 0) {
			currPage= 0;
		} else if (this.getSizePerPage() == 0) {
			currPage= 1;
		} else {
			currPage= this.getStartIndex() == 0 ? 1 : this.getStartIndex()
					/ this.getSizePerPage()+1;
		}
		if (this.getTotalSize() == 0 && this.getSizePerPage() == 0) {
			pageCount= 0;
		} else {
			pageCount= this.getSizePerPage() == 0 ? 1 : this.getTotalSize()
					% this.getSizePerPage() == 0 ? this.getTotalSize()
					/ this.getSizePerPage() : this.getTotalSize()
					/ this.getSizePerPage() + 1;
		}
		
		// TODO Auto-generated method stub
		this.put(CURR_PAGE, currPage);
		
		this.put(PAGE_COUNT, pageCount);
		
		
		System.out.println("--------------------------------------"+currPage+"::"+pageCount);
	}
	
	
	
}
