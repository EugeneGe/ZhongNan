package com.gly.zhongnan.excel.easyexcel.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author EugeneGe
 * @date 2023-03-13 16:45
 */
@Controller
@RequestMapping("/excel")
public class ExcelTest {
    /**
     * 测试导出Excel表格，带下拉框
     * 生成excel表格
     *
     * @return
     */
    @GetMapping(value = "/exportExcelpulldownmenu")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        try {
            String fileName = "模板.xlsx";

            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            // 设置背景颜色
            headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            // 设置头字体
            WriteFont headWriteFont = new WriteFont();
            headWriteFont.setFontHeightInPoints((short) 14);
            // 字体加粗
            headWriteFont.setBold(true);
            headWriteCellStyle.setWriteFont(headWriteFont);
            // 设置头居中
            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);

            // 内容策略
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            // 设置内容字体
            WriteFont contentWriteFont = new WriteFont();
            contentWriteFont.setFontHeightInPoints((short) 12);
            contentWriteFont.setFontName("宋体");
            contentWriteCellStyle.setWriteFont(contentWriteFont);
            // 设置 水平居中
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            // 设置 垂直居中
            contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            // 设置单元格格式为 文本
            contentWriteCellStyle.setDataFormat((short) 49);

            HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                    new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

            // 假数据 实际开发中一般是从数据库中查询
            List<Employee> objects = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                Employee employee = new Employee();
                employee.setAreaCode(i + "10");
                employee.setStationCode(i + "NB");
                employee.setMmCode(i + "NB");
                employee.setForecastName(i + "NB");
                objects.add(employee);
            }

            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            // 设置表名，引脚名，文件格式，list数据
            // SpinnerWriterHandler拦截器无参
//            EasyExcel.write(response.getOutputStream(), Employee.class)
//                    .registerWriteHandler(horizontalCellStyleStrategy)
//                    .registerWriteHandler(new SpinnerWriteHandler())
//                    .sheet("模板")
//                    .doWrite(objects);

            // SpinnerWriterHandler拦截器有参
            EasyExcel.write(response.getOutputStream(), Employee.class)
                    .registerWriteHandler(horizontalCellStyleStrategy)
                    .registerWriteHandler(new SpinnerWriteHandler(null, null, null, null))
                    .sheet("模板")
                    .doWrite(objects);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
