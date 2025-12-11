package org.example.apkahotels.controllers;

import org.example.apkahotels.services.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;


@Controller
@RequestMapping("/admin/hotels") // ✅ ZMIEŃ na to samo co AdminController
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Map<String, Object> stats = dashboardService.getAdvancedDashboardStats();
        model.addAttribute("stats", stats);
        return "admin_dashboard";
    }
}