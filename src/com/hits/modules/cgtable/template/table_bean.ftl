package ${entity.packageName};

import java.io.Serializable;
import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Name;
/**
 * 
 *  #(c) IFlytek cform <br/>
 *
 *  版本说明: $id:$ <br/>
 *
 *  功能说明: ${entity.tableName}表实体类
 * 
 *  <br/>创建说明: ${entity.createDate} (☆笑死宝宝了☆)  创建文件<br/>
 * 
 *  修改历史:<br/>
 *
 */
@Table("${entity.tableName}")
public class ${entity.className} implements Serializable {
	private static final long serialVersionUID = 1L;
	
	<#list entity.fileds as filed>
	@Column
	<#if (filed.isPrimary==1)>
	@Name
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT sys_guid() FROM dual")
	})
	</#if>
	private ${filed.filedType} ${filed.filedName};
	</#list>
	

	<#list entity.fileds as property>
    public ${property.filedType} get${property.filedName?cap_first}() {
        return ${property.filedName};
    }
    public void set${property.filedName?cap_first}(${property.filedType} ${property.filedName}) {
        this.${property.filedName} = ${property.filedName};
    }
	</#list>
	

}