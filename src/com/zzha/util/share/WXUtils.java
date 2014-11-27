package com.zzha.util.share;

import android.content.Context;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX.Req;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXUtils {

	public static final String APP_ID = "wx743b1e0852ac74a9";
	public static final String APP_SECRET = "f8bd52666ee831ca370e1251a4747e4f";

	private static IWXAPI iwxapi;
	private static Context context;

	public WXUtils() {
		super();
	}

	public WXUtils(Context context) {
		super();
		this.context = context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public static void registerToWX(Context context) {
		iwxapi = WXAPIFactory.createWXAPI(context, APP_ID, true);
		iwxapi.registerApp(APP_ID);
	}

	public void sendTextToWX(String text) {

		if (!isWXAppInstalled()) {
			return;
		}

		WXTextObject textObject = new WXTextObject();
		textObject.text = text;

		WXMediaMessage mediaMessage = new WXMediaMessage();
		mediaMessage.mediaObject = textObject;
		mediaMessage.description = text;

		SendMessageToWX.Req req = new Req();
		req.message = mediaMessage;
		req.scene = Req.WXSceneSession;
		req.transaction = System.currentTimeMillis() + "";

		iwxapi.sendReq(req);
	}

	public static boolean isWXAppInstalled() {
		boolean isInstalled = iwxapi.isWXAppInstalled();
		if (!isInstalled) {
			Toast.makeText(context, "您未安装微信!", 0).show();
		}
		return isInstalled;
	}
}
