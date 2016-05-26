package module6;

import java.awt.Component;
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
public class AirportMap extends PApplet {
	
	UnfoldingMap map;
	private List<Marker> airportList;
	List<Marker> routeList;
	private HashMapList<Integer, Integer> hMap;
	
	// Click variables
	private CommonMarker lastSelected;
	private CommonMarker lastClicked;
	
	public void setup() {
		// setting up PAppler
		size(1600,1200, OPENGL);
		
		// setting up map and default events
		map = new UnfoldingMap(this, 50, 50, 1450, 1050);
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
			//hMap.put(Integer.parseInt(feature.getId()), m);
			
			// put airport in hashmap with OpenFlights unique id for key
			airports.put(Integer.parseInt(feature.getId()), feature.getLocation());
			
			//printAirports();
			
		
		}
		
		
		
		// parse route data
		List<ShapeFeature> routes = ParseFeed.parseRoutes(this, "routes.dat");
		routeList = new ArrayList<Marker>();
		for(ShapeFeature route : routes) {
			
			// get source and destination airportIds
			int source = Integer.parseInt((String)route.getProperty("source"));
			int dest = Integer.parseInt((String)route.getProperty("destination"));
			
			//int counter = 0;
			// get locations for airports on route
			if(airports.containsKey(source) && airports.containsKey(dest)) {
				route.addLocation(airports.get(source));
				route.addLocation(airports.get(dest));
				hMap.put(source, dest);
				
			}
			
			
			SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());
		
			//System.out.println(sl.getProperties());
			
			
			//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
			routeList.add(sl);
		}
		//System.out.println(hMap);
		
		
		
		//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
		map.addMarkers(routeList);
		
		map.addMarkers(airportList);
		hideRoutes();
		removeSmallAirports();
		
	}
	
	public void draw() {
		background(0);
		map.draw();
		
		
	}
	
	public void mouseMoved() {
		
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		}
		
		selectMarkerIfHover(airportList);
		
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
				//showRoutes(m);
				unhideRoutes(m);
				marker.setSelected(true);
				return;
			}
			else {
				//unhideAirports();
				removeSmallAirports();
			}
		}
	}
	
	public void unhideRoutes(Marker a) {
		
		int airportNum = Integer.parseInt(a.getStringProperty("airportNum"));
		ArrayList<Integer> tmp = hMap.get(airportNum);
		for (int i = 0; i < tmp.size(); i++) {
			
		for (Marker r: routeList) {
			int source = Integer.parseInt((String)r.getProperty("source"));
			if (airportNum == source) {
				r.setHidden(false);
			}
			else {
				r.setHidden(true);
			}
			
		}
		}
	}
	
	public void showRoutes(Marker m) {
		for (Marker r: routeList) {
			int source = Integer.parseInt((String)r.getProperty("source"));
			//int dest = Integer.parseInt((String)r.getProperty("destination"));
			String s = m.getStringProperty("airportNum");
			int airportNum = Integer.parseInt(s);
			//System.out.println(source + " " + airportNum);
			ArrayList<Integer> destList = new ArrayList<Integer>();

			if (source == airportNum) {
				int dest = Integer.parseInt((String)r.getProperty("destination"));
				destList.add(dest);
				r.setHidden(false);
				//m.setHidden(true);

			}
			else {
				r.setHidden(true);
				//m.setHidden(false);

			}

			/*
			for (int i=0; i < destList.size(); i++) {
				//System.out.println(source + " " + destList.get(i) + " ");
				for (Marker a: airportList) {
					String st = a.getStringProperty("airportNum");
					Integer airportNumt = Integer.parseInt(st);

					//System.out.println(st + " = " + airportNumt);

					if (destList.get(i).intValue() == (airportNumt).intValue()) {
						a.setHidden(false);
						System.out.println("entered");
					}
					else {
						a.setHidden(true);
					}
				}

			}
			*/

		}
	}
	
	private void unhideRoutes() {
		for (Marker r: routeList) {
			r.setHidden(false);
		}
	}
	
	private void hideRoutes() {
		for (Marker r: routeList) {
			r.setHidden(true);
		}
	}
	
	private void unhideAirports() {
		for (Marker a: airportList) {
			a.setHidden(false);
		}
	}
	
	private void removeSmallAirports() {
		for (Marker a: airportList) {
			//System.out.println(a.getStringProperty("airportNum"));
			if (hMap.containsKey(Integer.parseInt(a.getStringProperty("airportNum")))) {
				a.setHidden(false);
				//System.out.println("entered");
			}
			else {
				a.setHidden(true);
				//System.out.println("hide: " + ((AirportMarker) a).getName());
			}
		}
	}

	public void printAirports() {
		for (Marker m: airportList) {
			System.out.println(m.getStringProperty("name"));
		}
	}
	
	/*
	public void createHashMap() {
		hMap = new HashMap<Integer,ArrayList<Marker>>();
		for (Marker a: airportList) {
			String temp = a.getStringProperty("airportNum");
			Integer airportNum = Integer.parseInt(temp);
			for (Marker r: routeList) {
				Integer source = Integer.parseInt(r.getStringProperty("source"));
				if(airportNum.equals(source)) {
					ArrayList<Marker>destList = new ArrayList<Marker>();
					//destList.add(e);
				}
			}
		}
	}
	*/
	
	

}
