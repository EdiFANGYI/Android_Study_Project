package com.haha;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	public static void main(String[] args) {
		// md5����
		// 1.��ϢժҪ��
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			String password = "12356";
			// 2.���byte����
			byte bytes[] = digest.digest(password.getBytes());
			StringBuffer buffer = new StringBuffer();

			// 3.ÿһ��byte����8��������λ��������
			for (byte b : bytes) {
				int number = b & 0xff;

				// 4.��int����ת����ʮ������
				String numberStr = Integer.toHexString(number);
				System.out.println();
				// 5.����Ĳ�ȫ
				if (numberStr.length() == 1) {
					buffer.append("0");
				}

				buffer.append(numberStr);

			}
			// buffer.toString();��׼��md5���ܺ�Ľ��
			System.out.println(buffer.toString());

		} catch (NoSuchAlgorithmException e) {
			// û��Ԥ��׼����˵���쳣
			e.printStackTrace();
		}
	}
}
