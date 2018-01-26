package Program;

public class PropertyDescription {
	public static int id;
	public static String title;
	public static String body;
	
	public PropertyDescription(int id, String title, String body){
		this.id = id;
		this.title = title;
		this.body = body;
	}

	public static int getId() {
		return id;
	}

	public static void setId(int id) {
		PropertyDescription.id = id;
	}

	public static String getTitle() {
		return title;
	}

	public static void setTitle(String title) {
		PropertyDescription.title = title;
	}

	public static String getBody() {
		return body;
	}

	public static void setBody(String body) {
		PropertyDescription.body = body;
	}
	
	
}
