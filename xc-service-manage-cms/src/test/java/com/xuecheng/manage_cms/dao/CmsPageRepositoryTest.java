package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsPageParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    @Autowired
    CmsPageRepository cmsPageRepository;

    @Test
    public void findCmsPage(){
        List<CmsPage> list = cmsPageRepository.findAll();
        System.out.println(list);
    }

    @Test
    public void findPageCmsPage(){

        int page = 1;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageRequest);
        System.out.println(all);
    }
    
    @Test
    public void addPageCms(){
        //定义实体类
         CmsPage cmsPage = new CmsPage();
         cmsPage.setSiteId("s01");
         cmsPage.setTemplateId("t01");
         cmsPage.setPageName("测试页面");
         cmsPage.setPageCreateTime(new Date());
         List<CmsPageParam>   cmsPageParams   =   new ArrayList<>();
         CmsPageParam   cmsPageParam   =   new   CmsPageParam();
         cmsPageParam.setPageParamName("param1");
         cmsPageParam.setPageParamValue("value1");
         cmsPageParams.add(cmsPageParam);
         cmsPage.setPageParams(cmsPageParams);
         cmsPageRepository.save(cmsPage);
         System.out.println(cmsPage);
    }

    //删除
    @Test
    public void testDelete() {
        cmsPageRepository.deleteById("5dff6775f949960b880e105b");
    }

    //修改
    @Test
    public void testUpdate() {

        Optional<CmsPage> optional = cmsPageRepository.findById("5dff6b42f9499644e4416c2f");

        if (optional.isPresent()){
            CmsPage cmsPage = optional.get();
            cmsPage.setPageName("测试修改页面");
            CmsPage save = cmsPageRepository.save(cmsPage);
            System.out.println(save);
        }



    }

    //多条件查询
    @Test
    public void manyFind() {

        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);

        CmsPage cmsPage = new CmsPage();

        cmsPage.setPageAliase("课程");
        //cmsPage.setPageWebPath("/coursepre/");
        //条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());

        //ExampleMatcher.GenericPropertyMatchers.contains() 包含关键字
//        ExampleMatcher.GenericPropertyMatchers.startsWith()//前缀匹配
        //不加选择器默认全字符匹配

        Example<CmsPage> of = Example.of(cmsPage, exampleMatcher);
        Page<CmsPage> all = cmsPageRepository.findAll(of, pageRequest);
        List<CmsPage> content = all.getContent();
        int number = all.getNumber();
        System.out.println(number);



    }
    //多条件不分页查询
    @Test
    public void manyNoPageFind() {

        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageName("index.html");
        cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
        cmsPage.setPageWebPath("/index.html");

        //条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        //不加选择器默认全字符匹配

        Example<CmsPage> of = Example.of(cmsPage, exampleMatcher);
        List<CmsPage> all = cmsPageRepository.findAll(of);

        System.out.println(all);



    }


}
