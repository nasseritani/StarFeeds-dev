package com.nader.starfeeds.data.componenets.model;

import com.nader.starfeeds.data.componenets.Provider;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private String id;
    private String name;
    private String email;
    private Provider provider;
    private String country;
    private int followersCount;


    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        country = null;
        provider=null;
    }

    public User(String id, String name, String email, String country) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.country = country;
        provider=null;
    }

    public User(JSONObject userJSON) {
        try {
            if(!userJSON.isNull("id")) this.id = userJSON.getString("id");
            if (!userJSON.isNull("name")) this.name = userJSON.getString("name");
            if(!userJSON.isNull("email")) this.email = userJSON.getString("email");
            if(!userJSON.isNull("country")) this.country = userJSON.getString("country");
            if(!userJSON.isNull("followers_count")) this.followersCount = userJSON.getInt("followers_count");

        } catch(JSONException e) {

        }
    }
    public void setProvider(Provider provider){
        this.provider = provider;
    }
    public Provider getProvider(){
        return provider;
    }
    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        if (name == null)
        return "id "+id+"Email: "+email;
        else return "Name: "+name+" Email: "+email;
    }

    public String getCountry() {
        return country;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toJSONString() {
        return toJSON().toString();
    }

    /**
     *
     */
    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        try {
            if (id != null) jsonObject.put("id", id);
            if (name != null)  jsonObject.put("name", name);
            if (email != null) jsonObject.put("email", email);
            if (country != null) jsonObject.put("country", country);
            if (provider!=null) jsonObject.put("provider", provider.toString());
            if (followersCount != 0) jsonObject.put("followers_count", followersCount);
        } catch (JSONException e) {
            // nothing to be done
        }
        return jsonObject;
    }

    public void updateUser(User newUser) {
        if (!newUser.name.equals(name)) name = newUser.getName();
        if (!newUser.email.equals(email)) email = newUser.getEmail();
        if (!newUser.country.equals(country)) country = newUser.getCountry();
    }

    public int getFollowersCount() {
        return followersCount;
    }
}
