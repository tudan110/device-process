package indi.tudan.deviceprocess.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import indi.tudan.deviceprocess.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 设备信息加工
 *
 * @author wangtan
 * @date 2019-10-26 17:05:46
 * @since 1.0
 */
public class DeviceProcess {

    /**
     * Don't let anyone else instantiate this class
     */
    private DeviceProcess() {
    }

    /**
     * 去空
     *
     * @param list 待处理原始列表
     * @return 去空后的列表
     * @date 2019-10-27 17:27:24
     */
    public static List<Map<String, Object>> filterNullWithMap(List<Map<String, Object>> list) {
        return list.stream().filter(item ->
                StringUtils.isNotBlank(StringUtils.getStr(item.get("machineCode")))
                && StringUtils.isNotBlank(StringUtils.getStr(item.get("maintainInterval")))
                && !"null".equals(StringUtils.getStr(item.get("maintainInterval")))
        ).collect(Collectors.toList());
    }

    /**
     * 加工信息，使用 jdk HashMap
     *
     * <pre>
     *     原始数据结构
     *     [
     *         {
     *             "machineCode": "testdelete001",
     *             "machineName": "设备000011",
     *             "maintainInterval": 0,
     *             "dateList": [
     *                 1571824443000
     *             ]
     *         },
     *         {
     *             "machineCode": "testdelete001",
     *             "machineName": "设备000011",
     *             "maintainInterval": 0,
     *             "dateList": [
     *                 1571846401000
     *             ]
     *         },
     *         {
     *            "machineCode": "testdelete001",
     *            "machineName": "设备000011",
     *            "maintainInterval": 1,
     *            "dateList": [
     *                1571824443000
     *            ]
     *         },
     *         {
     *            "machineCode": "testdelete001",
     *            "machineName": "设备000011",
     *            "maintainInterval": 1,
     *            "dateList": [
     *                1571846401000
     *            ]
     *         },
     *         ...
     *     ]
     * </pre>
     * <pre>
     *     处理后的数据结构
     *     [
     *         {
     *             "machineCode": "testdelete001",
     *             "machineName": "设备000011",
     *             "intervalGroup": [
     *                 {
     *                     maintainInterval: 0,
     *                     dateList: [
     *                         1571824443000,
     *                         1571846401000
     *                     ]
     *                 },
     *                 {
     *                     maintainInterval: 1,
     *                     dateList: [
     *                         1571824443000,
     *                         1571846401000
     *                     ]
     *                 },
     *                 ...
     *             ]
     *         },
     *         ...
     *     ]
     * </pre>
     *
     * @param list 设备信息列表
     * @return 处理后的列表
     * @date 2019-10-26 17:07:31
     */
    public static List<Map<String, Object>> processWithMap(List<Map<String, Object>> list) {

        // 待返回数据
        List<Map<String, Object>> result = new ArrayList<>();

        // XXX 注意：使用 Collectors.groupingBy 的时候， 分组属性千万不能为 null
        // 按照设备编号分组
        Map<String, List<Map<String, Object>>> deviceGroup = list.stream()
                .collect(Collectors.groupingBy(item -> StringUtils.getStr(item.get("machineCode"))));

        // 遍历分组后的数据列表
        deviceGroup.forEach((machineCode, machineList1) -> {

            // 每个设备元素
            Map<String, Object> element = new HashMap<>();
            // 机器编号
            element.put("machineCode", machineCode);
            // 机器名称，只要有 machineCode 分组，machineList1 就不可能为空，但最好加上判断
            element.put("machineName", StringUtils.getStr(machineList1.get(0).get("machineName")));

            machineList1.forEach(machine -> {

                // XXX 注意：使用 Collectors.groupingBy 的时候， 分组属性千万不能为 null
                // 按照周期分组
                Map<Integer, List<Map<String, Object>>> intervalGroup = machineList1.stream()
                        .collect(Collectors.groupingBy(item -> (int) item.get("maintainInterval")));

                // 待添加的周期列表
                List<Map<String, Object>> intervalList = new ArrayList<>();

                // 遍历周期分组
                intervalGroup.forEach((maintainInterval, machineList2) -> {

                    // 每个周期元素
                    Map<String, Object> intervalElement = new HashMap<>();

                    // 周期
                    intervalElement.put("maintainInterval", maintainInterval);

                    // 时间戳列表
                    List<Long> dateList = new ArrayList<>();

                    machineList2.forEach(machineObj -> {

                        // put 合并后的时间戳列表
                        dateList.addAll((List) machineObj.get("dateList"));
                    });

                    intervalElement.put("dateList", dateList);

                    // 周期分组
                    intervalList.add(intervalElement);
                });

                // put 周期分组
                element.put("intervalGroup", intervalList);
            });

            result.add(element);
        });

        return result;
    }

    /**
     * 去空
     *
     * @param jsonArray 待处理原始列表
     * @return 去空后的列表
     * @date 2019-10-27 17:36:52
     */
    public static List<Object> filterNullWithJson(JSONArray jsonArray) {
        return jsonArray.stream().filter(item ->
                StringUtils.isNotBlank(((JSONObject) item).getString("machineCode"))
                        && StringUtils.isNotBlank(((JSONObject) item).getString("maintainInterval"))
                        && !"null".equals(((JSONObject) item).getString("maintainInterval"))
        ).collect(Collectors.toList());
    }

    /**
     * 加工信息，使用 fastjson
     *
     * @param jsonArray 设备信息列表
     * @return 处理后的列表
     * @date 2019-10-27 13:52:32
     */
    public static List<JSONObject> processWithJson(JSONArray jsonArray) {

        // 待返回数据
        List<JSONObject> result = new ArrayList<>();

        // XXX 注意：使用 Collectors.groupingBy 的时候， 分组属性千万不能为 null
        // 按照设备编号分组
        Map<String, List<Object>> deviceGroup = jsonArray.stream()
                .collect(Collectors.groupingBy(item -> StringUtils.getStr(((JSONObject) item).getString("machineCode"))));

        // 遍历分组后的数据列表
        deviceGroup.forEach((machineCode, machineList1) -> {

            // 每个设备元素
            JSONObject element = new JSONObject();
            // 机器编号
            element.put("machineCode", machineCode);
            // 机器名称，只要有 machineCode 分组，machineList1 就不可能为空，但最好加上判断
            element.put("machineName", StringUtils.getStr(((JSONObject) machineList1.get(0)).getString("machineName")));

            machineList1.forEach(machine -> {

                // XXX 注意：使用 Collectors.groupingBy 的时候， 分组属性千万不能为 null
                // 按照周期分组
                Map<Integer, List<Object>> intervalGroup = machineList1.stream()
                        .collect(Collectors.groupingBy(item -> ((JSONObject) item).getInteger("maintainInterval")));

                // 待添加的周期列表
                List<JSONObject> intervalList = new ArrayList<>();

                // 遍历周期分组
                intervalGroup.forEach((maintainInterval, machineList2) -> {

                    // 每个周期元素
                    JSONObject intervalElement = new JSONObject();

                    // 周期
                    intervalElement.put("maintainInterval", maintainInterval);

                    // 时间戳列表
                    JSONArray dateList = new JSONArray();

                    machineList2.forEach(machineObj -> {

                        // put 合并后的时间戳列表
                        dateList.addAll(((JSONObject) machineObj).getJSONArray("dateList"));
                    });

                    intervalElement.put("dateList", dateList);

                    // 周期分组
                    intervalList.add(intervalElement);
                });

                // put 周期分组
                element.put("intervalGroup", intervalList);
            });

            result.add(element);
        });

        return result;
    }

}
