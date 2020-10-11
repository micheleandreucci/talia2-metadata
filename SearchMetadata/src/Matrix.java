
import java.io.IOException;
import java.util.Enumeration;
import java.util.stream.Collectors;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class Search
 */
public class Matrix extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Matrix() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String requestJson = request.getParameter("request");
		if(requestJson==null) {
			int index=Integer.parseInt(request.getParameter("index"));
			Models models = new Models();
			try {
				models.loadModels();
				String name=models.matrixModels.get(index).getName_A();
				models.matrixModels.remove(index);
				models.SaveModels();
				response.setContentType("text/html");
				response.setStatus(200);
				response.getWriter().append(name + " deleted.");
			} catch (ClassNotFoundException e) {
				response.setContentType("text/html");
				response.setStatus(500);
				response.getWriter().append("cannot delete.");
			}
		}else {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		Model mod = objectMapper.readValue(requestJson, Model.class);
		Models models = new Models();
		try {
			models.loadModels();
			models.matrixModels.add(mod);
			models.SaveModels();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response.setContentType("text/html");
		response.setStatus(200);
		response.getWriter().append(mod.getName_A() + " saved.");
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String requestJson = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		Model mod = objectMapper.readValue(requestJson, Model.class);
		Models models = new Models();
		try {
			models.loadModels();
			models.matrixModels.add(mod);
			models.SaveModels();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		response.setContentType("text/html");
		response.setStatus(200);
		response.getWriter().append(mod.getName_A() + " saved.");
		//doGet(request, response);
	}
}
