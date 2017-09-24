package com.hits.modules.gtxt.bean;

import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.*;

/**
 * Created by Numbgui on 2016/5/3 0003.
 */
@Table("user_breach")
public class User_breach {
    @Column
    @Id
    @Prev({
            @SQL(db = DB.ORACLE, value="SELECT user_breach_S.nextval FROM dual")
    })
    private int id;

    @Column
    private String userid;
    @Column
    private String breachid;
    @Column
    private String loginname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getBreachid() {
        return breachid;
    }

    public void setBreachid(String breachid) {
        this.breachid = breachid;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }
}
