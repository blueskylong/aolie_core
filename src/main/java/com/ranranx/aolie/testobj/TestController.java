package com.ranranx.aolie.testobj;

import com.ranranx.aolie.ds.dataoperator.DataOperatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/11 13:10
 * @Version V0.0.1
 **/
@RestController
public class TestController {
    @Autowired
    private DataOperatorFactory factory;

    @RequestMapping("/findContries")
    public List<Map<String, Object>> findContries() {
        return factory.getDataOperatorByName("mybatisDbOperator", "1").select(null);
    }
}
