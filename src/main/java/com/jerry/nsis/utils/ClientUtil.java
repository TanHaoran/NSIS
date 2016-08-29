package com.jerry.nsis.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 客户端读取服务json的服务类
 * 
 * @author Jerry
 * 
 */
public class ClientUtil {

	public static void doPostAsyn(final String urlStr, final String params,
			final CallBack callBack) throws Exception {
		new Thread() {
			public void run() {
				try {
					String result = doPost(urlStr, params);
					if (callBack != null) {
						callBack.onRequestComplete(result);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * post回传信息
	 * 
	 * @param postUrl
	 * @param json
	 * @return 1:成功，0：失败
	 */
	public static String doPost(final String postUrl, final String json) {

		String result = "";
		int code = -1;
		try {
			URL url = new URL(postUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(1000 * 100);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept-Charset", "gb2312");
			conn.connect();
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			out.writeBytes(json);
			out.flush();
			out.close();
			code = conn.getResponseCode();
			System.out.println("响应吗:" + code);
			if (200 == code) {
				InputStream in = conn.getInputStream();
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				byte buff[] = new byte[1024];
				int len = 0;
				while ((len = in.read(buff)) != -1) {
					os.write(buff, 0, len);
				}
				os.flush();
				byte[] data = os.toByteArray();
				result = new String(data);

				os.close();
				in.close();
			}

		} catch (Exception e1) {
			e1.printStackTrace();	
		}
		return result;

	}

	public interface CallBack {
		void onRequestComplete(String result);
	}

}
