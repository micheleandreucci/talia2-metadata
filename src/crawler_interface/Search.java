package crawler_interface;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.tribes.util.Arrays;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class Search
 */
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String path = "http://localhost:9100/ivp/v2";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Search() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String query = request.getParameter("query");
		System.out.println("collezione " + query);
		String collection = request.getParameter("collection");
		collection = collection.replace(" ", "%20");
		String collections[] = collection.split(",");
		int nResult = 60;
//		String field = "*";

		for (int i = 0; i < collections.length; i++) {
			if (query == null) {

				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}

			if (query.trim() == "") {

				query = "\"\"";
			}
			String requestURL = path + "/semsearch/" + collections[i] + "/" + nResult + "/"
					+ URLEncoder.encode(query, "UTF-8");
			ObjectMapper objectMapper = new ObjectMapper();

			// get results list
			String response1 = getResponse(requestURL);
			// System.out.println( response1);
			// map results list to object
			Results res = objectMapper.readValue(response1, Results.class);
			// System.out.println( res.ResultsSize());
			// get results list
			List<Result> results = res.getResults();
			// create new list with detail of results (metadata)
			List<DetailedResult> detResults = new LinkedList<DetailedResult>();

			String requestSimWords = path + "/simw/" + collection + "/" + "10" + "/" + query;
			requestSimWords = requestSimWords.replace(" ", "%20");
			String Sim = getResponse(requestSimWords);

			String[] vetSim = Sim.split("#sep#");

			for (int k = 0; k < vetSim.length; k++) {
				String[] temp = vetSim[k].split("\t");
				if (Double.parseDouble(temp[1]) < 1) {
					vetSim[k] = vetSim[k].replaceAll("[0-9]+", "");
					vetSim[k] = vetSim[k].replace('.', ' ');
				} else {
					List<String> vetSim1 = new ArrayList<>(java.util.Arrays.asList(vetSim));
					vetSim1.remove(vetSim[k]);
					vetSim = (String[]) vetSim1.toArray(new String[vetSim1.size()]);
					k--;
				}
			}

			for (Result r : results) {
				r.setScoreperc(r.getScore());
				String id1 = r.getId();
				if (id1 == null) {

					id1 = "";
				}
				String id = id1;

				// request metadata of the current result
				String requestURL2 = path + "/getDeliverableID/" + URLEncoder.encode(id, "UTF-8");

				String delId = getResponse(requestURL2);
				if (Integer.parseInt(delId) > 0) {
					Metadata met = new Metadata();

					met.setsimWord(vetSim);

					String delData = path + "/getDeliverablePrimaryData/" + URLEncoder.encode(delId, "UTF-8");
					String deliverablesdelData = getResponse(delData);
					String[] splitPrimary = deliverablesdelData.split("---");
					met.setDelDescription(splitPrimary[0]);
					met.setUrl(splitPrimary[1]);
					met.setProjName(splitPrimary[2]);

					Double double_proj_budget = Double.parseDouble(splitPrimary[3]);
					DecimalFormat df = new DecimalFormat("###,###,###");
					String proj_budget = df.format(double_proj_budget);

					met.setProjectBudget(proj_budget);

					String data = path + "/getDeliverableSearchData/" + URLEncoder.encode(delId, "UTF-8");
					String deliverablesData = getResponse(data);
					String[] split = deliverablesData.split("---");
					met.setDelDate(split[0]);

					Double double_del_budget = Double.parseDouble(split[1]);
					String del_budget = df.format(double_del_budget);

					met.setDeliverableBudget(del_budget);
					String[] keyWords = split[2].split(", ");
					List<String> lkeywords = new ArrayList<String>();
					for (int o = 0; o < keyWords.length; o++) {
						lkeywords.add(keyWords[o]);
					}
					met.setDelKeywords(lkeywords);
					String[] targets = split[3].split(", ");
					List<String> ltargets = new ArrayList<String>();
					for (int o = 0; o < targets.length; o++) {
						ltargets.add(targets[o]);
					}
					met.setDelTargets(ltargets);

					String projectPartners = path + "/getProjectPartners/" + URLEncoder.encode(delId, "UTF-8");

					String partners = getResponse(projectPartners);
					partners = partners.replace("[", "").replace("]", "");
					List<Partner> lpartners = new ArrayList<Partner>();
					String[] vecPartners = partners.split("---");
					for (int j = 0; j < vecPartners.length; j++) {
						String[] partnerAttr = new String[4];
						partnerAttr = vecPartners[j].split("\\|");
						Partner p = new Partner();
						p.setName(partnerAttr[0]);
//						p.setLead(partnerAttr[1]);
						p.setCountry(partnerAttr[2]);
//						Double double_budget = Double.parseDouble(partnerAttr[3]);
//						String budget = df.format(double_budget);

//						p.setBudget(budget);

						lpartners.add(p);
					}
					met.setPartners(lpartners);

					DetailedResult detRes = new DetailedResult(r, met);
					detResults.add(detRes);
				}

			}

			response.setContentType("text/html");
			response.setStatus(200);
			response.addHeader("Access-Control-Allow-Origin", "*");

			ObjectMapper objectMapper2 = new ObjectMapper();
			String stringJson = objectMapper2.writeValueAsString(detResults);

			response.getWriter().append(stringJson);
		}

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

		/*
		 * URL url = new URL(reqUrl); HttpURLConnection con = (HttpURLConnection)
		 * url.openConnection(); con.setRequestMethod("GET");
		 * 
		 * BufferedReader in = new BufferedReader( new
		 * InputStreamReader(con.getInputStream())); String inputLine; StringBuffer
		 * content = new StringBuffer(); while ((inputLine = in.readLine()) != null) {
		 * content.append(inputLine); } in.close(); con.disconnect(); String
		 * serviceResponse1 = content.toString();
		 * 
		 * return serviceResponse1;
		 */
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
