package com.example.back.sys.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class TokenGenerate {

    private static final Integer EXPIRE_TIME = 120 * 60 * 1000;
    private static final String TOKEN_SECRET = "tokenqkj";  //密钥盐

    public String generateToken(String username) {
        String token = null;
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("username", username)
                    .withExpiresAt(expiresAt)
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return token;
    }

    /**
     * 验证令牌的签名是否有效。
     *
     * @param token 要验证的令牌字符串
     * @return 如果签名有效返回 true，否则返回 false
     */
    public static boolean verify(String token) {
        System.out.println("执行了token验证代码");

        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
            DecodedJWT jwt = verifier.verify(token);
            System.out.println("认证通过：");
            System.out.println("issuer: " + jwt.getIssuer());
            System.out.println("username: " + jwt.getClaim("username").asString());
            System.out.println("过期时间：      " + jwt.getExpiresAt());
            return true;
        } catch (RuntimeException e) {
            System.out.println("没通过");
            return false;
        }
    }
}