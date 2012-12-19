package es.curso.android.panoramio.parser;

import java.io.Serializable;

public class PanoramioNode implements Serializable
{				
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2019009146620494209L;
	
	
	public String name;
	public String info_url;
	public String photo_url;
	public String thumb_url;
	
	public Double latitude;
	public Double longitude;
	
	public String date;
}