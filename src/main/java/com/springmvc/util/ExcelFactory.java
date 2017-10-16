package com.springmvc.util;


import jxl.*;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.Pattern;
import jxl.format.*;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.*;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.io.*;
import java.lang.Boolean;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;

public class ExcelFactory {


    public static void createExcel(CollectionBeanProvider<?> beanProvider, Class clz, String tempPath, String excelName, String title, String locale) {
        createExcel(2, beanProvider, null, clz, tempPath, excelName, title, locale);
    }


    /**
     * 生成Excel
     *
     * @param models    封装需要到处的数据BEAN结合
     * @param clz       导成Excel的实体BEAN包名.类名
     * @param tempPath  生成Excel存放的临时路径
     * @param excelName 生成的Excel名
     * @param locale    在action中用this.localeStr(); 国际化相关
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static String createExcel(List models, Class clz, String tempPath, String excelName, String title, String locale) {
        return createExcel(1, null, models, clz, tempPath, excelName, title, locale);
    }

    public static String createExcel(List<List> models, List<Class> clz, String tempPath, String excelName, List<String> sheetNames, String locale) {
        return createExcel(1, null, models, clz, tempPath, excelName, sheetNames, locale);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static String createExcel(int whichmod, CollectionBeanProvider<?> beanProvider, List models, Class clz, String tempPath, String excelName, String title, String locale) {
        Class modelClass = null;
        WritableWorkbook workbook = null;
        WritableSheet sheet = null;
        OutputStream os = null;
        WritableCellFormat wcf_center = null;
        Field[] fields = null;
        String filePath = tempPath + System.getProperty("file.separator") + excelName + ".xls";
        try {
            modelClass = Class.forName(clz.getName());
            os = new FileOutputStream(filePath);
            workbook = Workbook.createWorkbook(os);
            sheet = workbook.createSheet(excelName, 0);
            // 用于标题
            WritableFont titleFont = new WritableFont(WritableFont.ARIAL, 17, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
                    jxl.format.Colour.WHITE);
            WritableCellFormat wcf_title = new WritableCellFormat(titleFont);
            wcf_title.setBackground(Colour.TEAL, Pattern.SOLID);
            wcf_title.setBorder(Border.ALL, BorderLineStyle.DOUBLE, Colour.OCEAN_BLUE);
            wcf_title.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
            wcf_title.setAlignment(Alignment.CENTRE);


            // 用于正文
            WritableFont NormalFont = new WritableFont(WritableFont.TAHOMA, 11);
            wcf_center = new WritableCellFormat(NormalFont);
            wcf_center.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.GRAY_25);
            wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
            wcf_center.setAlignment(Alignment.CENTRE);
            wcf_center.setWrap(true); // 是否换行

            // 获取属性
            fields = modelClass.getDeclaredFields();
            List<ExcelAnnotation> annoList = new ArrayList<ExcelAnnotation>();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                    ExcelAnnotation anno = field.getAnnotation(ExcelAnnotation.class);
                    if (null != anno) {
                        annoList.add(anno);
                    }
                }
            }

            sheet.addCell(new Label(0, 0, title, wcf_title));
            sheet.mergeCells(0, 0, annoList.size() - 1, 0);
            sheet.setRowView(0, 800);

            // 按照注解id排序Excel列
//			Arrays.sort(fields, new FieldComparator());
            for (int i = 0; i < annoList.size(); i++) {
                // 获取该字段的注解对象
                ExcelAnnotation anno = annoList.get(i);
                sheet.setColumnView(i, anno.width());

                String localeName = transLang(anno.name(), locale);
                sheet.addCell(new Label(i, 1, localeName, wcf_center));
            }

            if (whichmod == 1) {

                int rowId = 2;// 写入第几行 第一行为列头 数据从第二行开始写
                for (Object ssTopModel : models) {
                    int columnId = 0;// 写入第几列 第一列为自动计算的行号 数据从第二列开始写
                    // 获取该类 并获取自身方法
                    Class clazz = ssTopModel.getClass();
                    for (int i = 0; i < fields.length; i++) {
                        Field field = fields[i];
                        if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                            StringBuffer methodName = new StringBuffer();
                            methodName.append("get");
                            methodName.append(field.getName().substring(0, 1).toUpperCase());
                            methodName.append(field.getName().substring(1));
                            Method method = clazz.getMethod(methodName.toString());
                            String content = method.invoke(ssTopModel) == null ? "" : method.invoke(ssTopModel).toString();
                            sheet.addCell(new Label(columnId, rowId, transLang(content, locale), wcf_center));

                            // 垂直方向合并单元格
                            try {
                                ExcelAnnotation excelAnnotation = field.getAnnotation(ExcelAnnotation.class);
                                if (!StringUtils.isEmpty(excelAnnotation.merge())) {
                                    Integer mergeSize = (Integer) clazz.getMethod(excelAnnotation.merge()).invoke(ssTopModel);
                                    if (mergeSize > 0) {
                                        sheet.mergeCells(columnId, rowId, columnId, rowId + mergeSize);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            columnId++;
                        }
                    }
                    rowId++;
                }
            } else if (whichmod == 2) {
                int rowId = 2;// 写入第几行 第一行为列头 数据从第二行开始写
                while (beanProvider.next()) {
                    Object ssTopModel = beanProvider.getBean();
                    int columnId = 0;// 写入第几列 第一列为自动计算的行号 数据从第二列开始写
                    // 获取该类 并获取自身方法
                    Class clazz = clz;
                    for (int i = 0; i < fields.length; i++) {
                        Field field = fields[i];
                        if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                            StringBuffer methodName = new StringBuffer();
                            methodName.append("get");
                            methodName.append(field.getName().substring(0, 1).toUpperCase());
                            methodName.append(field.getName().substring(1));
                            Method method = clazz.getMethod(methodName.toString());
                            String content = method.invoke(ssTopModel) == null ? "" : method.invoke(ssTopModel).toString();
                            sheet.addCell(new Label(columnId, rowId, transLang(content, locale), wcf_center));
                            columnId++;
                        }
                    }
                    rowId++;
                }
            }
            workbook.write();
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (Exception e) {
            }
            try {
                os.close();
            } catch (IOException e) {
            }
        }
        return filePath;
    }


    public static void createExcel(List models, Class clz, OutputStream os) {
        Class modelClass = clz;
        WritableWorkbook workbook = null;
        try {
            workbook = Workbook.createWorkbook(os);
            WritableSheet sheet = workbook.createSheet("订单信息", 0);
            // 用于标题
            WritableFont titleFont = new WritableFont(WritableFont.ARIAL, 17, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
                    jxl.format.Colour.WHITE);
            WritableCellFormat wcf_title = new WritableCellFormat(titleFont);
            wcf_title.setBackground(Colour.TEAL, Pattern.SOLID);
            wcf_title.setBorder(Border.ALL, BorderLineStyle.DOUBLE, Colour.OCEAN_BLUE);
            wcf_title.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
            wcf_title.setAlignment(Alignment.CENTRE);


            // 用于正文
            WritableFont NormalFont = new WritableFont(WritableFont.TAHOMA, 11);
            WritableCellFormat wcf_center = new WritableCellFormat(NormalFont);
            wcf_center.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.GRAY_25);
            wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
            wcf_center.setAlignment(Alignment.CENTRE);
            wcf_center.setWrap(true); // 是否换行

            // 获取属性
            Field[] fields = modelClass.getDeclaredFields();
            List<ExcelAnnotation> annoList = new ArrayList<ExcelAnnotation>();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                    ExcelAnnotation anno = field.getAnnotation(ExcelAnnotation.class);
                    if (null != anno) {
                        annoList.add(anno);
                    }
                }
            }

            sheet.addCell(new Label(0, 0, "订单信息", wcf_title));
            sheet.mergeCells(0, 0, annoList.size() - 1, 0);
            sheet.setRowView(0, 800);

            // 按照注解id排序Excel列
//			Arrays.sort(fields, new FieldComparator());
            for (int i = 0; i < annoList.size(); i++) {
                // 获取该字段的注解对象
                ExcelAnnotation anno = annoList.get(i);
                sheet.setColumnView(i, anno.width());
                sheet.addCell(new Label(i, 1, anno.name(), wcf_center));
            }

            int rowId = 2;// 写入第几行 第一行为列头 数据从第二行开始写
            for (Object ssTopModel : models) {
                int columnId = 0;// 写入第几列 第一列为自动计算的行号 数据从第二列开始写
                // 获取该类 并获取自身方法
                Class clazz = ssTopModel.getClass();
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                        StringBuffer methodName = new StringBuffer();
                        methodName.append("get");
                        methodName.append(field.getName().substring(0, 1).toUpperCase());
                        methodName.append(field.getName().substring(1));
                        Method method = clazz.getMethod(methodName.toString());
                        String content = method.invoke(ssTopModel) == null ? "" : method.invoke(ssTopModel).toString();
                        sheet.addCell(new Label(columnId, rowId, content, wcf_center));

                        // 垂直方向合并单元格
                        try {
                            ExcelAnnotation excelAnnotation = field.getAnnotation(ExcelAnnotation.class);
                            if (!StringUtils.isEmpty(excelAnnotation.merge())) {
                                Integer mergeSize = (Integer) clazz.getMethod(excelAnnotation.merge()).invoke(ssTopModel);
                                if (mergeSize > 0) {
                                    sheet.mergeCells(columnId, rowId, columnId, rowId + mergeSize);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        columnId++;
                    }
                }
                rowId++;
            }

            workbook.write();
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (Exception e) {
            }
            try {
                os.close();
            } catch (IOException e) {
            }
        }
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    private static String createExcel(int whichmod, CollectionBeanProvider<?> beanProvider, List<List> modelsList, List<Class> clzList, String tempPath, String excelName, List<String> sheetNames, String locale) {
        Class modelClass = null;
        WritableWorkbook workbook = null;
        WritableSheet sheet = null;
        OutputStream os = null;
        WritableCellFormat wcf_center = null;
        Field[] fields = null;
        String filePath = tempPath + System.getProperty("file.separator") + excelName + ".xls";
        try {
            os = new FileOutputStream(filePath);
            workbook = Workbook.createWorkbook(os);
            for (int j = 0; j < modelsList.size(); j++) {
                List models = modelsList.get(j);
                Class clz = clzList.get(j);
                modelClass = Class.forName(clz.getName());
                sheet = workbook.createSheet(sheetNames.get(j), j);
                // 用于正文
                WritableFont NormalFont = new WritableFont(WritableFont.TAHOMA, 11);
                wcf_center = new WritableCellFormat(NormalFont);
                wcf_center.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.GRAY_25);
                wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
                wcf_center.setAlignment(Alignment.CENTRE);
                wcf_center.setWrap(true); // 是否换行

                // 获取属性
                fields = modelClass.getDeclaredFields();
                List<ExcelAnnotation> annoList = new ArrayList<ExcelAnnotation>();
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                        ExcelAnnotation anno = field.getAnnotation(ExcelAnnotation.class);
                        if (null != anno) {
                            annoList.add(anno);
                        }
                    }
                }

//                sheet.mergeCells(0, 0, annoList.size() - 1, 0);
//                sheet.setRowView(0, 800);

                // 按照注解id排序Excel列
//			Arrays.sort(fields, new FieldComparator());
                for (int i = 0; i < annoList.size(); i++) {
                    // 获取该字段的注解对象
                    ExcelAnnotation anno = annoList.get(i);
                    sheet.setColumnView(i, anno.width());

                    String localeName = transLang(anno.name(), locale);
                    sheet.addCell(new Label(i, 0, localeName, wcf_center));
                }

                if (whichmod == 1) {

                    int rowId = 1;// 写入第几行 第一行为列头 数据从第二行开始写
                    for (Object ssTopModel : models) {
                        int columnId = 0;// 写入第几列 第一列为自动计算的行号 数据从第二列开始写
                        // 获取该类 并获取自身方法
                        Class clazz = ssTopModel.getClass();
                        for (int i = 0; i < fields.length; i++) {
                            Field field = fields[i];
                            if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                                StringBuffer methodName = new StringBuffer();
                                methodName.append("get");
                                methodName.append(field.getName().substring(0, 1).toUpperCase());
                                methodName.append(field.getName().substring(1));
                                Method method = clazz.getMethod(methodName.toString());
                                String content = method.invoke(ssTopModel) == null ? "" : method.invoke(ssTopModel).toString();
                                sheet.addCell(new Label(columnId, rowId, transLang(content, locale), wcf_center));

                                // 垂直方向合并单元格
                                try {
                                    ExcelAnnotation excelAnnotation = field.getAnnotation(ExcelAnnotation.class);
                                    if (!StringUtils.isEmpty(excelAnnotation.merge())) {
                                        Integer mergeSize = (Integer) clazz.getMethod(excelAnnotation.merge()).invoke(ssTopModel);
                                        if (mergeSize > 0) {
                                            sheet.mergeCells(columnId, rowId, columnId, rowId + mergeSize);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                columnId++;
                            }
                        }
                        rowId++;
                    }
                } else if (whichmod == 2) {
                    int rowId = 2;// 写入第几行 第一行为列头 数据从第二行开始写
                    while (beanProvider.next()) {
                        Object ssTopModel = beanProvider.getBean();
                        int columnId = 0;// 写入第几列 第一列为自动计算的行号 数据从第二列开始写
                        // 获取该类 并获取自身方法
                        Class clazz = clz;
                        for (int i = 0; i < fields.length; i++) {
                            Field field = fields[i];
                            if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                                StringBuffer methodName = new StringBuffer();
                                methodName.append("get");
                                methodName.append(field.getName().substring(0, 1).toUpperCase());
                                methodName.append(field.getName().substring(1));
                                Method method = clazz.getMethod(methodName.toString());
                                String content = method.invoke(ssTopModel) == null ? "" : method.invoke(ssTopModel).toString();
                                sheet.addCell(new Label(columnId, rowId, transLang(content, locale), wcf_center));
                                columnId++;
                            }
                        }
                        rowId++;
                    }
                }
            }
            workbook.write();
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (Exception e) {
            }
            try {
                os.close();
            } catch (IOException e) {
            }
        }
        return filePath;
    }


    /**
     * 设置大标题栏的样式
     *
     * @param workbook
     * @param titleRow
     * @param titleCell
     */
    private static void setTitleStyle(org.apache.poi.ss.usermodel.Workbook workbook, Row titleRow, org.apache.poi.ss.usermodel.Cell titleCell) {
        XSSFCellStyle titleCellStyle = (XSSFCellStyle) workbook.createCellStyle();
        XSSFFont titleCellFont = (XSSFFont) workbook.createFont();
        titleRow.setHeight((short) 800);// 大标题栏行高
        titleCellFont.setFontHeightInPoints((short) 30);// 大标题栏字体大小
        titleCellFont.setFontName("");
        titleCellStyle.setFont(titleCellFont);
        titleCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(50, 150, 50)));// 大标题栏背景颜色
        titleCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        titleCellStyle.setAlignment(HorizontalAlignment.CENTER);// 大标题栏居中显示
        titleCell.setCellStyle(titleCellStyle);
    }


    /**
     * 语言转化
     *
     * @throws IOException
     */
    public static String transLang(String content, String locale) throws IOException {
        return content;
    }


    /**
     * 读取Excel
     *
     * @param filename
     * @param clz
     * @return
     * @throws Exception
     */
    public static List readExcel(String filename, Class clz) throws Exception {
        return ExcelFactory.readExcel(filename, clz, 1);
    }

    /**
     * 读取Excel
     *
     * @param filename
     * @param clz
     * @param startIndex 默认1
     * @param sheetIndex 默认0
     * @return
     * @throws Exception
     */
    public static List readExcel(String filename, Class clz, int startIndex, int sheetIndex) throws Exception {
        Workbook workbook = Workbook.getWorkbook(new File(filename));
        Sheet firstSheet = workbook.getSheet(sheetIndex);

        Class modelClass = Class.forName(clz.getName());
        // 获取属性
        Field[] fields = modelClass.getDeclaredFields();
        // 按照注解id排序Excel列
        Arrays.sort(fields, new FieldComparator());

        List lstReturn = new ArrayList();

        for (int i = startIndex; i < firstSheet.getRows(); i++) {
            Object obj = modelClass.newInstance();
            for (int j = 0; j < fields.length; j++) {
                Field field = fields[j];
                if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                    Cell cell = firstSheet.getCell(j, i);
                    String cellValue = cell.getContents();//取得内容
                    if (cell.getType() == CellType.DATE) {
                        DateCell dc = (DateCell) cell;
                        Date date = dc.getDate();
                        TimeZone zone = TimeZone.getTimeZone("GMT");
                        SimpleDateFormat sdf = new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss");
                        sdf.setTimeZone(zone);
                        cellValue = sdf.format(date);
                    }
                    setter(obj, field, cellValue, String.class);
                }
            }
            lstReturn.add(obj);
        }
        return lstReturn;
    }

    public static List readExcel(String filename, Class clz, int startIndex) throws Exception {
        return readExcel(filename, clz, startIndex, 0);
    }

    private static List exportAsList(int startIndex, org.apache.poi.ss.usermodel.Sheet sheet, Class clz, Field[] fields) throws Exception {
        int rowCount = sheet.getPhysicalNumberOfRows();
        List exportList = new ArrayList();
        for (int i = startIndex; i < rowCount; i++) {
            Object obj = clz.newInstance();
            for (int j = 0; j < fields.length; j++) {
                Field field = fields[j];
                int annotationId = field.getAnnotation(ExcelAnnotation.class).id();
                org.apache.poi.ss.usermodel.Row row = sheet.getRow(i);
                org.apache.poi.ss.usermodel.Cell cell = row.getCell(annotationId - 1);
                String cellValue;
                if (cell != null) {
                    cell.setCellType(CELL_TYPE_STRING);
                    cellValue = cell.getStringCellValue().trim();
                } else {
                    cellValue = "";
                }
                setter(obj, field, cellValue, String.class);
            }
            exportList.add(obj);
        }
        return exportList;
    }

    /**
     * 读取Excel输入流
     *
     * @param inputStream
     * @param clz
     * @return
     * @throws Exception
     */
    public static List readExcel(InputStream inputStream, Class clz) throws Exception {
        org.apache.poi.ss.usermodel.Workbook workbook = POIXMLDocument.hasOOXMLHeader(inputStream) ? new XSSFWorkbook(inputStream) : new HSSFWorkbook(inputStream);
        List lstReturn = new ArrayList();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {

            org.apache.poi.ss.usermodel.Sheet firstSheet = workbook.getSheetAt(i);

            Class modelClass = Class.forName(clz.getName());
            // 获取属性
            Field[] fields = modelClass.getDeclaredFields();
            // 按照注解id排序Excel列
            Arrays.sort(fields, new FieldComparator());

            List sheetList = exportAsList(1, firstSheet, modelClass, fields);
            lstReturn.addAll(sheetList);
        }
        return lstReturn;
    }

    /**
     * 读取Excel输入流
     * （最后一个工作簿）
     *
     * @param inputStream
     * @param clz
     * @return
     * @throws Exception
     */
    public static List readExcelLastSheet(InputStream inputStream, Class clz) throws Exception {
        org.apache.poi.ss.usermodel.Workbook workbook = POIXMLDocument.hasOOXMLHeader(inputStream) ? new XSSFWorkbook(inputStream) : new HSSFWorkbook(inputStream);

        org.apache.poi.ss.usermodel.Sheet firstSheet = workbook.getSheetAt(workbook.getNumberOfSheets() - 1);

        Class modelClass = Class.forName(clz.getName());
        // 获取属性
        Field[] fields = modelClass.getDeclaredFields();
        // 按照注解id排序Excel列
        Arrays.sort(fields, new FieldComparator());

        List sheetList = exportAsList(2, firstSheet, modelClass, fields);

        return sheetList;
    }

    /**
     * 读取一列字符串
     *
     * @param filename
     * @param startIndex
     * @return
     * @throws Exception
     */
    public static List readExcel(String filename, int startIndex) throws Exception {
        Workbook workbook = Workbook.getWorkbook(new File(filename));
        Sheet firstSheet = workbook.getSheet(0);
        List lstReturn = new ArrayList();

        for (int i = startIndex; i < firstSheet.getRows(); i++) {
            Cell cell = firstSheet.getCell(0, i);
            String cellValue = cell.getContents();//取得内容
            lstReturn.add(cellValue);
        }
        return lstReturn;
    }


    /**
     * 读取Excel
     *
     * @param sheetId  Excel页签Id
     * @param filename
     * @param clz
     * @return
     * @throws Exception
     */
    public static List readExcelBySheetId(int sheetId, String filename, Class clz) throws Exception {
        Workbook workbook = Workbook.getWorkbook(new File(filename));
        Sheet firstSheet = workbook.getSheet(sheetId);

        Class modelClass = Class.forName(clz.getName());
        // 获取属性
        Field[] fields = modelClass.getDeclaredFields();
        // 按照注解id排序Excel列
        Arrays.sort(fields, new FieldComparator());

        List lstReturn = new ArrayList();

        for (int i = 1; i < firstSheet.getRows(); i++) {
            Object obj = modelClass.newInstance();
            for (int j = 0; j < fields.length; j++) {
                Field field = fields[j];
                if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                    Cell cell = firstSheet.getCell(j, i);
                    String cellValue = cell.getContents();//取得内容
                    setter(obj, field, cellValue, String.class);
                }
            }
            lstReturn.add(obj);
        }
        return lstReturn;
    }

    /**
     * 读取Excel
     *
     * @param sheetId  Excel页签Id
     * @param filename
     * @param clz
     * @return
     * @throws Exception
     */
    public static <T> List<T> readExcelBySheetId(int sheetId, String filename, String encode, Class clz) throws Exception {
        //设置编码
        WorkbookSettings workbookSettings = new WorkbookSettings();
        workbookSettings.setEncoding(encode);
        Workbook workbook = Workbook.getWorkbook(new File(filename), workbookSettings);
        Sheet firstSheet = workbook.getSheet(sheetId);

        Class modelClass = Class.forName(clz.getName());
        // 获取属性
        Field[] fields = modelClass.getDeclaredFields();
        // 按照注解id排序Excel列
        Arrays.sort(fields, new FieldComparator());

        List lstReturn = new ArrayList();

        for (int i = 1; i < firstSheet.getRows(); i++) {
            Object obj = modelClass.newInstance();
            for (int j = 0; j < fields.length; j++) {
                Field field = fields[j];
                if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                    Cell cell = firstSheet.getCell(j, i);
                    String cellValue;
                    if (!StringUtils.isEmpty(cell.getContents())) {
                        cellValue = cell.getContents().trim();//取得内容
                    } else {
                        cellValue = "";
                    }
                    setter(obj, field, cellValue, String.class);
                }
            }
            lstReturn.add(obj);
        }
        return lstReturn;
    }


    /**
     * 读取Excel
     * <p>
     * zhulin 20150730 该方法会自动删除实体类中没有ExcelAnnotion注解的属性
     *
     * @param sheetId  Excel页签Id
     * @param filename
     * @param clz
     * @return
     * @throws Exception
     */
    public static <T> List<T> readExcelBySheetIdSmart(int sheetId, String filename, String encode, Class clz) throws Exception {
        //设置编码
        WorkbookSettings workbookSettings = new WorkbookSettings();
        workbookSettings.setEncoding(encode);
        Workbook workbook = Workbook.getWorkbook(new File(filename), workbookSettings);
        Sheet firstSheet = workbook.getSheet(sheetId);

        Class modelClass = Class.forName(clz.getName());
        // 获取属性
        Field[] fields = modelClass.getDeclaredFields();


        //删除没有注解的属性
        List<Field> fieldList = new ArrayList<Field>();
        fieldList.addAll(Arrays.asList(fields));
        Iterator<Field> fieldIter = fieldList.iterator();
        while (fieldIter.hasNext()) {
            Field f = fieldIter.next();
            if (f.getAnnotation(ExcelAnnotation.class) == null) {
                fieldIter.remove();
            }
        }

        fields = fieldList.toArray(new Field[fieldList.size()]);

        // 按照注解id排序Excel列
        Arrays.sort(fields, new FieldComparator());

        List lstReturn = new ArrayList();

        for (int i = 1; i < firstSheet.getRows(); i++) {
            Object obj = modelClass.newInstance();
            for (int j = 0; j < fields.length; j++) {
                Field field = fields[j];
                if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                    Cell cell = firstSheet.getCell(j, i);
                    String cellValue = cell.getContents();//取得内容
                    setterSmart(obj, field, cellValue, field.getType());
                }
            }
            lstReturn.add(obj);
        }
        return lstReturn;
    }

    /**
     * @param obj 操作的对象
     * @param att 操作的属性
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static void getter(Object obj, String att) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Method method = obj.getClass().getMethod("get" + att);
        System.out.println(method.invoke(obj));
    }

    /**
     * @param obj   操作的对象
     * @param field 操作的属性
     * @param value 设置的值
     * @param type  参数的属性
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static void setter(Object obj, Field field, Object value,
                              Class<?> type) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {

        StringBuffer methodName = new StringBuffer();
        methodName.append(field.getName().substring(0, 1).toUpperCase());
        methodName.append(field.getName().substring(1));

        Method method = obj.getClass().getMethod("set" + methodName.toString(), type);
        method.invoke(obj, value);
    }


    /**
     * zhulin20150730 更聪明的设置值， 对于int, double等值自动调用valueOf方法
     *
     * @param obj   操作的对象
     * @param field 操作的属性
     * @param value 设置的值
     * @param type  参数的属性
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static void setterSmart(Object obj, Field field, Object value,
                                   Class<?> type) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {

        StringBuffer methodName = new StringBuffer();
        methodName.append(field.getName().substring(0, 1).toUpperCase());
        methodName.append(field.getName().substring(1));

        Method method = obj.getClass().getMethod("set" + methodName.toString(), type);
        if (type.getName().equals("java.lang.String")) { //String类型直接设置值
            method.invoke(obj, value);
        } else {
            Method typeMethod = type.getMethod("valueOf", String.class); //应对 Integer Double等类型， 自动运行valueOf(String)
            if (null != typeMethod) {
                try {
                    method.invoke(obj, typeMethod.invoke(null, value));
                } catch (InvocationTargetException e) {
                    throw new NumberFormatException("'" + ((ExcelAnnotation) field.getDeclaredAnnotations()[0]).name() + "' 只能为数字， 文件中的值 '" + String.valueOf(value) + "'无法转换为数字， 请检查导入文件");
                }
            }
        }
    }

    /**
     * 生成Excel
     *
     * @param models    封装需要到处的数据BEAN结合
     * @param clz       导成Excel的实体BEAN包名.类名
     * @param tempPath  生成Excel存放的临时路径
     * @param excelName 生成的Excel名
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void createExcelWithoutTitile(List models, Class clz, String tempPath, String excelName) {
        Class modelClass = null;
        try {
            modelClass = Class.forName(clz.getName());
            OutputStream os = new FileOutputStream(tempPath + System.getProperty("file.separator") + excelName + ".xls");
            WritableWorkbook workbook = Workbook.createWorkbook(os);
            WritableSheet sheet = workbook.createSheet(excelName, 0);

            // 用于正文
            WritableFont NormalFont = new WritableFont(WritableFont.TAHOMA, 11);
            WritableCellFormat wcf_center = new WritableCellFormat(NormalFont);
            wcf_center.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.GRAY_25);
            wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
            wcf_center.setAlignment(Alignment.CENTRE);
            wcf_center.setWrap(true); // 是否换行
            // 获取属性
            Field[] fields = modelClass.getDeclaredFields();
            // 按照注解id排序Excel列
//			Arrays.sort(fields, new FieldComparator());
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                    // 获取该字段的注解对象
                    ExcelAnnotation anno = field.getAnnotation(ExcelAnnotation.class);
                    sheet.setColumnView(i, anno.width());
                    sheet.addCell(new Label(i, 0, anno.name(), wcf_center));
                }
            }
            int rowId = 1;// 写入第几行 第一行为列头 数据从第二行开始写
            for (Object ssTopModel : models) {
                int columnId = 0;// 写入第几列 第一列为自动计算的行号 数据从第二列开始写
                // 获取该类 并获取自身方法
                Class clazz = ssTopModel.getClass();
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                        StringBuffer methodName = new StringBuffer();
                        methodName.append("get");
                        methodName.append(field.getName().substring(0, 1).toUpperCase());
                        methodName.append(field.getName().substring(1));
                        Method method = clazz.getMethod(methodName.toString());
                        try {
                            sheet.addCell(new Label(columnId, rowId, method.invoke(ssTopModel) == null ? "" : method.invoke(ssTopModel)
                                    .toString(), wcf_center));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        columnId++;
                    }
                }
                rowId++;
            }
            workbook.write();
            workbook.close();
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取有注解覆盖的字段集合
     *
     * @param fields
     * @return
     */
    private static List<Field> getAnnotationFields(Field[] fields) {
        List<Field> results = new LinkedList<Field>();
        if (fields != null) {
            for (Field field : fields) {
                if (field != null && field.isAnnotationPresent(ExcelAnnotation.class)) {
                    results.add(field);
                }
            }
        }
        return results;
    }
}
