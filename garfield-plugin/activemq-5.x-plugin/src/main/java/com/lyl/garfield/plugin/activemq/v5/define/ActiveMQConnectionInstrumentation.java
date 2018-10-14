package com.lyl.garfield.plugin.activemq.v5.define;

import com.lyl.garfield.core.plugin.interceptor.ConstructorInterceptPoint;
import com.lyl.garfield.core.plugin.interceptor.InstanceMethodsInterceptPoint;
import com.lyl.garfield.core.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import com.lyl.garfield.core.plugin.match.ClassMatch;
import com.lyl.garfield.core.plugin.match.HierarchyMatch;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class ActiveMQConnectionInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {

    private static final String ENHANCE_CLASS = "javax.jms.Connection";
    private static final String CONSUMER_MESSAGE_METHOD = "createSession";
    private static final String INTERCEPTOR_CLASS = "com.lyl.garfield.plugin.activemq.v5.ActiveMQConnectionInterceptor";

    @Override
    protected ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
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
        return HierarchyMatch.byHierarchyMatch(new String[]{ENHANCE_CLASS});
    }
}
