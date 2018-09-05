package com.lyl.garfield.plugin.elastic.job.v2.define;

import com.lyl.garfield.core.plugin.interceptor.ConstructorInterceptPoint;
import com.lyl.garfield.core.plugin.interceptor.InstanceMethodsInterceptPoint;
import com.lyl.garfield.core.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import com.lyl.garfield.core.plugin.match.ClassMatch;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static com.lyl.garfield.core.plugin.bytebuddy.ArgumentTypeNameMatch.takesArgumentWithType;
import static com.lyl.garfield.core.plugin.match.NameMatch.byName;

public class ElasticJobSimpleJobInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {

    private static final String ENHANCE_CLASS = "com.dangdang.ddframe.job.executor.AbstractElasticJobExecutor";

    private static final String JOB_EXECUTOR_INTERCEPTOR_CLASS = "com.lyl.garfield.plugin.elastic.job.v2.ElasticJobSimpleJobInterceptor";

    @Override protected ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[0];
    }

    @Override protected InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodsInterceptPoint[]{
                new InstanceMethodsInterceptPoint() {

                    @Override public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return takesArgumentWithType(2, "com.dangdang.ddframe.job.event.type.JobExecutionEvent");
                    }

                    @Override public String getMethodsInterceptor() {
                        return JOB_EXECUTOR_INTERCEPTOR_CLASS;
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
