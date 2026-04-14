import os
import re

sql_file = r'd:\Java_study\shop_v3\GoBuy\sql\shop_v3_schema.sql'
base_pkg = 'com.example.gobuy.modules'
base_dir = r'd:\Java_study\shop_v3\GoBuy\src\main\java\com\example\gobuy\modules'

if not os.path.exists(base_dir):
    os.makedirs(base_dir)

def table_to_class(table_name):
    # e.g., order_items -> OrderItem, users -> User
    words = table_name.split('_')
    # try to singularize the last word if it ends with 's'
    if words[-1].endswith('s'):
        words[-1] = words[-1][:-1]
    # Handle specific exceptions if any
    if words[-1] == 'addresse': words[-1] = 'address'
    if table_name == 'addresses': return 'Address' # Override logic above safely
    if table_name == 'scenarios': return 'Scenario'
    return ''.join(w.capitalize() for w in words)

def table_to_module(table_name):
    words = table_name.split('_')
    if words[-1].endswith('s'):
        words[-1] = words[-1][:-1]
    if table_name == 'addresses': return 'address'
    if table_name == 'scenarios': return 'scenario'
    if table_name == 'order_items': return 'order' # group with order
    return words[0].lower()

def snake_to_camel(name):
    words = name.split('_')
    return words[0] + ''.join(w.title() for w in words[1:])

def map_db_type_to_java(db_type):
    db_type = db_type.upper()
    if 'BIGINT' in db_type: return 'Long'
    if 'INT' in db_type: return 'Integer'
    if 'TINYINT' in db_type: return 'Integer'
    if 'DECIMAL' in db_type: return 'BigDecimal'
    if 'DATETIME' in db_type or 'TIMESTAMP' in db_type: return 'LocalDateTime'
    if 'JSON' in db_type: return 'String'
    return 'String'

def parse_sql():
    with open(sql_file, 'r', encoding='utf-8') as f:
        content = f.read()

    # match CREATE TABLE `table_name` (...) COMMENT='...'
    table_pattern = re.compile(r"CREATE TABLE `?(\w+)`?\s*\((.*?)\)\s*ENGINE=.*?COMMENT='(.*?)';", re.DOTALL)
    tables = table_pattern.findall(content)

    parsed_tables = []
    
    for t_name, t_body, t_comment in tables:
        fields = []
        lines = t_body.strip().split('\n')
        for line in lines:
            line = line.strip()
            if line.startswith('`'):
                # Extract field `name` TYPE [NOT NULL] [COMMENT '...']
                m = re.search(r"`(\w+)`\s+(\w+(?:\([\d,\s]+\))?)(.*)", line)
                if m:
                    f_name = m.group(1)
                    f_type = m.group(2)
                    f_rest = m.group(3)
                    
                    is_not_null = 'NOT NULL' in f_rest.upper()
                    is_pk = 'PRIMARY KEY' in f_rest.upper()
                    
                    comment = ''
                    cm = re.search(r"COMMENT\s+'(.*?)'", f_rest)
                    if cm:
                        comment = cm.group(1)
                    
                    fields.append({
                        'name': f_name,
                        'db_type': f_type,
                        'java_type': map_db_type_to_java(f_type),
                        'is_not_null': is_not_null,
                        'is_pk': is_pk,
                        'comment': comment
                    })
        
        parsed_tables.append({
            'table_name': t_name,
            'table_comment': t_comment,
            'fields': fields
        })
    return parsed_tables

def generate_entity(t):
    module = table_to_module(t['table_name'])
    cls = table_to_class(t['table_name'])
    pkg = f"{base_pkg}.{module}.entity"
    
    has_date = any(f['java_type'] == 'LocalDateTime' for f in t['fields'])
    has_decimal = any(f['java_type'] == 'BigDecimal' for f in t['fields'])
    
    imports = ["import com.baomidou.mybatisplus.annotation.*;", "import lombok.Data;"]
    if has_date: imports.append("import java.time.LocalDateTime;")
    if has_decimal: imports.append("import java.math.BigDecimal;")
    
    fields_code = ""
    for f in t['fields']:
        camel_name = snake_to_camel(f['name'])
        annotations = []
        if f['is_pk']:
            annotations.append("@TableId(type = IdType.AUTO)")
        elif f['name'] in ['created_at', 'updated_at']:
            # Maybe some auto-fill strategy could be added here
            pass
        
        fields_code += f"    /**\n     * {f['comment']}\n     */\n"
        for a in annotations:
            fields_code += f"    {a}\n"
        fields_code += f"    private {f['java_type']} {camel_name};\n\n"

    return f"""package {pkg};

{chr(10).join(imports)}

/**
 * {t['table_comment']} 实体类
 */
@Data
@TableName("{t['table_name']}")
public class {cls} {{

{fields_code}
}}"""

