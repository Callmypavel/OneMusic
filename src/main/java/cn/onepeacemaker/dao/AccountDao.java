package cn.onepeacemaker.dao;

import cn.onepeacemaker.entity.Account;
import org.apache.ibatis.annotations.Param;

public interface AccountDao {
    Account login(@Param("user_name") String accountName, @Param("password")String password);
}
