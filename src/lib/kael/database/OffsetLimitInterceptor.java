package lib.kael.database;

import java.util.Properties;  

import lib.kael.util.PropertiesHelper;

import org.apache.ibatis.executor.Executor;  
import org.apache.ibatis.mapping.BoundSql;  
import org.apache.ibatis.mapping.MappedStatement;  
import org.apache.ibatis.mapping.MappedStatement.Builder;  
import org.apache.ibatis.mapping.SqlSource;  
import org.apache.ibatis.plugin.Interceptor;  
import org.apache.ibatis.plugin.Intercepts;  
import org.apache.ibatis.plugin.Invocation;  
import org.apache.ibatis.plugin.Plugin;  
import org.apache.ibatis.plugin.Signature;  
import org.apache.ibatis.session.ResultHandler;  
import org.apache.ibatis.session.RowBounds;  

@Intercepts({ @Signature(type = Executor.class, method = "query", args = {  
    MappedStatement.class, Object.class, RowBounds.class,
    ResultHandler.class }) })
 public class OffsetLimitInterceptor implements Interceptor
 {
	static int MAPPED_STATEMENT_INDEX = 0;  
    static int PARAMETER_INDEX = 1;  
    static int ROWBOUNDS_INDEX = 2;
    static int RESULT_HANDLER_INDEX = 3;  
    
    Dialect dialect;
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		processIntercept(invocation.getArgs());
        return invocation.proceed();  
	}

	void processIntercept(final Object[] queryArgs)
	{
		MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];  
        Object parameter = queryArgs[PARAMETER_INDEX];  
        final RowBounds rowBounds = (RowBounds) queryArgs[ROWBOUNDS_INDEX]; 
        int offset = rowBounds.getOffset();  
        int limit = rowBounds.getLimit();  
        if (dialect.supportsLimit()
                && (offset != RowBounds.NO_ROW_OFFSET || limit != RowBounds.NO_ROW_LIMIT)) {  
            BoundSql boundSql = ms.getBoundSql(parameter);  
            String sql = boundSql.getSql().trim();
            if (dialect.supportsLimitOffset()) {  
                sql = dialect.getLimitString(sql, offset, limit);
                offset = RowBounds.NO_ROW_OFFSET;  System.out.println(sql);
            } else {  
                sql = dialect.getLimitString(sql, 0, limit);  
            }  
            limit = RowBounds.NO_ROW_LIMIT;  
  
            queryArgs[ROWBOUNDS_INDEX] = new RowBounds(offset, limit);  
            BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql,  
                    boundSql.getParameterMappings(),  
                    boundSql.getParameterObject());  
            MappedStatement newMs = copyFromMappedStatement(ms,  
                    new BoundSqlSqlSource(newBoundSql));  
            queryArgs[MAPPED_STATEMENT_INDEX] = newMs;  
        }  
	}
	
	 private MappedStatement copyFromMappedStatement(MappedStatement ms,  
	            SqlSource newSqlSource) {  
	        Builder builder = new MappedStatement.Builder(ms.getConfiguration(),  
	                ms.getId(), newSqlSource, ms.getSqlCommandType());  
	        builder.resource(ms.getResource());  
	        builder.fetchSize(ms.getFetchSize());  
	        builder.statementType(ms.getStatementType());  
	        builder.keyGenerator(ms.getKeyGenerator());  
	        builder.keyProperty(ms.getKeyProperty());  
	        builder.timeout(ms.getTimeout());  
	        builder.parameterMap(ms.getParameterMap());  
	        builder.resultMaps(ms.getResultMaps());  
	        builder.cache(ms.getCache());
	        MappedStatement newMs = builder.build();
	        return newMs;  
	}  
	
	@Override
	public Object plugin(Object arg0) {
		return Plugin.wrap(arg0, this);
	}

	@Override
	public void setProperties(Properties properties) {
		String dialectClass = new PropertiesHelper(properties)  
        .getRequiredString("dialectClass");  
		try {  
			dialect = (Dialect) Class.forName(dialectClass).newInstance();  
		} catch (Exception e) {  
			throw new RuntimeException(  
					"cannot create dialect instance by dialectClass:"  
                    + dialectClass, e);  
		}  
		System.out.println(OffsetLimitInterceptor.class.getSimpleName()  
				+ ".dialect=" + dialectClass);	
	}
	
	public static class BoundSqlSqlSource implements SqlSource {  
        BoundSql boundSql;  
  
        public BoundSqlSqlSource(BoundSql boundSql) {  
            this.boundSql = boundSql;  
        }  
  
        public BoundSql getBoundSql(Object parameterObject) {  
            return boundSql;  
        }  
    }
	
 }