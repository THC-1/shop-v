package com.example.gobuy.modules.admin.assembler;

import com.example.gobuy.modules.admin.dto.RoleCreateDTO;
import com.example.gobuy.modules.admin.entity.Permission;
import com.example.gobuy.modules.admin.entity.Role;
import com.example.gobuy.modules.admin.vo.PermissionSimpleVO;
import com.example.gobuy.modules.admin.vo.PermissionTreeVO;
import com.example.gobuy.modules.admin.vo.RoleVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleAssembler {

    Role toEntity(RoleCreateDTO dto);

    RoleVO toVO(Role entity);

    List<RoleVO> toVOList(List<Role> entities);

    PermissionSimpleVO toSimpleVO(Permission entity);

    List<PermissionSimpleVO> toSimpleVOList(List<Permission> entities);

    PermissionTreeVO toTreeVO(Permission entity);

    List<PermissionTreeVO> toTreeVOList(List<Permission> entities);
}
