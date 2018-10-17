package com.sdocean.map.model;

public class MapModel {
	private int id;
	private String url;
	private String format;
	private String version;
	private String tiled;
	private String styles;
	private String transparent;
	private String layers;
	private String code;
	private int initZoom;
	private int maxZoom;
	private int minZoom;
	
	public String getTiled() {
		return tiled;
	}
	public void setTiled(String tiled) {
		this.tiled = tiled;
	}
	public int getInitZoom() {
		return initZoom;
	}
	public void setInitZoom(int initZoom) {
		this.initZoom = initZoom;
	}
	public int getMaxZoom() {
		return maxZoom;
	}
	public void setMaxZoom(int maxZoom) {
		this.maxZoom = maxZoom;
	}
	public int getMinZoom() {
		return minZoom;
	}
	public void setMinZoom(int minZoom) {
		this.minZoom = minZoom;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getStyles() {
		return styles;
	}
	public void setStyles(String styles) {
		this.styles = styles;
	}
	public String getTransparent() {
		return transparent;
	}
	public void setTransparent(String transparent) {
		this.transparent = transparent;
	}
	public String getLayers() {
		return layers;
	}
	public void setLayers(String layers) {
		this.layers = layers;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
