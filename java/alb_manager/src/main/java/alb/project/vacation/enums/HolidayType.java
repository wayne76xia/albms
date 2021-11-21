package alb.project.vacation.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HaoHao
 * Created on 2021/10/25.
 */
public enum HolidayType {
    COMPENSATORY_LEAVE(1, "调休"),
    COMPASSIONATE_LEAVE(2, "事假"),
    ANNUAL_LEAVE(3, "年假"),
    MARRIAGE_LEAVE(4, "婚假"),
    SICK_LEAVE(5, "病假"),
    LACTATION_LEAVE(6, "哺乳假"),
    MATERNITY_LEAVE(7, "产假"),
    PATERNITY_LEAVE(8, "陪产假"),
    FUNERAL_LEAVE(9, "丧假"),
    PUBLIC_LEAVE(10, "公假");
    
    private static final Map<Integer, HolidayType> lookupTable = new HashMap<>();

    static {
        for (HolidayType type : values()) {
            lookupTable.put(type.value, type);
        }
    }

    private final Integer value;
    private final String name;

    HolidayType(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public static HolidayType getTypeFromTable(Integer value) {
        if (lookupTable.containsKey(value)) {
            return lookupTable.get(value);
        }
        throw new RuntimeException("预期的假期类型不存在！");
    }

    @Override
    public String toString() {
        return "AnnualLeaveType{" +
                "value=" + value +
                ", name='" + name + '\'' +
                '}';
    }
}
