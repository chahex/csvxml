package com.ea.throttle.mock;

enum Suspect{
	good,
	bad,
	between;

	private static final java.util.Random rdm = new java.util.Random();

	/**
	 * If the nextInt(probForBad) <= 1, return bad.
	 * Means the probability for bad is 1 / probForBad
	 *
	 * @param probForBad
	 * @return
	 */
	static Suspect randomSuspect(int probForBad)
	{
		int i = rdm.nextInt(probForBad);
		if(i == 0)
		{
			return bad;
		}else if(i == probForBad - 1){
			return between;
		}else{
			return good;
		}
	}

}

public class User{

	String userId;
	String email;  // unique, will use the id@ea.com
	Suspect suspect;  // either good or bad
	UserGroup group;

	/**
	 *
	 * @param userId
	 * @param probForBad
	 * @param IP If null, will generate a random IP address
	 */
	User(int userId, int probForBad)
	{
		this.userId = String.format("%d", userId);
		this.email = String.format("%d@cx.com", userId);
		this.suspect = Suspect.randomSuspect(probForBad);
	}

}