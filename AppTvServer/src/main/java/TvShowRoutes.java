import static spark.Spark.*;

public class TvShowRoutes {
	public TvShowRoutes(final TvShowService tvShowService) {
		
		// List of all routes
		get("/showthumbnails/:id", (req, res) -> {
			int id = Integer.parseInt(req.params(":id"));
			return tvShowService.getShowThumbnail(id);
		});

		get("/shows/:id", (req, res) -> {
			int id = Integer.parseInt(req.params(":id"));
			return tvShowService.getShow(id);
		});
		
		after((req, res) -> {
			res.type("application/json");
		});
	}
}
