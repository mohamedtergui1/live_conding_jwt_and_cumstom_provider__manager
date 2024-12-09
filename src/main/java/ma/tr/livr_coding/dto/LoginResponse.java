package ma.tr.livr_coding.dto;

import ma.tr.livr_coding.domain.entity.User;

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
