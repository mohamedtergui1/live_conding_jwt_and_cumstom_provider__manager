package ma.tr.livr_coding.mapper;

import ma.tr.livr_coding.entity.User;
import ma.tr.livr_coding.dto.RegisterRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    User toEntity(RegisterRequest registerRequest);
}
