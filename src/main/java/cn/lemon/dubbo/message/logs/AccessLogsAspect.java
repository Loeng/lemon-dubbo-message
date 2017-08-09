package cn.lemon.dubbo.message.logs;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import cn.lemon.framework.utils.JsonUtil;

/**
 * 记录访问日志
 * @date 2017年8月3日 下午8:23:55 <br>
 * @author lonyee
 */
@Aspect
@Configuration
public class AccessLogsAspect {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

    //定义一个切入点
    @Pointcut("execution(public * cn.lemon.dubbo.message.service..*.*(..))")
    public void init(){
    	
    }
  
    /*@Before(value="init()")  
    public void before(JoinPoint jp){  
    	logger.info("方法执行前执行.....");  
    }  
      
    @AfterReturning(value="init()")  
    public void afterReturning(JoinPoint jp){  
    	logger.info("方法执行完执行.....");  
    }*/  
      
    @AfterThrowing(value="init()", throwing = "ex")
    public void throwing(JoinPoint jp, Throwable ex){
		logger.error("throwing: {}", ex.getMessage());
    }
    
    @Around(value="init()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        Object[] args = point.getArgs();
        logger.info("methodName: {}.{}, args: {}", point.getTarget(), point.getSignature().getName(), JsonUtil.writeValue(args));
        result = point.proceed(args);
		logger.info("result values: {}", JsonUtil.writeValue(result));
        return result;
    }
}
