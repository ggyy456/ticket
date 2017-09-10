package com.mjx.ibatis;


import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

/**
 * 抽象分页查询Action 已经在框架环境中调用
 * 
 * 可通过spring配置直接使用 当不满足要求时，可继承并重写回调方法， 使得查询前在model中加入其它用于查询的信息 在 struts 中 result
 * type="json" param name="root" model param
 * 
 * @author zhangyi
 * 
 */
public class AbstractPagingAction extends ActionSupport implements ModelDriven {

	protected static Logger LOG = LoggerFactory.getLogger(AbstractPagingAction.class);

	private AppService appService;

	private String modelClassName;

	private Object model;

	/**
	 * 自定义查询分页的statement前缀
	 * 
	 * 
	 *添加判断 如果是paging对象是PagingMapImpl类的对象，查看对象是否有statementPrefix属性
	 * statementPrefix 是分页查询的前缀 这样一个sqlMap中就可以写多个分页查询。 前缀的定义要以getPage开头
	 * 如前缀定义成getPageCp 那么框架回去找getPageCpTotal这个statement去查询总记录数 执行getPageCp
	 * 去查询分页记录
	 */
	private String statementPrefix;

	/***
	 * @param obj
	 */
	public void setModel(Object obj) {
		this.model = obj;
	}

	/***
	 * @return model;
	 */
	public Object getModel() {
		return model;
	}

	public String getStatementPrefix() {
		return statementPrefix;
	}

	public void setStatementPrefix(String statementPrefix) {
		this.statementPrefix = statementPrefix;
	}

	/**
	 * 设置model类型
	 * 
	 * @param modelClassName
	 */
	public void setModelClassName(String modelClassName) {
		this.modelClassName = modelClassName;

		try {
			if (modelClassName != null && !"".equals(modelClassName)) {
				setModel(Class.forName(modelClassName).newInstance());
			}
		} catch (Exception e) {
			throw new RuntimeException("modelClassName : " + modelClassName + "配置DTO发生异常");
		}

	}

	/**
	 * 可回调此方法在查询前修改model信息 如在model中加入session信息
	 * 
	 * @param model
	 */
	protected void queryBeforeModelModifyCallBack(Object model) {

	}

	/**
	 * 可回调此方法在查询后修改model信息
	 * 
	 * @param model
	 */
	protected void queryAfterModelModifyCallBack(Object model) {
		
	}

	/**
	 * 可回调此方法在查询后修改model信息
	 * 
	 * @param model
	 */
	protected void queryAfterModelModifyCallBack(Object model,Object param) {
		queryAfterModelModifyCallBack(model);
	}
	
	public void setAppService(AppService appService) {
		this.appService = appService;
	}

	protected void setParamISC(PagingMapImpl pagingMapImpl) {
		Map parameters = ActionContext.getContext().getParameters();

		String[] sizePerPage = parameters.get("sizePerPage") == null ? null
				: (String[]) parameters.get("sizePerPage");
		
		String[] startIndex = parameters.get("startIndex") == null ? null
				: (String[]) parameters.get("startIndex");

		try {
			pagingMapImpl.setSizePerPage(Integer.valueOf(sizePerPage == null ? "0" : sizePerPage[0].toString()));
	
			if (startIndex != null && startIndex.length != 0) {
				pagingMapImpl.setStartIndex(Integer.valueOf(startIndex[0].toString()));
			} else {
				pagingMapImpl.setStartIndex(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("分页错误");

		}
	}

	@Override
	public String execute() throws Exception {
		Map parameters = ActionContext.getContext().getParameters();

		queryBeforeModelModifyCallBack(getModel());

		PagingMapImpl paging =  new PagingMapImpl(getModel());

		boolean smartClient = parameters.get("smartClient") == null ? false : Boolean.valueOf(((String[]) parameters.get("smartClient"))[0]);

		if (smartClient){
			paging.setSmartClient(true);
			setParamISC((PagingMapImpl) paging);
		}

		/**
		 * 如果类属性statementPrefix
		 * 有定义，那么会将statementPrefix的值通过page对象专递到PagingAppServiceImpl
		 * 由PagingAppServiceImpl 自定义分页statement去执行。
		 */
		if (statementPrefix != null) {
			((PagingMapImpl) paging).put("statementPrefix", statementPrefix);
		}
		
		IPaging returnPaging = (IPaging) this.appService.doService(paging);
		
		if (returnPaging != null) {
			setModel((PagingMapImpl) returnPaging);
			queryAfterModelModifyCallBack(getModel(),paging);
		}

		return SUCCESS;
	}

}
