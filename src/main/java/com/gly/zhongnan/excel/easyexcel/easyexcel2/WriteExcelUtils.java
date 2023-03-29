package com.gly.zhongnan.excel.easyexcel.easyexcel2;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.gly.zhongnan.excel.easyexcel.easyexcel.SpinnerWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;

import java.util.*;

/**
 * @author EugeneGe
 * @date 2023-03-13 17:40
 */
@Slf4j
public class WriteExcelUtils {

    public static void main(String[] args) {
        List<String> titleList = new ArrayList<>();
        titleList.add("区域");
        titleList.add("油站");
        titleList.add("油品");
        titleList.add("策略");
        titleList.add("参数");

        List<List<Object>> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<Object> objList = new ArrayList<>();
            objList.add("区域");
            objList.add("油站" + i);
            objList.add("油品" + i);
            objList.add("策略" + i);
            objList.add("参数");
            dataList.add(objList);
        }

        WriteExcelUtils writeExcelUtils = new WriteExcelUtils();
        writeExcelUtils.customDynamicExport("C:\\Users\\geluy\\Downloads\\测试导出excel" + Math.random(), titleList, dataList);
    }

    /**
     * 自定义动态导出excel
     *
     * @param fileName  文件名称 不带后缀
     * @param titleList excel标题名称
     * @param dataList  导出的数据 按顺序
     */
    public void customDynamicExport(String fileName, List<String> titleList, List<List<Object>> dataList) {
        fileName = fileName + ".xlsx";

        // 导出的数据转换为Map数据结构， k -> 标题，v -> 此标题下的数据按顺序
        Map<String, List<Object>> dataMap = new HashMap<>(titleList.size());
        for (int i = 0; i < titleList.size(); i++) {
            // 当前列数据
            List<Object> currectTitleDatas = new ArrayList<>();
            for (int j = 0; j < dataList.size(); j++) {
                currectTitleDatas.add(dataList.get(j).get(i));
            }
            dataMap.put(titleList.get(i), currectTitleDatas);
        }

        // 获取标题信息
        List<List<String>> headTitleInfo = excelTitle(titleList);
        // 获取数据信息
        if (CollectionUtils.isEmpty(dataList)) {
            log.error("===> 导出excel数据为空");
            throw new RuntimeException();
        }
        String[] areaCodeArr = new String[]{"清华大学", "北京大学", "郑州大学", "南京大学"};
        String[] stationCodeArr = new String[500];
        for (int i = 0; i < 500; i++) {
            stationCodeArr[i] = "清华大学" + i;
        }
        String[] mmCodeArr = new String[]{"清华大学", "北京大学", "郑州大学", "南京大学"};
        String[] forecastNameArr = new String[]{"清华大学", "北京大学", "郑州大学", "南京大学"};

        ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(fileName);
        excelWriterBuilder
                //设置表头样式
                .registerWriteHandler(WriteExcelUtils.getHorizontalCellStyleStrategy())
                .registerWriteHandler(new CustomCellWriteHandler())
                //表头
                .head(headTitleInfo)
                .sheet("模板")
                //增加下拉
                .registerWriteHandler(new SpinnerWriteHandler(areaCodeArr, stationCodeArr, mmCodeArr, forecastNameArr))
                .relativeHeadRowIndex(1)
                // 当然这里数据也可以用 List<List<String>> 去传入
                .doWrite(dataList);

            //response方式返回excel文件流
//        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
//        // 响应类型,编码
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//
//        EasyExcel.write(response.getOutputStream()).head(headTitleInfo).
//                registerWriteHandler(ForecastConfigExcelUtils.getHorizontalCellStyleStrategy()).
//                registerWriteHandler(new ForecastConfigCellWriteHandler()).
//                sheet("模板").
//                registerWriteHandler(new ForecastConfigExcelUtils(title, areaCodeList, stationCodeList,
//                        mmCodeList, forecastNameList))
//                .relativeHeadRowIndex(1).doWrite(dataList);
    }


    /**
     * 获取excel标题
     *
     * @param titleList
     * @return
     */
    private List<List<String>> excelTitle(List<String> titleList) {
        if (CollectionUtils.isEmpty(titleList)) {
            log.error("====> 导出excel标题为空");
            throw new RuntimeException();
        }

        List<List<String>> excelTitleList = new ArrayList<>(titleList.size());
        titleList.forEach(k -> {
            List<String> titles = new ArrayList<>(1);
            titles.add(k);
            excelTitleList.add(titles);
        });
        return excelTitleList;
    }

    public static HorizontalCellStyleStrategy getHorizontalCellStyleStrategy() {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景设置为白色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.YELLOW1.index);
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        //边框
        headWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        headWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        headWriteCellStyle.setBorderRight(BorderStyle.THIN);
        headWriteCellStyle.setBorderTop(BorderStyle.THIN);
        //自动换行
        headWriteCellStyle.setWrapped(true);
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setBold(true);
        headWriteFont.setFontName("宋体");
        headWriteFont.setFontHeightInPoints((short) 12);
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 背景绿色
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //边框
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        //自动换行
        contentWriteCellStyle.setWrapped(true);
        //文字
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short) 12);
        contentWriteFont.setFontName("宋体");
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }

}
