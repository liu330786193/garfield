package com.lyl.garfield.plugin.activemq.v5.define;

import core.plugin.interceptor.ConstructorInterceptPoint;
import core.plugin.interceptor.InstanceMethodsInterceptPoint;
import core.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import core.plugin.match.ClassMatch;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static com.lyl.garfield.core.plugin.match.NameMatch.byName;
import static net.bytebuddy.matcher.ElementMatchers.named;

public class ActiveMQConnectInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {
    private static final String ENHANCE_CLASS = "org.apache.activemq.ActiveMQConnectionFactory";
    private static final String CONSUMER_MESSAGE_METHOD = "createActiveMQConnection";
    private static final String INTERCEPTOR_CLASS = "com.lyl.garfield.plugin.activemq.v5.ActiveMQConnectInterceptor";

    @Override protected ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[0];
    }

    @Override protected InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodsInterceptPoint[] {
                new InstanceMethodsInterceptPoint() {
                    @Override public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return named(CONSUMER_MESSAGE_METHOD);
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
