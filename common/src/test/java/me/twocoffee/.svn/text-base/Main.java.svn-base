package me.twocoffee;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;


public class Main {
	public static void main(String[] args) throws Exception {
//		int b0=0x90;
//		int b1=0x01;
//		int b2=0x00;
//		int b3=0x00;
//		
		int b0=9*16;
		int b1=1;
		int b2=0;
		int b3=0;
		
		byte[] bts = new byte[4];
		
		
		bts[0] = (byte)b0;
		bts[1] = (byte)b1;
		bts[2] = (byte)b2;
		bts[3] = (byte)b3;
		
		System.out.println(bts[0]);
		
		InputStream fileInput = new ByteArrayInputStream(bts);

		DataInputStream dataInput = new DataInputStream(fileInput);
		
		System.out.println(dataInput.readInt());
	}
}