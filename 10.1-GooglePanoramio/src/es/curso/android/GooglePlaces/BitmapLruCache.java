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

package es.curso.android.GooglePlaces;

import android.graphics.Bitmap;
import android.util.LruCache;

public class BitmapLruCache
extends LruCache<String, Bitmap> implements com.android.volley.toolbox.ImageLoader.ImageCache {

    public BitmapLruCache() {
        this(getDefaultLruCacheSize());
    }

    public BitmapLruCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }

    public static int getDefaultLruCacheSize() {
    	// Data for Nexus 5
    	// MaxMemory = 196608 KBytes ~ 196MB 
    	// CacheSize = 24576 KBytes ~ 24MB
    	
        final int maxMemory = (int) 
        		(Runtime.getRuntime().maxMemory() / 1024);
        
        final int cacheSize = maxMemory / 8;
        
        return cacheSize;
    }
}