package com.example.gobuy.modules.admin.assembler;

import com.example.gobuy.modules.admin.vo.AdminUserDetailVO;
import com.example.gobuy.modules.admin.vo.AdminUserVO;
import com.example.gobuy.modules.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminUserAssembler {
    AdminUserVO toVO(User user);
    List<AdminUserVO> toVOList(List<User> users);
    AdminUserDetailVO toDetailVO(User user);
}
