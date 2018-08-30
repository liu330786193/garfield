package com.lyl.garfield.plugin.toolkit.trace.activation;

import com.lyl.garfield.core.plugin.interceptor.ConstructorInterceptPoint;
import com.lyl.garfield.core.plugin.interceptor.StaticMethodsInterceptPoint;
import com.lyl.garfield.core.plugin.interceptor.enhance.ClassStaticMethodsEnhancePluginDefine;
import com.lyl.garfield.core.plugin.match.ClassMatch;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static com.lyl.garfield.core.plugin.match.NameMatch.byName;
import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * {@link TraceAnnotationActivation} enhance the <code>tag</code> method of <code>org.skywalking.apm.toolkit.datacarrier.ActiveSpan</code>
 * by <code>org.skywalking.apm.toolkit.activation.datacarrier.ActiveSpanTagInterceptor</code>.
 *
 * @author zhangxin
 */
public class ActiveSpanLogActivation extends ClassStaticMethodsEnhancePluginDefine {

    public static final String ENHANCE_CLASS = "com.lyl.garfield.toolkit.trace.ActiveSpan";
    public static final String INTERCEPTOR_CLASS = "com.lyl.garfield.plugin.toolkit.trace.activation.ActiveSpanLogInterceptor";
    public static final String INTERCEPTOR_METHOD_NAME = "log";

    @Override protected ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[0];
    }

    @Override protected StaticMethodsInterceptPoint[] getStaticMethodsInterceptPoints() {
        return new StaticMethodsInterceptPoint[] {
            new StaticMethodsInterceptPoint() {
                @Override
                public ElementMatcher<MethodDescription> getMethodsMatcher() {
                    return named(INTERCEPTOR_METHOD_NAME);
                }

                @Override
                public String getMethodsInterceptor() {
                    return INTERCEPTOR_CLASS;
                }

                @Override
                public boolean isOverrideArgs() {
                    return false;
                }
            }
        };
    }

    @Override protected ClassMatch enhanceClass() {
        return byName(ENHANCE_CLASS);
    }
}
