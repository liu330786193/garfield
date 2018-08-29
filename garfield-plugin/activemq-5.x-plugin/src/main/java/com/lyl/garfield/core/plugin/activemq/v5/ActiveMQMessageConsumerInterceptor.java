package com.lyl.garfield.core.plugin.activemq.v5;

import com.timevale.cat.api.component.ComponentsDefine;
import com.timevale.cat.api.trace.AbstractSpan;
import com.timevale.cat.api.trace.SpanLayer;
import com.timevale.cat.api.trace.tag.Tags;
import com.timevale.cat.core.context.CarrierItem;
import com.timevale.cat.core.context.CatCarrierItem;
import com.timevale.cat.core.context.ContextCarrier;
import com.timevale.cat.core.context.ContextManager;
import com.timevale.cat.core.plugin.interceptor.enhance.EnhancedInstance;
import com.timevale.cat.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import com.timevale.cat.core.plugin.interceptor.enhance.MethodInterceptResult;
import org.apache.activemq.command.MessageDispatch;

import java.lang.reflect.Method;
import java.util.Map;

public class ActiveMQMessageConsumerInterceptor implements InstanceMethodsAroundInterceptor {

    public static final String ASYNC_SEND_OPERATION_NAME_PREFIX = "ActiveMQ/";

    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, MethodInterceptResult result) throws Throwable {
        MessageDispatch dispatch = (MessageDispatch) allArguments[0];
        ContextCarrier contextCarrier = getContextCarrierFromMessage(dispatch.getMessage().getProperties());
        AbstractSpan span = ContextManager.createEntrySpan(buildOperationName(dispatch.getMessage().getDestination().getPhysicalName()), contextCarrier);
        span.setComponent(ComponentsDefine.ACTIVE_MQ);
        SpanLayer.asMQ(span);
    }


    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Object ret) throws Throwable {
        if (ret != null){
            AbstractSpan activeSpan = ContextManager.activeSpan();
            Tags.STATUS_CODE.set(activeSpan, "200");
        }
        ContextManager.stopSpan();
        return ret;
    }

    @Override
    public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Throwable t) {
        AbstractSpan span = ContextManager.activeSpan();
        span.log(t).errorOccurred();
        Tags.STATUS_CODE.set(span, "500");
    }

    private ContextCarrier getContextCarrierFromMessage(Map<String,Object> properties) {
        ContextCarrier contextCarrier = new ContextCarrier();
        CarrierItem next = contextCarrier.items();
        while (next.hasNext()){
            next = next.next();
            next.setHeadValue(properties.get(CatCarrierItem.HEADER_NAME).toString());
        }
        return contextCarrier;
    }

    private String buildOperationName(String topicName) {
        return ASYNC_SEND_OPERATION_NAME_PREFIX + topicName + "/Consumer";
    }
}
