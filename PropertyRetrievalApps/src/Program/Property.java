package Program;

import java.util.*;

public class Property {
	public static int id;
	public static String address;
	public static String price;
	public static Map features;
	public static List<String> images;
	//public static String images;
	
	public Property(int id, String address, String price, Map features, List<String> images){
		this.id = id;
		this.address = address;
		this.price = price;
		this.features = features;
		this.images = images;
	}
	
	public Property(){
		this.id = (int) Math.random();
	}

	public static int getId() {
		return id;
	}

	public static void setId(int id) {
		Property.id = id;
	}

	public static String getAddress() {
		return address;
	}

	public static void setAddress(String address) {
		Property.address = address;
	}

	public static String getPrice() {
		return price;
	}

	public static void setPrice(String price) {
		Property.price = price;
	}

	public static Map getFeatures() {
		return features;
	}

	public static void setFeatures(Map features) {
		Property.features = features;
	}

	public static List<String> getImages() {
		return images;
	}

	public static void setImages(List<String> images) {
		Property.images = images;
	}
	
	
}
