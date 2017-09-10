package com.mjx.ibatis;

/**
 * Created by Administrator on 2017/9/9 0009.
 */
public class PagingAppServiceImpl implements AppService {
    private IDAO idao;

    public IDAO getIdao() {
        return this.idao;
    }

    public void setIdao(IDAO idao) {
        this.idao = idao;
    }

    public IPaging doService(IPaging paging) {
        IPaging a = null;
        if(paging instanceof PagingMapImpl && ((PagingMapImpl)paging).containsKey("statementPrefix")) {
            a = (IPaging)this.idao.execute(new Object[]{((PagingMapImpl)paging).get("statementPrefix").toString(), paging});
        } else {
            a = this.idao.getPage(paging);
        }

        return a;
    }

    public Object doService(Object object) {
        return this.doService((IPaging)object);
    }

}
