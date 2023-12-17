package com.example.qlnv.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashEncryption {

    public static String hashPassword(String password) {
        try {
            // Tạo đối tượng MessageDigest với thuật toán SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Chuyển đổi mật khẩu thành mảng byte và băm nó
            byte[] hashedBytes = md.digest(password.getBytes());

            // Chuyển đổi mảng byte thành chuỗi hex
            StringBuilder hexString = new StringBuilder();
            for (byte hashedByte : hashedBytes) {
                String hex = Integer.toHexString(0xff & hashedByte);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            // Trả về chuỗi hex đã băm
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Xử lý ngoại lệ nếu thuật toán không hỗ trợ
        }

        // Trả về null nếu có lỗi
        return null;
    }

}
