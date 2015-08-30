package org.ship.shipservice.oil;


public class TestOil extends TestBase{
	public static void main(String[] args) throws Exception {
		//testCity();
		testOilStatin();
		//testOsDetail();
    }
	
	public static void testCity(){
		String url = "http://localhost:8080/shipService/api/v1/os/city";
		System.out.println("ccc ="+httpGet(url));
	}
	
	public static void testOilStatin(){
		String url = "http://localhost:8080/shipService/api/v1/os/station?cityId=1";
		System.out.println("ccc ="+httpGet(url));
	}
	
	public static void testOsDetail(){
		String url = "http://localhost:8080/shipService/api/v1/os/detail?osId=1";
		System.out.println("ccc ="+httpGet(url));
	}
}