def generate_dto(t):
    module = table_to_module(t['table_name'])
    cls = table_to_class(t['table_name'])
    pkg = f"{base_pkg}.{module}.dto"
    
    imports = ["import lombok.Data;", "import jakarta.validation.constraints.*;"]
    has_date = any(f['java_type'] == 'LocalDateTime' for f in t['fields'])
    has_decimal = any(f['java_type'] == 'BigDecimal' for f in t['fields'])
    if has_date: imports.append("import java.time.LocalDateTime;")
    if has_decimal: imports.append("import java.math.BigDecimal;")
    
    fields_code = ""
    for f in t['fields']:
        # typically we skip id natively, but let's just include all for simplicity
        if f['name'] in ['created_at', 'updated_at']:
            continue
            
        camel_name = snake_to_camel(f['name'])
        
        annotations = []
        if f['is_not_null'] and not f['is_pk'] and f['name'] != 'status':  # status usually has default
            if f['java_type'] == 'String':
                annotations.append(f'@NotBlank(message = "{f["comment"]}不能为空")')
            else:
                annotations.append(f'@NotNull(message = "{f["comment"]}不能为空")')
                
        fields_code += f"    /**\n     * {f['comment']}\n     */\n"
        for a in annotations:
            fields_code += f"    {a}\n"
        fields_code += f"    private {f['java_type']} {camel_name};\n\n"

    return f"""package {pkg};

{chr(10).join(imports)}

/**
 * {t['table_comment']} DTO (数据传输对象)
 */
@Data
public class {cls}DTO {{

{fields_code}
}}"""

def generate_vo(t):
    module = table_to_module(t['table_name'])
    cls = table_to_class(t['table_name'])
    pkg = f"{base_pkg}.{module}.vo"
    
    imports = ["import lombok.Data;"]
    has_date = any(f['java_type'] == 'LocalDateTime' for f in t['fields'])
    has_decimal = any(f['java_type'] == 'BigDecimal' for f in t['fields'])
    if has_date: imports.append("import java.time.LocalDateTime;")
    if has_decimal: imports.append("import java.math.BigDecimal;")
    
    fields_code = ""
    for f in t['fields']:
        if f['name'] == 'password':
            continue  # secure hide password
            
        camel_name = snake_to_camel(f['name'])
        fields_code += f"    /**\n     * {f['comment']}\n     */\n"
        fields_code += f"    private {f['java_type']} {camel_name};\n\n"

    return f"""package {pkg};

{chr(10).join(imports)}

/**
 * {t['table_comment']} VO (视图对象)
 */
@Data
public class {cls}VO {{

{fields_code}
}}"""

def generate_mapper(t):
    module = table_to_module(t['table_name'])
    cls = table_to_class(t['table_name'])
    pkg = f"{base_pkg}.{module}.mapper"
    
    return f"""package {pkg};

import {base_pkg}.{module}.entity.{cls};
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * {t['table_comment']} Mapper 接口
 */
@Mapper
public interface {cls}Mapper extends BaseMapper<{cls}> {{
}}"""

def generate_assembler(t):
    module = table_to_module(t['table_name'])
    cls = table_to_class(t['table_name'])
    pkg = f"{base_pkg}.{module}.assembler"
    
    return f"""package {pkg};

import {base_pkg}.{module}.entity.{cls};
import {base_pkg}.{module}.dto.{cls}DTO;
import {base_pkg}.{module}.vo.{cls}VO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import java.util.List;

/**
 * {t['table_comment']} 实体转换装配器
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface {cls}Assembler {{

    /**
     * DTO 转 Entity
     * @param dto DTO 对象
     * @return Entity 对象
     */
    {cls} toEntity({cls}DTO dto);

    /**
     * Entity 转 VO
     * @param entity Entity 对象
     * @return VO 对象
     */
    {cls}VO toVO({cls} entity);

    /**
     * Entity 列表 转 VO 列表
     * @param entityList Entity 列表
     * @return VO 列表
     */
    List<{cls}VO> toVOList(List<{cls}> entityList);
}}"""

def generate_iservice(t):
    module = table_to_module(t['table_name'])
    cls = table_to_class(t['table_name'])
    pkg = f"{base_pkg}.{module}.service"
    
    return f"""package {pkg};

import {base_pkg}.{module}.entity.{cls};
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * {t['table_comment']} 服务接口
 */
public interface I{cls}Service extends IService<{cls}> {{
}}"""

