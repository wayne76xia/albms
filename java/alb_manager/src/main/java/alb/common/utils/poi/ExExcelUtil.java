package alb.common.utils.poi;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.WriteContext;
import com.alibaba.excel.write.ExcelBuilderImpl;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * 用于填充Excel模板
 *
 * @author HaoHao
 * Created on 2021/1/9.
 */
public class ExExcelUtil {

    /** Easy Excel */
    /**
     * 获取workbook
     * 因为EasyExcel这个库设计的原因
     * 只能使用反射获取workbook
     *
     * @param writer
     * @return
     */
    public static Workbook getWorkbook(ExcelWriter writer) {
        Workbook workbook = null;
        try {
            Class<?> clazz1 = Class.forName("com.alibaba.excel.ExcelWriter");
            Field[] fs = clazz1.getDeclaredFields();
            for (Field field : fs) {
                // 要设置属性可达，不然会抛出IllegalAccessException异常
                field.setAccessible(true);
                if ("excelBuilder".equals(field.getName())) {
                    ExcelBuilderImpl excelBuilder = (ExcelBuilderImpl) field.get(writer);
                    Class<?> clazz2 = Class.forName("com.alibaba.excel.write.ExcelBuilderImpl");
                    Field[] fs2 = clazz2.getDeclaredFields();
                    for (Field field2 : fs2) {
                        field2.setAccessible(true);
                        if ("context".equals(field2.getName())) {
                            WriteContext context = (WriteContext) field2.get(excelBuilder);
                            workbook = context.getWorkbook();
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return workbook;
    }

    /* =====  合并sheet  ===== */

    /**
     * 合并两个Excel
     *
     * @param targetWorkBook
     * @param sourceWorkBook
     * @return
     */
    public static Workbook mergeExcel(Workbook targetWorkBook, Workbook sourceWorkBook) {
        // 创建目标sheet
        org.apache.poi.ss.usermodel.Sheet targetSheet = targetWorkBook.getSheetAt(0);
        org.apache.poi.ss.usermodel.Sheet sourceSheet = sourceWorkBook.getSheetAt(0);

        moveSourceSheetIntoTargetSheet(targetWorkBook, sourceSheet, targetSheet, 0);

        return targetWorkBook;
    }

    /**
     * sheet页合并
     *
     * @param targetWorkBook    目标workbook，该对象主要用来创建单元格格式
     * @param sourceSheet       源sheet
     * @param targetSheet       目标sheet
     * @param removeTitleLength 源sheet页合并到目标sheet中需要去掉的表头长度，如果不去掉传递0
     */
    public static void moveSourceSheetIntoTargetSheet(Workbook targetWorkBook,
                                                      org.apache.poi.ss.usermodel.Sheet sourceSheet,
                                                      org.apache.poi.ss.usermodel.Sheet targetSheet,
                                                      int removeTitleLength) {

        if (sourceSheet == null) {
            return;
        }

        // 获取目标sheet最后一行的下一行
        int targetRowNums = targetSheet.getLastRowNum();
        int physicalNumberOfRows = targetSheet.getPhysicalNumberOfRows();
        targetRowNums = physicalNumberOfRows == 0 ? 0 : targetRowNums + 1;

        // 移动 源sheet页中的 合并单元格区域 到目标sheet页中
        moveSourceSheetAllMergedRegionToTargetSheet(sourceSheet, targetSheet, targetRowNums, removeTitleLength);

        int sourceRowNums = sourceSheet.getLastRowNum();
        for (int i = removeTitleLength; i <= sourceRowNums; i++) {

            Row targetRow = targetSheet.createRow(targetRowNums++);
            Row sourceRow = sourceSheet.getRow(i);

            // 复制行
            copySourceRowToTargetRow(targetWorkBook, sourceRow, targetRow);
        }
    }

    /**
     * 合并sheet页中，处理源sheet页中可能存在的 合并单元格部分；
     * 当源sheet页在合并单元格的时候可能去掉表头，所以也需去掉表头的合并单元格部分
     *
     * @param sourceSheet       源sheet
     * @param targetSheet       目标sheet
     * @param targetRowNums     目标sheet的最后一行（源合并单元格的位置，需要变化到目标单元格区域，需要提供一个位置角标）
     * @param removeTitleLength 需要移除的表头长度
     */
    private static void moveSourceSheetAllMergedRegionToTargetSheet(org.apache.poi.ss.usermodel.Sheet sourceSheet,
                                                                    org.apache.poi.ss.usermodel.Sheet targetSheet,
                                                                    int targetRowNums,
                                                                    int removeTitleLength) {

        int numMergedRegions = sourceSheet.getNumMergedRegions();
        for (int i = 0; i < numMergedRegions; i++) {

            CellRangeAddress mergedRegion = sourceSheet.getMergedRegion(i);

            int firstRow = mergedRegion.getFirstRow();

            // 去掉表头的 单元格合并
            if (firstRow < removeTitleLength) {
                continue;
            }

            int lastRow = mergedRegion.getLastRow();
            int firstColumn = mergedRegion.getFirstColumn();
            int lastColumn = mergedRegion.getLastColumn();

            // 合并单元格的行需要跟随当前单元格的行数下移
            firstRow = firstRow + targetRowNums;
            lastRow = lastRow + targetRowNums;

            CellRangeAddress cellRangeAddress = new CellRangeAddress(firstRow, lastRow, firstColumn, lastColumn);
            targetSheet.addMergedRegion(cellRangeAddress);
        }

    }

    /**
     * 移动单元格
     *
     * @param targetWorkBook 目标workbook，用来在本方法中创建单元格样式
     * @param targetCell     目标单元格
     * @param sourceCell     源单元格
     */
    private static void copySourceCellToTargetCell(Workbook targetWorkBook, Cell targetCell, Cell sourceCell) {

        if (sourceCell == null) {
            return;
        }

        // 将源单元格的格式 赋值到 目标单元格中
        CellStyle sourceCellStyle = sourceCell.getCellStyle();
        /*
            此处由于是新建了workbook对象，只能新建 CellStyle对象，然后clone，再赋值；
            直接赋值 源CellStyle对象 会报不是同源异常
        */
        CellStyle targetCellStyle = targetWorkBook.createCellStyle();
        targetCellStyle.cloneStyleFrom(sourceCellStyle);
        targetCell.setCellStyle(targetCellStyle);

        CellType cellTypeEnum = sourceCell.getCellTypeEnum();
        switch (cellTypeEnum) {
            case STRING:
                targetCell.setCellValue(sourceCell.getStringCellValue());
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(sourceCell)) {
                    // 日期格式的值
                    targetCell.setCellValue(sourceCell.getDateCellValue());
                } else {
                    targetCell.setCellValue(sourceCell.getNumericCellValue());
                }
                break;
            case BOOLEAN:
                targetCell.setCellValue(sourceCell.getBooleanCellValue());
                break;
            case FORMULA:
                // ***为公式的情况下获取的是单元格的数值
                targetCell.setCellValue(sourceCell.getNumericCellValue());
                break;
            case BLANK:
                break;
            case ERROR:
                targetCell.setCellValue(sourceCell.getErrorCellValue());
                break;
            case _NONE:
                break;
            default:

        }
    }

    /**
     * 将源行复制到目标行
     *
     * @param targetWorkBook 目标workbook，主要用来创建单元格样式
     * @param sourceRow      源行
     * @param targetRow      目标行
     */
    private static void copySourceRowToTargetRow(Workbook targetWorkBook, Row sourceRow, Row targetRow) {

        if (sourceRow == null) {
            return;
        }

        // 行高
        targetRow.setHeight(sourceRow.getHeight());

        int sourceCellNums = sourceRow.getLastCellNum();
        for (int i = 0; i < sourceCellNums; i++) {

            Cell targetCell = targetRow.createCell(i);
            Cell sourceCell = sourceRow.getCell(i);

            // 复制单元格
            copySourceCellToTargetCell(targetWorkBook, targetCell, sourceCell);
        }
    }

    public static XSSFWorkbook getNewWorkbook(Workbook workbook) {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            workbook.write(baos);
            return new XSSFWorkbook(new ByteArrayInputStream(baos.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
