package me.twocoffee.entity;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.code.morphia.annotations.Embedded;

/**
 * 商品
 * 
 * @author leon
 * 
 */
@Embedded
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ProductPayload {
	/** 数量 */
	private int amount;
	/**
	 * 在本地服务器上的图片地址
	 */
	private String archivePicture;
	/** 描述、备注 */
	private String description;
	/** 商品的名称 **/
	private String name;
	/** 商品的图片 */
	private String picture;
	/** 商品图片文件的尺寸。单位是byte */
	private int size;

	/** 价格， 主要精度 */
	private String price;

	public int getAmount() {
		return amount;
	}

	public String getArchivePicture() {
		return archivePicture;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public String getPicture() {
		return picture;
	}

	public String getPrice() {
		return price;
	}

	public ProductPayload setAmount(int amount) {
		this.amount = amount;
		return this;
	}

	public void setArchivePicture(String archivePicture) {
		this.archivePicture = archivePicture;
	}

	public ProductPayload setDescription(String description) {
		this.description = description;
		return this;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProductPayload setPicture(String picture) {
		this.picture = picture;
		return this;
	}

	public ProductPayload setPrice(String price) {
		this.price = price;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("name [").append(name).append("] image [")
				.append(picture).append("] price [").append(price).append("]");

		return builder.toString();
	}

	public ProductPayload setSize(int size) {
		this.size = size;
		return this;
	}

	public int getSize() {
		return size;
	}

}
