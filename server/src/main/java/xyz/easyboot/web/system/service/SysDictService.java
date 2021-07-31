package xyz.easyboot.web.system.service;

import xyz.easyboot.web.system.entity.SysDict;
import xyz.easyboot.web.system.entity.SysDictItem;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
* Created by Quarkus-Vue-Admin on 2021-07-13.
*/
@ApplicationScoped
@Transactional
public class SysDictService {
    
    @Transactional
    public boolean delete(String...ids) {
        List<Long> idList = Arrays.stream(ids).map(Long::parseLong).collect(Collectors.toList());
        
        // dict
        List<SysDict> dictList = SysDict.find("id in (?1)", idList).list();
        List<String> dictCodeList = dictList.stream().map(x -> x.dictCode).collect(Collectors.toList());
        long result = SysDict.delete("id in (?1)", idList);
        
        // dict_item
        SysDictItem.delete("dictCode in (?1)", dictCodeList);
        return result > 0;
    }

}
