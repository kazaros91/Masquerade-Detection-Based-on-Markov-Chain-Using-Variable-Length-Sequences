package pojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class UserBehaviorBuilder {
	
	private UserBehavior userBehavior;
	
	public UserBehaviorBuilder() {
		userBehavior = new UserBehavior();
	}
	
	public UserBehaviorBuilder addUserId(int userId)
	{
		userBehavior.setUserId(userId);
		return this;
	}
	
	public UserBehaviorBuilder addItemId(int itemId)
	{
		userBehavior.setItemId(itemId);
		return this;
	}
	
	public UserBehaviorBuilder addBehaviorType(int behaviorType)
	{
		userBehavior.setBehaviorType(behaviorType);
		return this;
	}
	
	public UserBehaviorBuilder addUserGeohash(String userGeohash)
	{
		userBehavior.setUserGeohash(userGeohash);
		return this;
	}
	
	public UserBehaviorBuilder addItemCategory(int itemCategory)
	{
		userBehavior.setItemCategory(itemCategory);
		return this;
	}
	
	public UserBehaviorBuilder addTime(String dateTime)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh");
		java.util.Date parsedDate;
		try 
		{
			parsedDate = dateFormat.parse(dateTime);
			java.sql.Timestamp timestamp = new java.sql.Timestamp( parsedDate.getTime() );
			userBehavior.setDateTime(timestamp);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		
		return this;
	}
	
	public UserBehavior get()
	{
		return userBehavior;
	}
	
}
