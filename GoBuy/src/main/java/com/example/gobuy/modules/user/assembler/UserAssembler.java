package com.example.gobuy.modules.user.assembler;

import com.example.gobuy.modules.user.entity.User;
import com.example.gobuy.modules.user.dto.UserRegisterDTO;
import com.example.gobuy.modules.user.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserAssembler {

    User toEntity(UserRegisterDTO dto);

    UserVO toVO(User entity);

    List<UserVO> toVOList(List<User> entityList);
}
