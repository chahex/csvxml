package com.ea.throttle.mock;

import java.util.Random;

public class IP {

	String ip;
	String country;

	static Random r2IP = new Random(123456);

	static String[]	countries = {"United States", "Canada", "Mexico", "Cuba", "Chile", "Brazil", "Spain", "German",
			"India", "Pakistan", "England", "China"};

	/**
	 * You can make this guy complicated.
	 *
	 * @return
	 */
	static String randomIPAddress()
	{
		int a = r2IP.nextInt(254) + 1;
		int b = r2IP.nextInt(254) + 1;
		int c = r2IP.nextInt(254) + 1;
		int d = r2IP.nextInt(254) + 1;
		String ip = String.format("%d.%d.%d.%d", a, b, c, d);
		return ip;
	}

	private IP(){}

	public static IP getRandomIP(){
		IP ip = new IP();
		ip.ip = randomIPAddress();
		ip.country = countries[r2IP.nextInt(countries.length)];
		return ip;
	}

}
