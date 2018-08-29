package com.lyl.garfield.core.jvm.bean;

import java.util.Date;

public interface JVMInfoMBean {

    Date getStartTime();

    String getJVM();

    String getJavaVersion();

    String getPID();

    String getInputArguments();

    String getJavaHome();

    String getArch();

    String getOSName();

    String getOSVersion();

    String getJavaSpecificationVersion();

    String getJavaLibraryPath();

    String getFileEncode();

    int getAvailableProcessors();

    int getLoadedClassCount();

    long getTotalLoadedClassCount();

    long getUnloadedClassCount();

    long getTotalCompilationTime();

}
