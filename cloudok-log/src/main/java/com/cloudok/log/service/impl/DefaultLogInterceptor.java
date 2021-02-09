package com.cloudok.log.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cloudok.core.vo.Response;
import com.cloudok.core.vo.VO;
import com.cloudok.log.service.LogInterceptor;
import com.cloudok.log.vo.InterceptorResult;
import com.cloudok.log.vo.LogContext;
import com.cloudok.util.NumberUtil;

@Component
public class DefaultLogInterceptor implements LogInterceptor{
	

	/**
	 * 用于SpEL表达式解析.
	 */
	private SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
	
	/**
     * 用于获取方法参数定义名字.
     */
    private DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();


	@Override
	public InterceptorResult process(LogContext context) {
		InterceptorResult result = new InterceptorResult();
		List<Long> idList = 	this.getIds(context);
		if(!CollectionUtils.isEmpty(idList)) {
			result.setBusinessId(idList.get(idList.size()-1));//最后一个id
			result.setRelationBusinessId(idList.get(0));
		}
		result.setFormatedMessage(this.getFormatedMessage(context));
		return result;
	}
	
	protected String getFormatedMessage(LogContext context) {
		String template = context.getLoggable().messageTemplate();
		String message = null;
		if(StringUtils.isEmpty(template)) {
			message = context.getInterfaceConfig().getName();
		}else {
			message = this.getValBySpEL(template, context.getMethodSignature(), context.getRet());
		}
		return message;
	}
	protected List<Long> getIds(LogContext context) {
		List<Long> idList =  new ArrayList<Long>();
		if(context.getRequest() != null) {
			idList = this.getIdsFromUri(context.getRequest().getRequestURI());
			if(context.getRequest().getMethod().equalsIgnoreCase(HttpMethod.POST.name())) {
				Object obj = context.getRet();
				if(obj != null && obj instanceof Response) {
					Response re = Response.class.cast(obj);
					Object r = re.getResult();
					Long id = r == null ? null : this.getIdFromSignalObj(r);
					if(id != null) {
						idList.add(id);
					}
				}
			}
		}
		return idList;
	}
	
	private Long getIdFromSignalObj(Object obj) { 
		 if(obj instanceof VO) {
			return VO.class.cast(obj).getId();
		 }
		return null;
	}
	
	private List<Long> getIdsFromUri(String uri){
		List<Long> idList = new ArrayList<Long>();
		String[] arrays = uri.split("/");
		for(String str : arrays) {
			if(NumberUtil.isNum(str)) {
				idList.add(Long.parseLong(str));
			}
		}
		return idList;
	}
	  /**
     * 解析spEL表达式
     */
    private String getValBySpEL(String spEL, MethodSignature methodSignature, Object... args) {
        //获取方法形参名数组
        String[] paramNames = nameDiscoverer.getParameterNames(methodSignature.getMethod());
        if (paramNames != null && paramNames.length > 0) {
            Expression expression = spelExpressionParser.parseExpression(spEL);
            // spring的表达式上下文对象
            EvaluationContext context = new StandardEvaluationContext();
            // 给上下文赋值
            for(int i = 0; i < args.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
            return expression.getValue(context).toString();
        }
        return null;
    }
    
}
