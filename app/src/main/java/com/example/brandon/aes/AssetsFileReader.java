package com.example.brandon.aes;

import android.content.Context;
import android.content.res.AssetManager;


import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


public class AssetsFileReader{
	private final Context mContext;

	public AssetsFileReader(Context context) {
		mContext = context;
	}

	//---------------------------------
	// READ JAVASCRIPT FILES
	//---------------------------------

	public String ReadFile(String fileName) throws IOException {
		final AssetManager am = mContext.getAssets();
		final InputStream inputStream = am.open(fileName);

		final Scanner scanner = new Scanner(inputStream, "UTF-8");
		final String text = scanner.useDelimiter("\\A").next();
		scanner.close();
		return text;
	}
}
