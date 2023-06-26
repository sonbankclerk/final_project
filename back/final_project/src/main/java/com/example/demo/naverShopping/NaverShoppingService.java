package com.example.demo.naverShopping;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;


@Service
public class NaverShoppingService {
	
	final String baseURL = "https://openapi.naver.com/v1/search/shop.json?display=50&start=1&query=";
	
	// 우선 URL을 이용하여 요청 값을 받는다.
	// 아래의 search 메서드는 주가 되는 메서드이고, 결과 값으로 Map 형식의 결과를 배출한다.
	// map안에는 네이버 list들이 들어있다. 갖다 쓰면 된다.
	public Map search(String query) {
		// naver에 요청할 때 필요한  method와 property들을 담을 urlConnection
		HttpURLConnection con = null;

		// naver에서 준 결과를 json형태가 아닌 String 형태로 일단 받고 다른 메서드에 넣어 원하는 결과를 뽑는다.
		String result = "";
		
		try {
			// 영어만 들어오면 상관이 없지만 한글이 들어올 수 있으므로 utf-8로 인코딩 해준다.
			// 혹여나 인코딩 에러 SE-06이 뜨면 아래와 같이 encoding이 잘 되어 있는 지 확인해준다.
			query = URLEncoder.encode(query,"utf-8");
			// 검색어가 함께 들어 있는 전체 URL
			String newURL = baseURL+query;
			URL url = new URL(newURL);
			con = (HttpURLConnection)url.openConnection();
			
			// naver에 줘야할 cliient Id와 Secret, method 
			con.setRequestMethod("GET");
			con.setRequestProperty("X-Naver-Client-Id", "CE8LDMxK9axt3zA_KhBo");
			con.setRequestProperty("X-Naver-Client-Secret", "jc995gq69R");
			
			//  결과를 잘 받아왔는 지 확인한다.
			int responseCode = con.getResponseCode();
			if(responseCode == HttpURLConnection.HTTP_OK) {
				// 잘 받아왔다면 결과 값을 받아오자.
				// readBody는 바로 아래에 있는 private method이다.
				// readBody를 이해하기 위해서 아래에 있는 readBody method를 확인하자.
				result = readBody(con.getInputStream());
			}else {
				// error가 뜬다면 error 메시지를 보내준다.
				result = readBody(con.getErrorStream());
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("연결 오류");
		}finally {
			con.disconnect();
		}
		
		// 받은 결과값으로 getResult라는 method에서 Map 형태로 list를 받아온다.
		// 키는 "list", 결과는 ArrayList형태로 받아와진다.
		// getResult의 반환값이 map이므로 search의 반환값으로 바로 반환할 수 있다.
		return getResult(result);
	}
	
	private String readBody(InputStream body) {
		// json 형태로 받아오는 것 같은데 이 json을 읽기위한 InputStreamReader를 만든다.
		InputStreamReader streamReader = new InputStreamReader(body);
		
		// lineReader라고 한 이유는 한 줄 씩 읽기위함.
		// BufferedReader는 wrapper class로 굳이 이용안해도 되지만 읽는 속도가 상당히 빨라지므로 이용한다.
		try (BufferedReader lineReader = new BufferedReader(streamReader)){
			// 결과 값을 하나의 String으로 만들기 위한 StringBuilder
			StringBuilder responseBody = new StringBuilder();
			String line;
			// 혹시 읽고 있는 그 줄이 null 이 아니면 StringBuilder에 append한다.
			while((line = lineReader.readLine()) != null) {
				responseBody.append(line);
			}
			
			// 계속 append되어 완성된 StringBuilder를 String으로 바꾼 후 반환한다.
			return responseBody.toString();
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("API 응답을 불러오기 실패",e);
		}
	}
	
	private Map getResult(String response) {
		
		// 우리가 vue에서 이용할 값들이 들어있는 map 생성.
		Map map = new HashMap<>();
		try {

			// 여기서 어떤 값을 받아오는 지 확인할 수 있다.
			System.out.println(response);
			
			// String을 Json형태로 이해하기 위한 parsing작업을 위한 parser
			JSONParser parser = new JSONParser();

			// 여기부터는 이해할 것이라고 믿는다.
			JSONObject object = (JSONObject)parser.parse(response);
			JSONArray items = (JSONArray) object.get("items");
			
			ArrayList<NaverShoppingDto> resultList = new ArrayList<>();
			
			for(int i = 0; i < items.size(); i++) {
				JSONObject item = (JSONObject) items.get(i);
				NaverShoppingDto dto = new NaverShoppingDto();
				String title = (String)item.get("title");
				String link = (String)item.get("link");
				String img = (String)item.get("image");
				String mallName = (String)item.get("mallName");
				String maker = (String)item.get("maker");
				long productId = Long.parseLong((String)item.get("productId"));

				// lprice가 혹시나 없을 수도 있다.
				// 다른 것들은 무조건 있거나 String으로 값을 받기 때문에 null이 들어오거나 에러가 나지 않지만
				// lprice에 null이 들어오면 Integer.parseInt(null)이 되므로 NumberFormatException이 나올 수 있다.
				// null이나 빈 값이 들어올 때를 방지하여 조건문을 걸어준다.
				// 만약 값이 없다면 0이 기본값이 된다.
				String lpriceObject = (String)item.get("lprice");
				int price = 0;
				if(!lpriceObject.equals("")) {
					price = Integer.parseInt((String)item.get("lprice"));
				}
				
				dto.setProductId(productId);
				dto.setTitle(title);
				dto.setLink(link);
				dto.setImg(img);
				dto.setMallName(mallName);
				dto.setMaker(maker);
				dto.setPrice(price);
				
				// 결과로 반환할 list에 dto를 추가한다.
				resultList.add(dto);
			}
			
			// 완성된 list를 map에 넣는다.
			map.put("list", resultList);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		// 완성된 map 반환.
		return map;
	}
		
}
