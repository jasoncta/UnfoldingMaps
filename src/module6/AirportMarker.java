package module6;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import processing.core.PGraphics;

/** 
 * A class to represent AirportMarkers on a world map.
 *   
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMarker extends CommonMarker {
	public List<Marker> routes;
	
	public AirportMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
		routes = new ArrayList<Marker>();
	
	}
	
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		pg.fill(65,105,225);
		pg.ellipse(x, y, 10, 10);
		
		
	}

	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		 // show rectangle with title
		String title = getName() + ": " + getCity() + ", " + getCountry();
		int titleLength = title.length();
		pg.fill(230,230,250);
		pg.rect(x, y, titleLength*6, 20);
		pg.fill(0);
		pg.text(title, x + 4, y + 14);
		
		// show routes
		
		
	}
	
	
	public String getName() {
		return getStringProperty("name");
	}
	
	public String getCity() {
		return getStringProperty("city");
	}
	
	public String getCountry() {
		return getStringProperty("country");
	}
	
	public String getCode() {
		return getStringProperty("code");
	}
	
	public void addRoute(SimpleLinesMarker e) {
		routes.add(e);
	}
	
	public void printRoute() {
		for (int i = 0; i < routes.size(); i++) {
			System.out.println(routes.get(i).getProperties());
		}
	}
	
	public List<Marker> getRoutes() {
		return routes;
	}
}
