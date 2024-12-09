package ma.tr.livr_coding.dto;

public record RefreshResponse(
        Long expTime
        ,
        String accessToken
) {
}
