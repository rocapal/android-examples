/*
 *
 *  Copyright (C) Roberto Calvo Palomino
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see http://www.gnu.org/licenses/. 
 *
 *  Author : Roberto Calvo Palomino <rocapal at gmail dot com>
 *
 */


package es.curso.android.panoramio;

import android.location.Location;

public class HelperPanoramio {

	public static int NOTIFICATION_PLACES_ID = 1;
	
	public static String NEW_PLACES_NAME = "PLACE_LIST";
	public static String NEW_PLACES_VALUE = "YES";
	
	public static String getUrlFromLocation (Location loc, Integer page)
	{
	
		if (loc == null)
			return null;
		
		return "http://rest.libregeosocial.org/social/layer/560/search/?search=&latitude=" + String.valueOf(loc.getLatitude()) + 
				"&longitude=" + String.valueOf(loc.getLongitude()) + "&radius=1.0&category=0&elems=20&page=" + page.toString() +  "&format=JSON";
	}
	
}
