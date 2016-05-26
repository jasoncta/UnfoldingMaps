package demos;

import java.util.ArrayList;
import java.util.HashMap;

public class TestHashMap {



	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap<Integer, ArrayList<String>> hashmap = new HashMap<Integer,ArrayList<String>>();
		ArrayList<String> al = new ArrayList<String>();
		al.add("C");
		al.add("A");
		al.add("E");
		al.add("B");
		al.add("D");
		al.add("F");
		al.add(1, "A2");
		System.out.println(al);
		
		for (int i = 0; i < al.size(); i++) {
			int myKey = 1;
			ArrayList<String> tmp = new ArrayList<String>();
			hashmap.put(myKey, tmp);
			
		}
		
		HashMapList<String, String> myHash = new HashMapList<String, String>();
		myHash.put("key1", "A");
		myHash.put("key1", "A");
		myHash.put("key2", "B");
		myHash.put("key1", "B");
		
		System.out.println(myHash);
		
		//hashmap.put("mykey", "b");
		
		//hashmap.put("mykey", "a");
		//hashmap.put("mykey", "f");
		//String output = hashmap.get("mykey");
		//System.out.println(output);

	}

}
