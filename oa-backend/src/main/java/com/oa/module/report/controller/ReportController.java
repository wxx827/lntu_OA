package com.oa.module.report.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/report")
@CrossOrigin
public class ReportController {

    /**
     * Get overview statistics (Top cards)
     */
    @GetMapping("/overview")
    public Map<String, Object> getOverview() {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("totalEmployees", 158);
            data.put("activeProjects", 12);
            data.put("pendingTasks", 45);
            data.put("monthlyExpenses", 68500.00);
            
            // New metrics
            data.put("revenue", 1250000.00); // 营收
            data.put("clientSatisfaction", 4.8); // 客户满意度
            
            // Growth rates
            data.put("employeeGrowth", 0.08); 
            data.put("projectGrowth", 0.15); 
            data.put("taskGrowth", -0.12); 
            data.put("expenseGrowth", 0.05);
            data.put("revenueGrowth", 0.22);
            data.put("satisfactionGrowth", 0.02);

            result.put("success", true);
            result.put("data", data);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * Get attendance trends (Line + Bar chart)
     */
    @GetMapping("/chart/attendance")
    public Map<String, Object> getAttendanceTrend() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<String> dates = Arrays.asList("周一", "周二", "周三", "周四", "周五", "周六", "周日");
            
            List<Integer> late = Arrays.asList(5, 2, 8, 1, 4, 0, 0); // 迟到
            List<Integer> onTime = Arrays.asList(145, 148, 140, 150, 142, 15, 8); // 正常
            List<Integer> leave = Arrays.asList(8, 5, 7, 3, 6, 135, 140); // 请假
            List<Double> efficiency = Arrays.asList(95.0, 96.5, 94.0, 98.0, 93.5, 85.0, 80.0); // 效率

            Map<String, Object> data = new HashMap<>();
            data.put("xAxis", dates);
            
            List<Map<String, Object>> series = new ArrayList<>();
            series.add(createSeries("正常出勤", "bar", onTime));
            series.add(createSeries("迟到/早退", "bar", late));
            series.add(createSeries("请假/休息", "bar", leave));
            series.add(createSeries("工作效率", "line", efficiency)); // Mixed chart
            
            data.put("series", series);
            result.put("success", true);
            result.put("data", data);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * Get department distribution (Pie chart)
     */
    @GetMapping("/chart/department")
    public Map<String, Object> getDepartmentDist() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> data = new ArrayList<>();
            data.add(createPieData("人力资源部", 15));
            data.add(createPieData("技术研发部", 55));
            data.add(createPieData("市场营销部", 32));
            data.add(createPieData("财务部", 18));
            data.add(createPieData("运营中心", 38));

            result.put("success", true);
            result.put("data", data);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * Get Project Status (Radar Chart) - [NEW]
     */
    @GetMapping("/chart/project-status")
    public Map<String, Object> getProjectStatus() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> indicators = new ArrayList<>();
            indicators.add(createIndicator("代码质量", 100));
            indicators.add(createIndicator("交付速度", 100));
            indicators.add(createIndicator("成本控制", 100));
            indicators.add(createIndicator("团队协作", 100));
            indicators.add(createIndicator("文档规范", 100));
            indicators.add(createIndicator("创新能力", 100));

            List<Integer> teamA = Arrays.asList(90, 85, 70, 95, 80, 88);
            List<Integer> teamB = Arrays.asList(75, 95, 90, 80, 85, 70);

            Map<String, Object> data = new HashMap<>();
            data.put("indicator", indicators);
            data.put("teamA", teamA);
            data.put("teamB", teamB);

            result.put("success", true);
            result.put("data", data);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * Get Funnel Data (Funnel Chart) - [NEW]
     */
    @GetMapping("/chart/funnel")
    public Map<String, Object> getFunnelData() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> data = new ArrayList<>();
            data.add(createPieData("访问系统", 1000));
            data.add(createPieData("提交申请", 800));
            data.add(createPieData("部门审批", 600));
            data.add(createPieData("财务复核", 400));
            data.add(createPieData("完成归档", 350));

            result.put("success", true);
            result.put("data", data);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * Get Performance (Bar Race/Multi-bar) - [NEW]
     */
    @GetMapping("/chart/performance")
    public Map<String, Object> getPerformance() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<String> depts = Arrays.asList("研发部", "市场部", "运营部", "人事部", "财务部");
            List<Integer> kpi = Arrays.asList(92, 88, 85, 90, 95);
            List<Integer> satisfaction = Arrays.asList(89, 94, 90, 92, 88);

            Map<String, Object> data = new HashMap<>();
            data.put("yAxis", depts);
            
            List<Map<String, Object>> series = new ArrayList<>();
            series.add(createSeries("KPI考核", "bar", kpi));
            series.add(createSeries("员工满意度", "bar", satisfaction));

            data.put("series", series);
            result.put("success", true);
            result.put("data", data);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    // Helper methods
    private Map<String, Object> createSeries(String name, String type, List<?> data) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("type", type);
        map.put("data", data);
        if ("bar".equals(type)) {
            map.put("barMaxWidth", 30);
            Map<String, Object> itemStyle = new HashMap<>();
            itemStyle.put("borderRadius", Arrays.asList(4, 4, 0, 0));
            map.put("itemStyle", itemStyle);
        }
        if ("line".equals(type)) {
            map.put("smooth", true);
            map.put("yAxisIndex", 1); // Use secondary y-axis for line
        }
        return map;
    }

    private Map<String, Object> createPieData(String name, Integer value) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("value", value);
        return map;
    }
    
    private Map<String, Object> createIndicator(String name, Integer max) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("max", max);
        return map;
    }
}
