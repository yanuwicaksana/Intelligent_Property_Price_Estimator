package Program;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.lang.Object;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jaunt.Element;
import com.jaunt.JauntException;
import com.jaunt.UserAgent;

public class Retrieval {
	//Helper class to create a new directory
	public static void mkDir(String dir){
		File file = new File(dir);
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
	}
	//Helper class to write the file content into the given directory path
	public static void writeFile(String dir, String content){
		FileWriter file;
		try {
			file = new FileWriter(dir);
			file.write(content);
	    	file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
	}
	//Helper class to save the image from the given URL
	public static void saveImage(String imageUrl, String destinationFile) throws IOException {
//		System.out.println("Image URL: " + imageUrl);
		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
		
	}

    public static void main(String[] args) throws Exception {
        // CRAWLING MECHANISM
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
        String mainUrl = "http://realestate.com.au";
        String START_URL = "https://www.realestate.com.au/buy/property-villa-townhouse-unit+apartment-house-in-brisbane+-+greater+region,+qld/list-1";
        int propId = 1;
        int maxDepth = 500;
        int resultPerPage = 20;
        String mainDirectory = "/Users/yanuarwicaksana/Documents/COMP7802-Thesis/NewRetrieval";
        List<String> crawledPage = new ArrayList();
        crawledPage.add(START_URL);
        List<String> descPage = new ArrayList();
        
        // CONTENT SCRAPPING
        mkDir(mainDirectory);
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        //List<List> Images = new ArrayList();
    	Map<String,String> featureMap = new HashMap<String,String>();
    	int depthcount = 1;
        try {
        	while(maxDepth > 0){
        		System.out.println("Depth Count: " + Integer.toString(depthcount));
        		depthcount++;
	            HtmlPage page = webClient.getPage(crawledPage.get(0));
	            
	            DomNodeList<DomElement> article = page.getElementsByTagName("article");
	            System.out.println("article size: " + Integer.toString(article.size()));

	            List<HtmlListItem> nextPage = page.getByXPath("//li[@class='nextLink']");
	            List<String> tempImage = new ArrayList();

	            
	            for(int x=0; x<article.size(); x++) {
	            	System.out.println("Iterate:" + Integer.toString(x));
	            	DomElement currentarticle = article.get(x);
	            	String articleclass = currentarticle.getAttribute("class");
	            	System.out.println(articleclass);
	            	
	            	try{
//	            		if(!(articleclass.equals("resultBody elite platinum tier1 rui-clearfix") || articleclass.equals("resultBody first elite platinum tier1 rui-clearfix") ||
//		            			articleclass.equals("resultBody mid-tier platinum tier1 rui-clearfix") || articleclass.equals("resultBody mid-tier tier1 rui-clearfix") ||
//		            			articleclass.equals("resultBody first mid-tier platinum tier1 rui-clearfix") || articleclass.equals("resultBody featured platinum tier1 rui-clearfix") ||
//		            			 articleclass.equals("resultBody first featured platinum tier1 rui-clearfix"))){
//		            		continue;
//		            	}
		            	
		            	if(articleclass.equals("resultBody elite platinum tier1 rui-clearfix") || articleclass.equals("resultBody first elite platinum tier1 rui-clearfix") ||
		            			articleclass.equals("resultBody mid-tier platinum tier1 rui-clearfix") || articleclass.equals("resultBody mid-tier tier1 rui-clearfix") || 
		            			articleclass.equals("resultBody first mid-tier platinum tier1 rui-clearfix")){
		            		
			            	DomElement header = currentarticle.getFirstElementChild();
			            	DomElement photoview = header.getNextElementSibling();
			            	DomElement listinginfo = photoview.getNextElementSibling();
			            	
			            	DomElement listerName = listinginfo.getFirstElementChild();
			            	DomElement propertyStats = listerName.getNextElementSibling();
			            	DomElement vcard = propertyStats.getNextElementSibling();
			            	DomElement feature = vcard.getNextElementSibling();
			            	DomElement button = feature.getNextElementSibling();
			            	
			            	DomElement priceParagraph = propertyStats.getFirstElementChild();
			            	String priceValue = priceParagraph.asText();
			            	
			            	DomElement h2 = vcard.getFirstElementChild();
			            	DomElement ahref = h2.getFirstElementChild();
			            	String addressValue = ahref.asText();
			            	
			            	DomElement detailsButton = button.getLastElementChild();
			            	String nextPageRef = detailsButton.getAttribute("href");
			            	descPage.add(mainUrl + nextPageRef);
			            	
			            	String[] featureList = feature.asText().split("(?<=[0-9])(?=\\s)");
			                for(int i=0;i<(featureList.length);i++){
			                	featureMap.put(featureList[i].substring(0, featureList[i].length()-2).replaceAll("\\s",""),
			                			featureList[i].substring(featureList[i].length()-2).replaceAll("\\s",""));
			                }
			                
			                DomElement carousel = photoview.getFirstElementChild();
			                DomElement photobody = carousel.getFirstElementChild();
			                DomElement viewport = photobody.getFirstElementChild();
			                DomElement strip = viewport.getFirstElementChild();
			                
			              //Get Multiple Image
			                for(DomElement div : strip.getChildElements()){
			                	DomElement media = div.getFirstElementChild();
			                	DomElement href = media.getFirstElementChild();
			                	DomElement image = href.getLastElementChild();
		//	                	System.out.println(image);
			                	String img = image.getAttribute("src");
			                	if (img.equals(DomElement.ATTRIBUTE_NOT_DEFINED)){
		//	                		System.out.println("enter data-src");
			                		img = image.getAttribute("data-src");
			                	}
			                	if (img.equals(DomElement.ATTRIBUTE_NOT_DEFINED)){
		//	                		System.out.println("enter data-lazyloadsrc");
			                		img = image.getAttribute("data-lazyloadsrc");
			                	}
			                	tempImage.add(img);
			                }
			            	
			            	System.out.println(priceValue);
			            	System.out.println(addressValue);
			            	System.out.println(featureMap);
			            	System.out.println("");
			            	
			                Gson gson = new GsonBuilder().registerTypeAdapter(Property.class, new PropertySerializer()).create();
			                Property properties = new Property(propId,addressValue,priceValue,
			                		featureMap,tempImage);
			                String json = gson.toJson(properties);
			                String dirName = mainDirectory +"/" +  Integer.toString(propId);
			                mkDir(dirName);
			                writeFile(dirName + "/result.json",json);
		//	                saveImage(properties.getImages(),dirName + "/" + Integer.toString(propId) + ".jpg"); Single Image
			                for(int i = 0; i<tempImage.size(); i++){
		//	                	System.out.println(tempImage.get(i).getClass().getName());
			                	saveImage(tempImage.get(i),dirName + "/" + Integer.toString(propId) +"-"+ Integer.toString(i+1) + ".jpg");
			                }
			                tempImage.clear();
			                featureMap.clear();
			                propId++;
			            	
		            	} else if(articleclass.equals("resultBody featured platinum tier1 rui-clearfix") || articleclass.equals("resultBody first featured platinum tier1 rui-clearfix")){
		            		
		            		DomElement header = currentarticle.getFirstElementChild();
			            	DomElement photoview = header.getNextElementSibling();
			            	DomElement listinginfo = photoview.getNextElementSibling();
			            	
			            	DomElement listerDiv = listinginfo.getFirstElementChild();
			            	DomElement propertyStats = listerDiv.getFirstElementChild();
			            	DomElement vcard = propertyStats.getNextElementSibling();
			            	DomElement feature = vcard.getNextElementSibling();
			            	DomElement button = feature.getNextElementSibling();
			            	
			            	DomElement priceParagraph = propertyStats.getFirstElementChild();
			            	String priceValue = priceParagraph.asText();
			            	
			            	DomElement h2 = vcard.getFirstElementChild();
			            	DomElement ahref = h2.getFirstElementChild();
			            	String addressValue = ahref.asText();
			            	
			            	DomElement detailsButton = button.getLastElementChild();
			            	String nextPageRef = detailsButton.getAttribute("href");
			            	descPage.add(mainUrl + nextPageRef);
			            	
			            	String[] featureList = feature.asText().split("(?<=[0-9])(?=\\s)");
			                for(int i=0;i<(featureList.length);i++){
			                	featureMap.put(featureList[i].substring(0, featureList[i].length()-2).replaceAll("\\s",""),
			                			featureList[i].substring(featureList[i].length()-2).replaceAll("\\s",""));
			                }
			                
			                DomElement carousel = photoview.getFirstElementChild();
			                DomElement photobody = carousel.getFirstElementChild();
			                DomElement viewport = photobody.getFirstElementChild();
			                DomElement strip = viewport.getFirstElementChild();
			                
			              //Get Multiple Image
			                for(DomElement div : strip.getChildElements()){
			                	DomElement media = div.getFirstElementChild();
			                	DomElement href = media.getFirstElementChild();
			                	DomElement image = href.getLastElementChild();
		//	                	System.out.println(image);
			                	String img = image.getAttribute("src");
			                	if (img.equals(DomElement.ATTRIBUTE_NOT_DEFINED)){
		//	                		System.out.println("enter data-src");
			                		img = image.getAttribute("data-src");
			                	}
			                	if (img.equals(DomElement.ATTRIBUTE_NOT_DEFINED)){
		//	                		System.out.println("enter data-lazyloadsrc");
			                		img = image.getAttribute("data-lazyloadsrc");
			                	}
			                	tempImage.add(img);
			                }
			            	
			            	System.out.println(priceValue);
			            	System.out.println(addressValue);
			            	System.out.println(featureMap);
			            	System.out.println("");
			            	
			            	Gson gson = new GsonBuilder().registerTypeAdapter(Property.class, new PropertySerializer()).create();
			                Property properties = new Property(propId,addressValue,priceValue,
			                		featureMap,tempImage);
			                String json = gson.toJson(properties);
			                String dirName = mainDirectory +"/" +  Integer.toString(propId);
			                mkDir(dirName);
			                writeFile(dirName + "/result.json",json);
		//	                saveImage(properties.getImages(),dirName + "/" + Integer.toString(propId) + ".jpg"); Single Image
			                for(int i = 0; i<tempImage.size(); i++){
		//	                	System.out.println(tempImage.get(i).getClass().getName());
			                	saveImage(tempImage.get(i),dirName + "/" + Integer.toString(propId) +"-"+ Integer.toString(i+1) + ".jpg");
			                }
			                tempImage.clear();
			                featureMap.clear();
			                propId++;
		            	} else{
		            		continue;
		            	}
	            		
	            	} catch(Exception ex){
	            		System.out.println(ex.getMessage());
	            		continue;
	            	}
	            	
	            }
	            DomElement link = nextPage.get(0).getFirstElementChild();
	            crawledPage.add(mainUrl + link.getAttribute("href"));
	            crawledPage.remove(0);
	            maxDepth--;
            }
        	
        	
        } catch (Exception ex ) {
        	System.out.println(ex.getMessage());
        } finally{
        	crawledPage.clear();
//          System.out.println(descPage);
          crawledPage.addAll(descPage);
//          System.out.println(crawledPage);
          WebClient webClient2 = new WebClient(BrowserVersion.BEST_SUPPORTED);
          String docUrl = "https://www.realestate.com.au/";
//          System.out.println(crawledPage.get(0));
          try{
          	for(int i = 0; i<crawledPage.size();i++){
//          		System.out.println(crawledPage.get(i));
          		String[] urlList = crawledPage.get(i).split("/");
          		String finalUrl = docUrl + urlList[3];
          		System.out.println("final url: " + finalUrl);
          		String content = "";
          		URL pageurl = new URL(finalUrl);
          		URLConnection conn = pageurl.openConnection();

      			// open the stream and put it into BufferedReader
      			BufferedReader br = new BufferedReader(
                                     new InputStreamReader(conn.getInputStream()));

      			String inputLine;
      			while ((inputLine = br.readLine()) != null) {
      				content+= inputLine + "\n";
      			}
      			br.close();
//      			System.out.println(content);
//              	HtmlPage page = webClient2.getPage(finalUrl);
//              	System.out.println(page.asText());
//              	List<HtmlParagraph> title = page.getByXPath("//p[@class='title']");
//              	List<HtmlParagraph> body = page.getByXPath("//p[@class='body']");
//              	PropertyDescription propDesc = new PropertyDescription(i+1,title.get(0).asText(),body.get(0).asText());
//              	System.out.println(propDesc.getTitle());
//              	System.out.println(propDesc.getBody());
//              	Gson gson = new GsonBuilder().registerTypeAdapter(PropertyDescription.class, new PropDescSerializer()).create();
//              	String json = gson.toJson(propDesc);
              	String dirName = mainDirectory +"/" +  Integer.toString(i+1);
              	writeFile(dirName + "/result-desc.txt",content);
              }
          	 System.out.println("Succesfully Retrieved");
          } catch (Exception ex) {
          	System.out.println(ex.getMessage());
          	System.out.println("Exited with error!");
          }
          
          
         
        }
        
    }

}