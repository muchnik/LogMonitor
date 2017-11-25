package com.muchnik.logmonitor.controller;

import com.muchnik.logmonitor.entity.Record;
import com.muchnik.logmonitor.service.record.RecordService;
import com.muchnik.logmonitor.service.record.impl.RecordServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "searchController", urlPatterns = "/search")
public class SearchController  extends HttpServlet{
    private RecordService recordService = new RecordServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String searchString = req.getParameter("userid");
        List<Record> records = recordService.searchByUserId(searchString);
        req.setAttribute("records", records);
        req.getRequestDispatcher("WEB-INF/pages/search.jsp").forward(req, resp);
    }
}
