package eu.blky.wdb.fb2parser;
/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  10.05.2012::15:10:01<br> 
 */
public class Fb2Book {
    private String type;
    private String name;
    private String address;
    private String city;
    private String province;
    private String postalcode;
    private String country;
    private String telephone;

    public String toString(){
    	return ""
        + "/"+type
        + "/"+name
        + "/"+address
        + "/"+city
        + "/"+province
        + "/"+postalcode
        + "/"+country
        + "/"+telephone;
    }
    
    public void addContact(Object o){
    	System.out.println(o);
    }
    
    public void setType(String newType)
    {
        type = newType;
    }
    public String getType()
    {
        return type;
    }

    public void setName(String newName)
    {
        name = newName;
    }
    public String getName()
    {
        return name;
    }

    public void setAddress(String newAddress)
    {
        address = newAddress;
    }
    public String getAddress()
    {
        return address;
    }

    public void setCity(String newCity)
    {
        city = newCity;
    }
    public String getCity()
    {
        return city;
    }

    public void setProvince(String newProvince)
    {
        province = newProvince;
    }
    public String getProvince()
    {
        return province;
    }

    public void setPostalcode(String newPostalcode)
    {
        postalcode = newPostalcode;
    }
    public String getPostalcode()
    {
        return postalcode;
    }

    public void setCountry(String newCountry)
    {
        country = newCountry;
    }
    public String getCountry()
    {
        return country;
    }

    public void setTelephone(String newTelephone)
    {
        telephone = newTelephone;
    }
    public String getTelephone()
    {
        return telephone;
    }
}


 