package com.lyl.garfield.core.plugin.activemq.v5.define;

import com.timevale.cat.core.plugin.interceptor.ConstructorInterceptPoint;
import com.timevale.cat.core.plugin.interceptor.InstanceMethodsInterceptPoint;
import com.timevale.cat.core.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import com.timevale.cat.core.plugin.match.ClassMatch;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static com.timevale.cat.core.plugin.match.NameMatch.byName;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;

public class ActiveMQMessageProducerInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {

    private static final String ENHANCE_CLASS = "org.apache.activemq.ActiveMQMessageProducer";
    private static final String CONSUMER_MESSAGE_METHOD = "send";
    private static final String INTERCEPTOR_CLASS = "com.timevale.cat.plugin.activemq.v5.ActiveMQMessageProducerInterceptor";

    @Override protected ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[0];
    }

    @Override protected InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodsInterceptPoint[] {
                new InstanceMethodsInterceptPoint() {
                    @Override public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return named(CONSUMER_MESSAGE_METHOD).and(takesArguments(6));
                    }

                    @Override public String getMethodsInterceptor() {
                        return INTERCEPTOR_CLASS;
                    }

                    @Override public boolean isOverrideArgs() {
                        return false;
                    }
                }
        };
    }

    @Override protected ClassMatch enhanceClass() {
        return byName(ENHANCE_CLASS);
    }
}
