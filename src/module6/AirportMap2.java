package module6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.geo.Location;
import parsing.ParseFeed;
import processing.core.PApplet;

/** An applet that shows airports (and routes)
 * on a world map.  
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMap2 extends PApplet {
	
	UnfoldingMap map;
	private List<Marker> airportList;
	List<Marker> routeList;
	private HashMapList<Integer, Integer> hMap;
	
	// Click variables
		private CommonMarker lastSelected;
		private CommonMarker lastClicked;
	
	public void setup() {
		// setting up PAppler
		size(1000,600, OPENGL);
		
		// setting up map and default events
		map = new UnfoldingMap(this, 200, 50, 750, 550);
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// get features from airport data
		List<PointFeature> features = ParseFeed.parseAirports(this, "airports.dat");
		
		// list for markers, hashmap for quicker access when matching with routes
		airportList = new ArrayList<Marker>();
		HashMap<Integer, Location> airports = new HashMap<Integer, Location>();
		hMap = new HashMapList<Integer, Integer>();
		
		// create markers from features
		for(PointFeature feature : features) {
			AirportMarker m = new AirportMarker(feature);
	
			m.setRadius(5);
			airportList.add(m);
			
			// put airport in hashmap with OpenFlights unique id for key
			airports.put(Integer.parseInt(feature.getId()), feature.getLocation());
		
		}
		
		
		// parse route data
		List<ShapeFeature> routes = ParseFeed.parseRoutes(this, "routes.dat");
		routeList = new ArrayList<Marker>();
		for(ShapeFeature route : routes) {
			
			// get source and destination airportIds
			int source = Integer.parseInt((String)route.getProperty("source"));
			int dest = Integer.parseInt((String)route.getProperty("destination"));
			
			// get locations for airports on route
			if(airports.containsKey(source) && airports.containsKey(dest)) {
				route.addLocation(airports.get(source));
				route.addLocation(airports.get(dest));
				//hMap.put(source, dest);
			}
			
			SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());
			
			//System.out.println(sl.getProperties());
			for (Marker air: airportList) {
				int airportNum = Integer.parseInt(air.getStringProperty("airportNum"));
				if (source == airportNum) {
					((AirportMarker) air).addRoute(sl);
				}
			}
			//((AirportMarker) airportList.get(0)).addRoute(sl);
			//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
			//routeList.add(sl);
		}
		
		((AirportMarker) airportList.get(0)).printRoute();
		
		//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
		//map.addMarkers(routeList);
		
		map.addMarkers(airportList);
		for (Marker airP: airportList) {
			map.addMarkers(((AirportMarker) airP).getRoutes());
		}
		
		
	}
	
	public void draw() {
		background(0);
		map.draw();
		addKey();
		
	}
	
	private void addKey() {
		fill(255, 250, 240);
		
		int xbase = 25;
		int ybase = 50;
		
		rect(xbase, ybase, 150, 150);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Airport Routes Key", xbase+25, ybase+25);
		
		fill(65,105,225);
		ellipse(xbase + 35, ybase + 50, 10, 10);
		
		fill(150, 30, 30);
		int tri_xbase = xbase + 35;
		int tri_ybase = ybase + 50;
		
		fill(0, 0, 0);
		textAlign(LEFT, CENTER);
		text("Airport Marker", tri_xbase + 15, tri_ybase);
		
		text("Route Marker", xbase+50, ybase+70);
		
		fill(255, 255, 255);
		line(xbase+25, ybase+70, xbase+35+10, ybase+70);
		
		
	}
	
public void mouseMoved() {
		
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		}
		
		selectMarkerIfHover(airportList);
		//hideRoutes();
		
	}
	
	private void selectMarkerIfHover(List<Marker> a) {
		
		// Abort if there's already a marker selected
		if (lastSelected != null) {
			return;
		}
		
		for (Marker m : a) {
			CommonMarker marker = (CommonMarker)m;
			if (marker.isInside(map, mouseX, mouseY)) {
				lastSelected = marker;
				marker.setSelected(true);
				showRoutes(m);
				return;
			}
			else {
				hideRoutes();
			}
		}
	}
	
	private void showRoutes(Marker a) {
		for (int i = 0; i < ((AirportMarker)a).getRoutes().size(); i++){
			((AirportMarker)a).getRoutes().get(i).setHidden(false);
		}
	}
	
	private void hideRoutes() {
		for (Marker m: airportList) {
			for (int i = 0; i < ((AirportMarker)m).getRoutes().size(); i++){
				((AirportMarker)m).getRoutes().get(i).setHidden(true);
			}
		}
	}
	

}
