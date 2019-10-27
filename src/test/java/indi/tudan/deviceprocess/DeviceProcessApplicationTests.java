package indi.tudan.deviceprocess;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import indi.tudan.deviceprocess.core.DeviceProcess;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class DeviceProcessApplicationTests {

    @Test
    void contextLoads() {

    }

    @Test
    void testWithMap() {

        // 用 HashMap 组装原始数据
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object o : getDataString()) {
            JSONObject json = (JSONObject) o;
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("machineCode", json.getString("machineCode"));
            itemMap.put("machineName", json.getString("machineName"));
            itemMap.put("maintainInterval", json.getIntValue("maintainInterval"));
            List<Long> dataList = new ArrayList<>();
            for (Object dateList : json.getJSONArray("dateList")) {
                dataList.add((Long) dateList);
            }
            itemMap.put("dateList", dataList);
            list.add(itemMap);
        }

        // 打印输出结果
        System.out.println(DeviceProcess.processWithMap(DeviceProcess.filterNullWithMap(list)));
    }

    @Test
    void testWithJson() {

        // 打印输出结果
        System.out.println(
                DeviceProcess.processWithJson(
                        JSONArray.parseArray(JSON.toJSONString(
                                DeviceProcess.filterNullWithJson(getDataString())
                        ))));

    }

    private JSONArray getDataString() {
        // 1、原始数据
        JSONArray jsonArray = JSONArray.parseArray("[{\n" +
                "\t\"machineCode\": \"testdelete001\",\n" +
                "\t\"machineName\": \"设备000011\",\n" +
                "\t\"maintainInterval\": 0,\n" +
                "\t\"dateList\": [1568545050000]\n" +
                "}, {\n" +
                "\t\"machineCode\": \"testdelete001\",\n" +
                "\t\"machineName\": \"设备000011\",\n" +
                "\t\"maintainInterval\": 1,\n" +
                "\t\"dateList\": [1568545070000]\n" +
                "}, {\n" +
                "\t\"machineCode\": \"testdelete001\",\n" +
                "\t\"machineName\": \"设备000011\",\n" +
                "\t\"maintainInterval\": 1,\n" +
                "\t\"dateList\": [1568545060000]\n" +
                "}, {\n" +
                "\t\"machineCode\": \"shebeibangdtest001\",\n" +
                "\t\"machineName\": \"设备绑定测试001\",\n" +
                "\t\"maintainInterval\": 0,\n" +
                "\t\"dateList\": [1568279597000]\n" +
                "}, {\n" +
                "\t\"machineCode\": \"SDL-001\",\n" +
                "\t\"machineName\": \"离心机001\",\n" +
                "\t\"maintainInterval\": 0,\n" +
                "\t\"dateList\": [1571824443000]\n" +
                "}, {\n" +
                "\t\"machineCode\": \"HT-001\",\n" +
                "\t\"machineName\": \"选籽机001\",\n" +
                "\t\"maintainInterval\": 0,\n" +
                "\t\"dateList\": [1571824443000, 1571846401000]\n" +
                "}, {\n" +
                "\t\"machineCode\": \"FZJ-001\",\n" +
                "\t\"machineName\": \"纺织机001\",\n" +
                "\t\"maintainInterval\": null,\n" +
                "\t\"dateList\": [null]\n" +
                "}, {\n" +
                "\t\"machineCode\": \"2\",\n" +
                "\t\"machineName\": \"阿诗丹顿\",\n" +
                "\t\"maintainInterval\": 0,\n" +
                "\t\"dateList\": [1571397603000, 1571825190000, 1571882705000]\n" +
                "}, {\n" +
                "\t\"machineCode\": \"3\",\n" +
                "\t\"machineName\": \"阿诗丹顿\",\n" +
                "\t\"maintainInterval\": 1,\n" +
                "\t\"dateList\": [1571193334000]\n" +
                "}]");
        System.out.println(jsonArray.toJSONString());
        return jsonArray;
    }

}
