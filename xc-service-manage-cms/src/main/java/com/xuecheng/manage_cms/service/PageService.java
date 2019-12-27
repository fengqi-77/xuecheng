package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageService {
    @Autowired
    CmsPageRepository cmsPageRepository;


    /**
     * 页面查询方法
     * @param page 页码，从1开始记数
     * @param size 每页记录数
     * @param queryPageRequest 查询条件
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest){
        //确保条件不为空
        if (queryPageRequest == null){
             queryPageRequest = new QueryPageRequest();
        }
        CmsPage cmsPage = new CmsPage();
        //创建条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());

        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }

        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        if (StringUtils.isNotEmpty(queryPageRequest.getTemplateId())){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        //创建条件实例
        Example<CmsPage> of = Example.of(cmsPage, exampleMatcher);

        //分页参数
        if(page <=0){
            page = 1;
        }
        page = page -1;
        if(size<=0){
            size = 10;
        }

        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> all = cmsPageRepository.findAll(of,pageable);

        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent());//数据列表
        queryResult.setTotal(all.getTotalElements());//数据总记录数
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);

        return queryResponseResult;
    }


    public CmsPageResult addCmsPage(CmsPage cmsPage){
        if(cmsPage == null){
           cmsPage = new CmsPage();
        }
        //校验页面是否存在，根据页面名称、站点Id、页面webpath查询

        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        CmsPage queryCmsPage = new CmsPage();
        queryCmsPage.setSiteId(cmsPage.getSiteId());
        queryCmsPage.setPageName(cmsPage.getPageName());
        queryCmsPage.setPageWebPath(cmsPage.getPageWebPath());
        Example<CmsPage> of = Example.of(queryCmsPage, exampleMatcher);
        List<CmsPage> all = cmsPageRepository.findAll(of);

        //不存在添加
        if (all.size()==0){
            cmsPageRepository.save(cmsPage);
            return  new CmsPageResult(CommonCode.SUCCESS,cmsPage);
        }
        //存在不添加
        return new CmsPageResult(CommonCode.FAIL,null);


    }
}
