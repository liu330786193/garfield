package com.lyl.garfield.plugin.elasticsearch.v5.define;

import com.lyl.garfield.core.plugin.interceptor.ConstructorInterceptPoint;
import com.lyl.garfield.core.plugin.interceptor.InstanceMethodsInterceptPoint;
import com.lyl.garfield.core.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import com.lyl.garfield.core.plugin.match.ClassMatch;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static com.lyl.garfield.core.plugin.match.NameMatch.byName;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;

public class ElasticsearchRestInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {

    private static final String ENHANCE_CLASS = "org.elasticsearch.client.RestClient";

    private static final String INVOKE_INTERCEPT_CLASS = "com.lyl.garfield.plugin.elasticsearch.v5.ElasticsearchRestInterceptor";

    private static final String ENHANCE_METHOD = "performRequestAsync";

    @Override
    protected ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[0];
    }

    @Override
    protected InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodsInterceptPoint[]{
                new InstanceMethodsInterceptPoint() {
                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return named(ENHANCE_METHOD).and(takesArguments(7));
                    }

                    @Override
                    public String getMethodsInterceptor() {
                        return INVOKE_INTERCEPT_CLASS;
                    }

                    @Override
                    public boolean isOverrideArgs() {
                        return false;
                    }
                }
        };
    }

    @Override
    protected ClassMatch enhanceClass() {
        return byName(ENHANCE_CLASS);
    }


}
