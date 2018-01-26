package Program;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
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
        String START_URL = "http://realestate.com.au/buy/in-brisbane+-+greater+region,+qld/list-1";
        int propId = 1;
        int maxDepth = 50;
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
        try {
        	while(maxDepth > 0){
        		
	            HtmlPage page = webClient.getPage(crawledPage.get(0));
	            List<HtmlAnchor> address = page.getByXPath("//a[@rel='listingName']");
	            System.out.println("address size: " + Integer.toString(address.size()));
	            System.out.println(address);
	            List<HtmlParagraph> price = page.getByXPath("//p[@class='priceText']");
//	            System.out.println("price size: " + Integer.toString(price.size()));
	            List<HtmlDefinitionList> features = page.getByXPath("//dl[@class='rui-property-features rui-clearfix']");
//	            System.out.println("features size: " + Integer.toString(features.size()));
	            List<HtmlDivision> container = page.getByXPath("//div[@class='strip']");
//	            System.out.println("image size: " + Integer.toString(container.size()));
	            List<HtmlListItem> nextPage = page.getByXPath("//li[@class='nextLink']");
	            List<HtmlAnchor> desc = page.getByXPath("//a[@class='detailsButton']");
//	            System.out.println("details size: " + Integer.toString(desc.size()));
	            
	            List<String> tempImage = new ArrayList();
	            for(int x=0; x<resultPerPage; x++) {
	            	System.out.println("Iterate");
	            	System.out.println(address.get(x).asText());
//	            	descPage.add(mainUrl + desc.get(x).getAttribute("href"));
//	         
//	                String[] featureList = features.get(x).asText().split("(?<=[0-9])(?=\\s)");
//	                for(int i=0;i<(featureList.length);i++){
//	                	featureMap.put(featureList[i].substring(0, featureList[i].length()-2).replaceAll("\\s",""),
//	                			featureList[i].substring(featureList[i].length()-2).replaceAll("\\s",""));
//	                }
//	                // Get Single Image
////	                DomElement div = container.get(x).getFirstElementChild();
////	                DomElement media = div.getFirstElementChild();
////	                DomElement ahref = media.getFirstElementChild();
////	                DomElement domImage = ahref.getFirstElementChild();
////	                String image = domImage.getAttribute("src");
////	                if(image.equalsIgnoreCase("")){
////	                	String[] temp = domImage.getTextContent().split("\'");
////	                	image = temp[1];
////	                }
//	                //Get Multiple Image
//	                int count = 1;
//	                for(DomElement div : container.get(x).getChildElements()){
//	                	DomElement media = div.getFirstElementChild();
//	                	DomElement ahref = media.getFirstElementChild();
//	                	DomElement image = ahref.getLastElementChild();
//	                	if (count == 1){
//	                		tempImage.add(image.getAttribute("src"));
//	                	} else{
//	                		tempImage.add(image.getAttribute("data-lazyloadsrc"));
//	                	}
//	                	count++;
//	                }
//	                //Images.add(tempImage);
//	                Gson gson = new GsonBuilder().registerTypeAdapter(Property.class, new PropertySerializer()).create();
//	                Property properties = new Property(propId,address.get(x).asText(),price.get(x).asText(),
//	                		featureMap,tempImage);
//	                String json = gson.toJson(properties);
//	                String dirName = mainDirectory +"/" +  Integer.toString(propId);
//	                mkDir(dirName);
//	                writeFile(dirName + "/result.json",json);
////	                saveImage(properties.getImages(),dirName + "/" + Integer.toString(propId) + ".jpg"); Single Image
//	                for(int i = 0; i<tempImage.size(); i++){
//	                	saveImage(tempImage.get(i),dirName + "/" + Integer.toString(propId) +"-"+ Integer.toString(i+1) + ".jpg");
//	                }
//	                tempImage.clear();
//	                featureMap.clear();
//	                propId++;
	            }
	            DomElement link = nextPage.get(0).getFirstElementChild();
	            crawledPage.add(mainUrl + link.getAttribute("href"));
	            crawledPage.remove(0);
	            maxDepth--;
            }
        	
        	
        } catch (IOException ex ) {
            ex.printStackTrace();
        }
        crawledPage.clear();
        System.out.println(descPage);
        crawledPage.addAll(descPage);
        System.out.println(crawledPage);
        WebClient webClient2 = new WebClient(BrowserVersion.FIREFOX_52);
        System.out.println(crawledPage.get(0));
        try{
        	for(int i = 0; i<crawledPage.size();i++){
            	HtmlPage page = webClient2.getPage(crawledPage.get(i));
            	List<HtmlParagraph> title = page.getByXPath("//p[@class='title']");
            	List<HtmlParagraph> body = page.getByXPath("//p[@class='body']");
            	PropertyDescription propDesc = new PropertyDescription(i+1,title.get(0).asText(),body.get(0).asText());
            	System.out.println(propDesc.getTitle());
            	System.out.println(propDesc.getBody());
            	Gson gson = new GsonBuilder().registerTypeAdapter(PropertyDescription.class, new PropDescSerializer()).create();
            	String json = gson.toJson(propDesc);
            	String dirName = mainDirectory +"/" +  Integer.toString(i+1);
            	writeFile(dirName + "/result-desc.json",json);
            }
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
        
        
        System.out.println("Succesfully Retrieved");
    }

}