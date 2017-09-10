package com.mjx.ibatis;

import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ibatis.sqlmap.engine.mapping.statement.RowHandlerCallback;
import com.ibatis.sqlmap.engine.scope.StatementScope;
import com.mjx.ibatis.dialect.Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017-9-8.
 */
public class PagingSQLExecutor extends SqlExecutor {
    private static final Logger logger = LoggerFactory.getLogger(PagingSQLExecutor.class);
    private Dialect dialect;
    private boolean enableLimit = true;

    public PagingSQLExecutor() {
    }

    public Dialect getDialect() {
        return this.dialect;
    }

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    public boolean isEnableLimit() {
        return this.enableLimit;
    }

    public void setEnableLimit(boolean enableLimit) {
        this.enableLimit = enableLimit;
    }

    public void executeQuery(StatementScope statementScope, Connection conn, String sql, Object[] parameters, int skipResults, int maxResults, RowHandlerCallback callback) throws SQLException {
        if((skipResults != NO_SKIPPED_RESULTS || maxResults != NO_MAXIMUM_RESULTS) && this.supportsLimit()) {
            sql = this.dialect.getLimitString(sql, skipResults, maxResults);
            logger.debug(sql, new Object[0]);
            skipResults = NO_SKIPPED_RESULTS;
            maxResults = NO_MAXIMUM_RESULTS;
        }

        super.executeQuery(statementScope, conn, sql, parameters, skipResults, maxResults, callback);
    }

    public boolean supportsLimit() {
        return this.enableLimit && this.dialect != null?this.dialect.supportsLimit():false;
    }
}
