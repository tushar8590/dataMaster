package com.datamaster.servlet;


import java.util.List;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.datamaster.dao.JobStatusDao;
import com.datamaster.model.JobStatus;

@WebServlet("/JobStatusServlet")
public class JobStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    JobStatusDao statusDao = new JobStatusDao();
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<JobStatus> list= statusDao.getjobStatus("");
        JSONArray array = new JSONArray(list);
        JSONObject obj = new JSONObject();
        try {
            obj.put("key", array);
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        response.setContentType("application/json");
        response.getWriter().write(obj.toString());
    }
   


}
