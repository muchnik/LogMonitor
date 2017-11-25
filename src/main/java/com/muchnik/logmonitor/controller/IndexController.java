package com.muchnik.logmonitor.controller;

import com.muchnik.logmonitor.service.record.RecordService;
import com.muchnik.logmonitor.service.record.impl.RecordServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "indexController", urlPatterns = "")
public class IndexController extends HttpServlet {
    private RecordService recordService = new RecordServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("records", recordService.getAll());
        req.getRequestDispatcher("WEB-INF/pages/index.jsp").forward(req, resp);
    }
}
