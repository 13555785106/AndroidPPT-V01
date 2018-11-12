package com.sample.app12306.http;

public interface HttpDownloadProgress {
	void onStart(String fileName, int length);

	void onProgress(byte[] bytes, int count);

	void onFinish();
}
