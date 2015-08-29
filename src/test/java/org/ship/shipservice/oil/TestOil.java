package org.ship.shipservice.oil;


public class TestOil extends TestBase{
	public static void main(String[] args) throws Exception {
		testCity();
		//testOilStatin();
    }
	
	public static void testCity(){
		String url = "http://localhost:8080/shipService/api/v1/oil/city";
		System.out.println("ccc ="+httpGet(url));
	}
	
	public static void testOilStatin(){
		String url = "http://localhost:8080/shipService/api/v1/oil/station?cityId=1";
		System.out.println("ccc ="+httpGet(url));
	}
}
