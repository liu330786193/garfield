package com.lyl.garfield.plugin.activemq.v5;

import com.lyl.garfield.api.component.ComponentsDefine;
import com.lyl.garfield.api.trace.AbstractSpan;
import com.lyl.garfield.api.trace.SpanLayer;
import com.lyl.garfield.api.trace.tag.Tags;
import com.lyl.garfield.core.context.CarrierItem;
import com.lyl.garfield.core.context.CatCarrierItem;
import com.lyl.garfield.core.context.ContextCarrier;
import com.lyl.garfield.core.context.ContextManager;
import com.lyl.garfield.core.plugin.interceptor.enhance.EnhancedInstance;
import com.lyl.garfield.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import com.lyl.garfield.core.plugin.interceptor.enhance.MethodInterceptResult;
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
