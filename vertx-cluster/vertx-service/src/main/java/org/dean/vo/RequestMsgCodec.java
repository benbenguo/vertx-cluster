package org.dean.vo;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

import java.io.*;

public class RequestMsgCodec implements MessageCodec<Request, Request> {

	/**
	 * 将消息实体封装到Buffer用于传输
	 * 
	 * 实现方式： 使用对象流从对象中获取Byte数组然后追加到Buffer
	 */
	@Override
	public void encodeToWire(Buffer buffer, Request s) {
		final ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o;
		try {
			o = new ObjectOutputStream(b);
			o.writeObject(s);
			o.close();
			buffer.appendBytes(b.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 从buffer中获取传输的消息实体
	 */
	@Override
	public Request decodeFromWire(int pos, Buffer buffer) {
		byte[] bytes = buffer.getBytes(pos, buffer.length());
		final ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		ObjectInputStream o = null;
		Request msg = null;
		try {
			o = new ObjectInputStream(b);
//			ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(buffer.getBytes())));
			msg = (Request) o.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * 如果是本地消息则直接返回
	 */
	@Override
	public Request transform(Request s) {
		return s;
	}

	/**
	 * 编解码器的名称： 必须唯一，用于发送消息时识别编解码器，以及取消编解码器
	 */
	@Override
	public String name() {
		return "RequestMsgCodec";
	}

	/**
	 * 用于识别是否是用户编码器 自定义编解码器通常使用-1
	 */
	@Override
	public byte systemCodecID() {
		return -1;
	}

}
