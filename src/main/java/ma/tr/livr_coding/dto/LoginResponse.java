package ma.tr.livr_coding.dto;

import ma.tr.livr_coding.entity.User;

public record LoginResponse(
        User authUser
        ,
        String token
        ,
        String refreshToken
        ,
        Long expiresIn
) {
}
