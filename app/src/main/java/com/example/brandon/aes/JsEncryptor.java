package com.example.brandon.aes;

import android.app.Activity;


import com.evgenii.jsevaluator.JsEvaluator;
import com.evgenii.jsevaluator.interfaces.JsCallback;
import com.evgenii.jsevaluator.interfaces.JsEvaluatorInterface;

import java.io.IOException;
import java.util.ArrayList;

/** Encrypts text using JavaScript library */
public class JsEncryptor{
	public static JsEncryptor evaluateAllScripts(Activity context) {
		final AssetsFileReader assetsFileReader = new AssetsFileReader(context);
		final JsEvaluator jsEvaluator = new JsEvaluator(context);
		final JsEncryptor jsEncryptor = new JsEncryptor(assetsFileReader, jsEvaluator);
		try {
			jsEncryptor.readScripts();
		} catch (final IOException e) {

		}
		return jsEncryptor;
	}

	private final AssetsFileReader mAssetsFileReader;
	private final JsEvaluatorInterface mJsEvaluator;
	private final String cryptoJsFileNames = "crypto_js";
	private final String aesCryptoFileName = "aes_crypto";
	private final String jsRootDir = "javascript";

	private static final String prefix = "AESCryptoV10";

	private ArrayList<String> mScriptsText;

	public JsEncryptor(AssetsFileReader assetsFileReader, JsEvaluatorInterface jsEvaluator) {
		mAssetsFileReader = assetsFileReader;
		mJsEvaluator = jsEvaluator;
	}

	public String concatenateScripts() {
		final StringBuilder stringBuilder = new StringBuilder();
		final ArrayList<String> scripts = getScripts();
		for (final String scriptText : scripts) {
			stringBuilder.append(scriptText);
			stringBuilder.append("; ");
		}

		return stringBuilder.toString();
	}


	public void decrypt(String text, String password, JsCallback callback) {
		final String libraryJsCode = concatenateScripts();
		mJsEvaluator.callFunction(libraryJsCode, callback, "aesCrypto.decrypt", text, password);
	}


	public void encrypt(String text, String password, JsCallback callback) {
		final String libraryJsCode = concatenateScripts();
		mJsEvaluator.callFunction(libraryJsCode, callback, "aesCrypto.encrypt", text, password);
	}

	public ArrayList<String> getScripts() {
		if (mScriptsText == null) {
			mScriptsText = new ArrayList<String>();
		}
		return mScriptsText;
	}


	public boolean isEncrypted(String text) {
		if (text == null)
			return false;

		return text.trim().startsWith(prefix);
	}

	public void readScripts() throws IOException {
		final ArrayList<String> scriptsToLoad = new ArrayList<String>();

		scriptsToLoad.add(jsRootDir + "/" + cryptoJsFileNames + ".js");
		scriptsToLoad.add(jsRootDir + "/" + aesCryptoFileName + ".js");

		final ArrayList<String> scriptsText = getScripts();

		for (final String scriptName : scriptsToLoad) {
			scriptsText.add(mAssetsFileReader.ReadFile(scriptName));
		}
	}
}
