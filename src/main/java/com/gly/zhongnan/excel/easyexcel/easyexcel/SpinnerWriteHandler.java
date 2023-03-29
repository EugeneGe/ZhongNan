package com.gly.zhongnan.excel.easyexcel.easyexcel;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author EugeneGe
 * @date 2023-03-13 16:51
 */
public class SpinnerWriteHandler implements SheetWriteHandler {
    // 需要传递参数进来就定义一个变量和构造方法
    private String[] areaCodeArr;
    private String[] stationCodeArr;
    private String[] mmCodeArr;
    private String[] forecastNameArr;

    public SpinnerWriteHandler() {
    }

    //传递参数

    public SpinnerWriteHandler(String[] areaCodeArr, String[] stationCodeArr, String[] mmCodeArr, String[] forecastNameArr) {
        this.areaCodeArr = areaCodeArr;
        this.stationCodeArr = stationCodeArr;
        this.mmCodeArr = mmCodeArr;
        this.forecastNameArr = forecastNameArr;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        // 获取到当前的sheet
        Sheet sheet = writeSheetHolder.getSheet();
        //首行合并设置
        Row row1 = sheet.createRow(0);
        row1.setHeight((short) 500);
        Cell cell = row1.createCell(0);
        //设置单元格内容
        cell.setCellValue("存量建筑垃圾堆体治理进度月报表");
        sheet.addMergedRegionUnsafe(new CellRangeAddress(0, 0, 0, 4));

        Map<Integer, String[]> mapDropDown = new HashMap<>();
        // 这里的key值 对应导出列的顺序 从0开始
        mapDropDown.put(0, areaCodeArr);
        mapDropDown.put(1, stationCodeArr);
        mapDropDown.put(2, mmCodeArr);
        mapDropDown.put(3, forecastNameArr);

        /// 开始设置下拉框
        DataValidationHelper helper = writeSheetHolder.getSheet().getDataValidationHelper();// 设置下拉框
        for (Map.Entry<Integer, String[]> entry : mapDropDown.entrySet()) {
            // 区间设置 第一列第一行和第二行的数据。
            // 由于第一行是头，所以第一、二行的数据实际上是第二三行
            //从第五行开始 100行都是这个
            //fristRow表示 从第几行开始 到第几行结束  表格行从0开始的
            //firstCol表示 从第几列开始 到第几列结束 表格列从0开始的
            CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(2, 1000, entry.getKey(), entry.getKey());
//        List<String> classGradeList= (List<String>) request.getSession().getAttribute("classGradeList");
//        if(classGradeList==null || classGradeList.size()==0){
//            classGradeList.add("目前没有班级");
//        }
//        String[] classGrades = classGradeList.toArray(new String[classGradeList.size()]);
            String[] classGrades = entry.getValue();
            /*   解决办法从这里开始   */
            //获取一个workbook
            Workbook workbook = writeWorkbookHolder.getWorkbook();
            //定义sheet的名称
            String hiddenName = "hidden" + entry.getKey();
            //1.创建一个隐藏的sheet 名称为 hidden
            Sheet hidden = workbook.createSheet(hiddenName);
            //2.循环赋值（为了防止下拉框的行数与隐藏域的行数相对应，将隐藏域加到结束行之后）
            for (int i = 0, length = classGrades.length; i < length; i++) {
                // 3:表示你开始的行数  3表示 你开始的列数
                hidden.createRow(i).createCell(entry.getKey()).setCellValue(classGrades[i]);
            }
            Name category1Name = workbook.createName();
            category1Name.setNameName(hiddenName);
            //4 A1:A代表隐藏域创建第N列createCell(N)时。以A1列开始A行数据获取下拉数组
            category1Name.setRefersToFormula(hiddenName + "!A1:A" + (classGrades.length + 3));
            //5 将刚才设置的sheet引用到你的下拉列表中
            DataValidationConstraint constraint8 = helper.createFormulaListConstraint(hiddenName);
            DataValidation dataValidation3 = helper.createValidation(constraint8, cellRangeAddressList);
            writeSheetHolder.getSheet().addValidationData(dataValidation3);
        }

        /// 开始设置下拉框
//        Map<Integer, String[]> mapDropDown = new HashMap<>();
//        // 这里的key值 对应导出列的顺序 从0开始
//        mapDropDown.put(0, areaCodeArr);
//        mapDropDown.put(1, stationCodeArr);
//        mapDropDown.put(2, stationCodeArr);
//        mapDropDown.put(3, forecastNameArr);
//
//        DataValidationHelper helper = sheet.getDataValidationHelper();// 设置下拉框
//        for (Map.Entry<Integer, String[]> entry : mapDropDown.entrySet()) {
//            /*** 起始行、终止行、起始列、终止列 **/
//            CellRangeAddressList addressList = new CellRangeAddressList(2, 1000, entry.getKey(), entry.getKey());
//            /*** 设置下拉框数据 **/
//            DataValidationConstraint constraint = helper.createExplicitListConstraint(entry.getValue());
//            DataValidation dataValidation = helper.createValidation(constraint, addressList);
//            /*** 处理Excel兼容性问题 **/
//            if (dataValidation instanceof XSSFDataValidation) {
//                dataValidation.setSuppressDropDownArrow(true);
//                dataValidation.setShowErrorBox(true);
//            } else {
//                dataValidation.setSuppressDropDownArrow(false);
//            }
//            sheet.addValidationData(dataValidation);
//        }

    }
}