def generate_service_impl(t):
    module = table_to_module(t['table_name'])
    cls = table_to_class(t['table_name'])
    pkg = f"{base_pkg}.{module}.service.impl"
    
    return f"""package {pkg};

import {base_pkg}.{module}.entity.{cls};
import {base_pkg}.{module}.mapper.{cls}Mapper;
import {base_pkg}.{module}.service.I{cls}Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * {t['table_comment']} 服务实现层
 */
@Service
public class {cls}ServiceImpl extends ServiceImpl<{cls}Mapper, {cls}> implements I{cls}Service {{
}}"""

def generate_controller(t):
    module = table_to_module(t['table_name'])
    cls = table_to_class(t['table_name'])
    pkg = f"{base_pkg}.{module}.controller"
    
    var_name = cls[0].lower() + cls[1:]
    url_path = t['table_name'].replace('_', '-')
    
    return f"""package {pkg};

import com.example.gobuy.common.result.Result;
import {base_pkg}.{module}.assembler.{cls}Assembler;
import {base_pkg}.{module}.dto.{cls}DTO;
import {base_pkg}.{module}.vo.{cls}VO;
import {base_pkg}.{module}.entity.{cls};
import {base_pkg}.{module}.service.I{cls}Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * {t['table_comment']} 前端控制器
 */
@RestController
@RequestMapping("/api/v1/{url_path}")
@RequiredArgsConstructor
public class {cls}Controller {{

    private final I{cls}Service {var_name}Service;
    private final {cls}Assembler {var_name}Assembler;

    /**
     * 获取{t['table_comment']}列表
     */
    @GetMapping
    public Result<List<{cls}VO>> list() {{
        List<{cls}> list = {var_name}Service.list();
        return Result.success({var_name}Assembler.toVOList(list));
    }}

    /**
     * 获取{t['table_comment']}详情
     */
    @GetMapping("/{{id}}")
    public Result<{cls}VO> getById(@PathVariable("id") Long id) {{
        {cls} entity = {var_name}Service.getById(id);
        if (entity == null) {{
            return Result.fail(404, "未找到相关数据");
        }}
        return Result.success({var_name}Assembler.toVO(entity));
    }}

    /**
     * 新增{t['table_comment']}
     */
    @PostMapping
    public Result<Boolean> save(@Valid @RequestBody {cls}DTO dto) {{
        {cls} entity = {var_name}Assembler.toEntity(dto);
        boolean success = {var_name}Service.save(entity);
        return success ? Result.success(true) : Result.fail(500, "保存失败");
    }}

    /**
     * 修改{t['table_comment']}
     */
    @PutMapping("/{{id}}")
    public Result<Boolean> update(@PathVariable("id") Long id, @Valid @RequestBody {cls}DTO dto) {{
        {cls} entity = {var_name}Assembler.toEntity(dto);
        entity.setId(id);
        boolean success = {var_name}Service.updateById(entity);
        return success ? Result.success(true) : Result.fail(500, "更新失败");
    }}

    /**
     * 删除{t['table_comment']}
     */
    @DeleteMapping("/{{id}}")
    public Result<Boolean> delete(@PathVariable("id") Long id) {{
        boolean success = {var_name}Service.removeById(id);
        return success ? Result.success(true) : Result.fail(500, "删除失败");
    }}
}}"""


def main():
    tables = parse_sql()
    print(f"Found {len(tables)} tables.")
    
    for t in tables:
        module = table_to_module(t['table_name'])
        cls = table_to_class(t['table_name'])
        m_dir = os.path.join(base_dir, module)
        
        # dirs
        for d in ['entity', 'mapper', 'dto', 'vo', 'assembler', 'service/impl', 'controller']:
            os.makedirs(os.path.join(m_dir, d.replace('/', '\\')), exist_ok=True)
        
        # write files
        files = [
            (f"entity/{cls}.java", generate_entity(t)),
            (f"mapper/{cls}Mapper.java", generate_mapper(t)),
            (f"dto/{cls}DTO.java", generate_dto(t)),
            (f"vo/{cls}VO.java", generate_vo(t)),
            (f"assembler/{cls}Assembler.java", generate_assembler(t)),
            (f"service/I{cls}Service.java", generate_iservice(t)),
            (f"service/impl/{cls}ServiceImpl.java", generate_service_impl(t)),
            (f"controller/{cls}Controller.java", generate_controller(t))
        ]
        
        for file_part, content in files:
            path = os.path.join(m_dir, file_part.replace('/', '\\'))
            with open(path, 'w', encoding='utf-8') as f:
                f.write(content)
        
        print(f"Generated module [{module}] for table [{t['table_name']}] mapped to [{cls}].")

if __name__ == '__main__':
    main()
