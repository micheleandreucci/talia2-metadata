
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class Search
 */
public class LoadMatrix extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoadMatrix() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String res = "failed";
		try {
			Models models = new Models();
			Models.loadModels();
			Models.matrixModels.get(0).getAuthor();
			ObjectMapper objectMapper = new ObjectMapper();
			res = objectMapper.writeValueAsString(models);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		response.setContentType("text/html");
		response.setStatus(200);

		response.getWriter().append(res);

	}

	private String getResponse(String reqUrl) throws IOException {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		String responseBody = null;

		try {
			HttpGet httpget = new HttpGet(reqUrl);

			// System.out.println("Executing request " + httpget.getRequestLine());

			// Create a custom response handler
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

				@Override
				public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity) : null;
					} else {
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}

			};

			responseBody = httpclient.execute(httpget, responseHandler);
		} finally {

			httpclient.close();
		}

		return responseBody;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
