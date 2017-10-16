package com.springmvc.util;

import java.lang.annotation.*;

/**
 * Excel实体BEAN的属性注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelAnnotation {
	int id();//Excel列ID
	 String name();//Excel列名
	 int width();//Excel列宽
	String merge() default "";	// 垂直方向合并单元格方法名称,调用该方法,结果应返回Integer类型的值
}
