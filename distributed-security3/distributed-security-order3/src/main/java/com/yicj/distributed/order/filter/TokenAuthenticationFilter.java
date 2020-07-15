package com.yicj.distributed.order.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yicj.distributed.order.common.EncryptUtil;
import com.yicj.distributed.order.model.UserDTO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ClassName: TokenAuthenticationFilter
 * Description: TODO(描述)
 * Date: 2020/7/15 12:34
 *
 * @author yicj(626659321 @ qq.com)
 * 修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息
 */
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("json-token");
        if (token !=null){
            //1. 解析token
            String json = EncryptUtil.decodeUTF8StringBase64(token) ;
            JSONObject userJson = JSON.parseObject(json) ;
            UserDTO user = new UserDTO();
            user.setUsername(userJson.getString("principal"));
            JSONArray authoritiesArray = userJson.getJSONArray("authorities");
            String[] authorities = authoritiesArray.toArray(new String[authoritiesArray.size()]);
            //2. 新建并填充 authorities
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    user, null, AuthorityUtils.createAuthorityList(authorities)
            ) ;
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            //3. 将authenticationToken保存进安全上下文
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);

    }
}
