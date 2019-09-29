package pojo;

import java.util.Comparator;


public class UserBehavior {
	
	private int userBehaviorId;
	private int userId; 
	private int itemId;
	private int behaviorType;
	private String userGeohash;
	private int itemCategory;
	private java.sql.Timestamp dateTime;
	
	
	public void setuserBehaviorId(final int userBehaviorId)
	{
		this.userBehaviorId = userBehaviorId;
	}
	
	public int getUserBehaviorId()
	{
		return userBehaviorId;
	}
	
	public void setUserId(final int userId)
	{
		this.userId = userId;
	}
	
	public int getUserId()
	{
		return userId;
	}
	
	public void setItemId(final int itemId)
	{
		this.itemId = itemId;
	}
	
	public int getItemId()
	{
		return itemId;
	}
    
	public void setBehaviorType(final int behaviorType)
	{
		this.behaviorType = behaviorType;
	}
	
	public int getBehaviorType()
	{
		return behaviorType;
	}
	
	public void setUserGeohash(final String userGeohash)
	{
		this.userGeohash = userGeohash;
	}
	
	
	public String getUserGeohash()
	{
		return userGeohash;
	}
	
	public void setItemCategory(final int itemCategory)
	{
		this.itemCategory = itemCategory;
	}
	
	
	public int getItemCategory()
	{
		return itemCategory;
	}
	
	public void setDateTime(final java.sql.Timestamp dateTime)
	{
		this.dateTime = dateTime;
	}
	
	
	public java.util.Date getDateTime()
	{
		return dateTime;
	}
	
	public static Comparator<UserBehavior> TimeComparator
		= new Comparator<UserBehavior>() {
			public int compare (UserBehavior u1, UserBehavior u2) {
				java.util.Date t1 = u1.getDateTime();
				java.util.Date t2 = u2.getDateTime();
				// ascending order
				return t1.compareTo(t2);
			}
		};
		
}
