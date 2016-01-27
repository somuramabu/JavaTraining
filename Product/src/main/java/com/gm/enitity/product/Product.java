package com.gm.enitity.product;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Product implements java.io.Serializable{
	
	@XmlElement private String id;
	@XmlElement private String type;
	@XmlElement private String description;
	@XmlElement private String longDescription;
	@XmlElement private String imgUrl;
	@XmlElement private String dimentions;
	@XmlElement private boolean sellable;
	public boolean getSellable() {
		return sellable;
	}
	public void setSellable(boolean sellable) {
		this.sellable = sellable;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLongDescription() {
		return longDescription;
	}
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getDimentions() {
		return dimentions;
	}
	public void setDimentions(String dimentions) {
		this.dimentions = dimentions;
	}
	
	
}
