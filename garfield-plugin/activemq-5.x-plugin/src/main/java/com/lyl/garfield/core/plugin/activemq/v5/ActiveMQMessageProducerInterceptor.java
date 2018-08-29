package com.lyl.garfield.core.plugin.activemq.v5;

import com.timevale.cat.api.component.ComponentsDefine;
import com.timevale.cat.api.trace.AbstractSpan;
import com.timevale.cat.api.trace.SpanLayer;
import com.timevale.cat.api.trace.tag.Tags;
import com.timevale.cat.core.context.CarrierItem;
import com.timevale.cat.core.context.ContextCarrier;
import com.timevale.cat.core.context.ContextManager;
import com.timevale.cat.core.plugin.interceptor.enhance.EnhancedInstance;
import com.timevale.cat.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import com.timevale.cat.core.plugin.interceptor.enhance.MethodInterceptResult;
import com.timevale.cat.core.utils.StringUtil;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQTextMessage;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ActiveMQMessageProducerInterceptor implements InstanceMethodsAroundInterceptor {

    public static final String ASYNC_SEND_OPERATION_NAME_PREFIX = "ActiveMQ/";

    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, MethodInterceptResult result) throws Throwable {
        ActiveMQDestination destination = (ActiveMQDestination) allArguments[0];
        String physicalName = destination.getPhysicalName();
        ActiveMQTextMessage message = (ActiveMQTextMessage) allArguments[1];
        ContextCarrier contextCarrier = new ContextCarrier();
        AbstractSpan span = ContextManager.createExitSpan(buildOperationName(physicalName), contextCarrier, physicalName);
        span.setComponent(ComponentsDefine.ACTIVE_MQ);
        SpanLayer.asMQ(span);

        Map<String, Object> properties = new HashMap<>(message.getProperties());
        CarrierItem next = contextCarrier.items();

        while (next.hasNext()){
            next = next.next();
            if (!StringUtil.isEmpty(next.getHeadValue())){
                properties.put(next.getHeadKey(), next.getHeadValue());
            }
        }
        message.setProperties(properties);
    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Object ret) throws Throwable {
        ContextManager.stopSpan();
        return ret;
    }

    @Override
    public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Throwable t) {
        AbstractSpan span = ContextManager.activeSpan();
        span.log(t).errorOccurred();
        Tags.STATUS_CODE.set(span, "500");
    }

    private String buildOperationName(String topicName) {
        return ASYNC_SEND_OPERATION_NAME_PREFIX + topicName + "/Producer";
    }
}
