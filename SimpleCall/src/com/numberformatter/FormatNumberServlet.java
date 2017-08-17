package com.numberformatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Servlet implementation class FormatNumberServlet
 */
@WebServlet("/FormatNumberServlet")
public class FormatNumberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FormatNumberServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/json");
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String requestJSON = "";
        String serviceResponse="{\"errorMessage\":\"\"}";
        if(br != null){
        	requestJSON = br.readLine();
        	br.close();
        }
        JSONObject resObj = new JSONObject();
		String responseString="";
        JSONParser parser = new  JSONParser();
        try {
			JSONObject obj = (JSONObject) parser.parse(requestJSON);
			int firstNumber = 0;
			int lastNumber = 0;
			if(obj.get("firstNumber")!=null){
				firstNumber = Integer.parseInt((String)obj.get("firstNumber"));
			}
			if(obj.get("lastNumber")!=null){
				lastNumber = Integer.parseInt((String)obj.get("lastNumber"));
			}
			if(lastNumber>firstNumber){
				 handleError(501, "Last number is lesser than the first number");
			}
			JSONArray resArray = new JSONArray();
			for(int i=firstNumber;i<=lastNumber;i++){
				JSONObject jsonObj = new JSONObject();
				if((i%3==0) && (i%7==0)){
					jsonObj.put("number", i);
					jsonObj.put("code", "M3_ME");

					resArray.add(jsonObj);
				}else if(i%3==0){
					jsonObj.put("number", i);
					jsonObj.put("code", "M3");

					resArray.add(jsonObj);
				}else if(i%7==0){
					jsonObj.put("number", i);
					jsonObj.put("code", "ME");

					resArray.add(jsonObj);
				}
			}
			resObj.put("errorCode", 200);
			resObj.put("resultArray", resArray);
			responseString = resObj.toJSONString();
        } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseString = handleError(500,"unable to parse the input");
		} catch(Exception e){
			e.printStackTrace();
			responseString = handleError(500,"exception occured");
		}
        out.print(responseString);
	}
	
	public String handleError(int errorCode, String errorMessage){
		String response = null;
		JSONObject resObj = new JSONObject();
		resObj.put("errorCode", errorCode);
		resObj.put("errorMessage", errorMessage);
		response = resObj.toJSONString();
		return response;
	}

}
