package com.yicj.distributed.uua.dao;

import com.yicj.distributed.uua.model.PermissionDTO;
import com.yicj.distributed.uua.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: UserDao
 * Description: TODO(描述)
 * Date: 2020/7/14 14:25
 *
 * @author yicj(626659321 @ qq.com)
 * 修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息
 */
@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate ;


    public UserDTO getUserByUsername(String username){
        String sql ="select id,username,password,fullname from t_user where username = ?";
        List<UserDTO> list = jdbcTemplate.query(sql, new Object[]{username}, new BeanPropertyRowMapper<>(UserDTO.class));
        if(list == null && list.size() <= 0){
            return null;
        }
        return list.get(0);
    }


    public List<String> findPermissionsByUserId(String userId){
        String sql="SELECT * FROM t_permission WHERE id IN(\n" +
                    "SELECT permission_id FROM t_role_permission WHERE role_id IN(\n" +
                        "\tSELECT role_id FROM t_user_role WHERE user_id = ? \n" +
                    ")\n" +
                ")";
        List<PermissionDTO> list = jdbcTemplate.query(sql, new Object[]{userId}, new
                BeanPropertyRowMapper<>(PermissionDTO.class));
        List<String> permissions = new ArrayList<>();
        list.iterator().forEachRemaining(c -> permissions.add(c.getCode()));
        return permissions;
    }

}
