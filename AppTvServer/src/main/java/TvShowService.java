import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import spark.utils.IOUtils;

public class TvShowService {
	
	private Show shows = new Show(0);
	private final int showsPerPage = 250;
	
	// Get all 250 tv shows of a page
	private void getAllShows(int page) {
		try {
			URL url = new URL("http://api.tvmaze.com/shows?page=" + page);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.connect();
			String body = IOUtils.toString(connection.getInputStream());
			
			shows.setPage(page);
			shows.setJsonString(body);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Get a tv show with an specific id
	public String getShow(int id) {
		
		try {
			URL url = new URL("http://api.tvmaze.com/shows/" + id);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.connect();
			String body = IOUtils.toString(connection.getInputStream());
			
			return body;
		} catch (IOException e) {
			e.printStackTrace();
			
			return null;
		}
		
	}
	
	// Get 6 tv shows thumbnails
	public String getShowThumbnail(int id) {
		
		if ((id / showsPerPage) != shows.getPage() || shows.getJsonString() == null) {
			getAllShows(id / showsPerPage);
		}
		
		JsonArray showsArray = new JsonArray();
		JsonArray jsonArray = new Gson().fromJson(shows.getJsonString(), JsonArray.class);
		
		for (int i = id + 1; i < id + 7; i++) {
			JsonObject jsonObject = new JsonObject();
			int index = (i - 1) % showsPerPage;
			
			jsonObject.addProperty("id", jsonArray.get(index).getAsJsonObject().get("id").getAsInt());
			jsonObject.addProperty("name", jsonArray.get(index).getAsJsonObject().get("name").getAsString());
			jsonObject.addProperty("imageUrl", jsonArray.get(index).getAsJsonObject().get("image")
					.getAsJsonObject().get("medium").getAsString());
			
			showsArray.add(jsonObject);
		}
		
		return showsArray.toString();
	}
}
