package com.example.gobuy.modules.admin.assembler;

import com.example.gobuy.modules.admin.entity.Admin;
import com.example.gobuy.modules.admin.entity.Role;
import com.example.gobuy.modules.admin.vo.AdminDetailVO;
import com.example.gobuy.modules.admin.vo.AdminVO;
import com.example.gobuy.modules.admin.vo.RoleSimpleVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminAssembler {

    AdminVO toVO(Admin entity);

    List<AdminVO> toVOList(List<Admin> entities);

    AdminDetailVO toDetailVO(Admin entity);

    RoleSimpleVO toRoleSimpleVO(Role entity);

    List<RoleSimpleVO> toRoleSimpleVOList(List<Role> entities);
}
