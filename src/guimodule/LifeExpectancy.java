package guimodule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

public class LifeExpectancy extends PApplet{
	UnfoldingMap map;
	
	HashMap<String, Float> lifeExpMap;
	
	List<Feature> countries;
	List<Marker> countryMarkers;
	
	public void setup() {
		size(1600, 1200, OPENGL);
		map = new UnfoldingMap(this, 50, 50, 1400, 1000, new Google.GoogleMapProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		
		lifeExpMap = loadLifeExpectancyFromCSV("LifeExpectancyWorldBankModule3.csv");
		
		countries = GeoJSONReader.loadData(this, "countries.geo.json");
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);
		
		shadeCountries();
		
		
		
		}
	
	

	private void shadeCountries() {
		for (Marker marker : countryMarkers) {
			String countryId = marker.getId();
			if (lifeExpMap.containsKey(countryId)) {
				float lifeExp = lifeExpMap.get(countryId);
				int colorLevel = (int) map(lifeExp, 40, 90, 10, 255);
				marker.setColor(color(255-colorLevel, 100, colorLevel));
			}
			else {
				marker.setColor(color(150, 150, 150));
			}
		}
		
	}



	private HashMap<String, Float> loadLifeExpectancyFromCSV(String fileName) {
		HashMap<String, Float> lifeExpMap = new HashMap<String, Float>();
		String [] rows = loadStrings(fileName);
		
		for (String row: rows) {
			String [] columns = row.split(",");
			if(columns.length == 6 && !columns[5].equals("..")) {
				lifeExpMap.put(columns[4], Float.parseFloat(columns[5]));
				System.out.println(columns[4] + " " + columns[5]);
			}
			
		}
		return lifeExpMap;
	}

	public void draw() {
		map.draw();
		
	}
}
