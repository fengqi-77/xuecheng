package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PageService {
    @Autowired
    CmsPageRepository cmsPageRepository;


    /**
     * 页面查询方法
     *
     * @param page             页码，从1开始记数
     * @param size             每页记录数
     * @param queryPageRequest 查询条件
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        //确保条件不为空
        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }
        CmsPage cmsPage = new CmsPage();
        //创建条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());

        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }

        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        if (StringUtils.isNotEmpty(queryPageRequest.getTemplateId())) {
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        //创建条件实例
        Example<CmsPage> of = Example.of(cmsPage, exampleMatcher);

        //分页参数
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(of, pageable);

        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent());//数据列表
        queryResult.setTotal(all.getTotalElements());//数据总记录数
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);

        return queryResponseResult;
    }


    /**
     * 新增cmsPage页面
     *
     * @param cmsPage
     * @return
     */
    public CmsPageResult addCmsPage(CmsPage cmsPage) {
        if (cmsPage == null) {
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
        if (all.size() == 0) {
            cmsPage.setPageId(null);
            cmsPageRepository.save(cmsPage);
            return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
        }
        //存在不添加
        return new CmsPageResult(CommonCode.FAIL, null);


    }

    /**
     * 通过id查询Cms页面
     *
     * @param pageId
     * @return
     */
    public CmsPage findById(String pageId) {
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (optional.isPresent()) {
            return optional.get();
        }

        return null;
    }

    /**
     * 通过id修改CmsPage页面
     * @param pageId
     * @param cmsPage
     * @return
     */
    public CmsPageResult editCmsPage(String pageId, CmsPage cmsPage) {

        //先查询page页面是否存在
        CmsPage one = this.findById(pageId);
        if (one != null) {
            //存在修改
            //更新模板id
            one.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            one.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            one.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            one.setPageName(cmsPage.getPageName());
            //更新访问路径
            one.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());

            CmsPage save = cmsPageRepository.save(one);

            if (save != null) {

                CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS, save);
                return cmsPageResult;
            }
        }

        //不存在返回null
        return new CmsPageResult(CommonCode.FAIL, null);
    }


    /**
     * 根据id删除cms 页面
     * @param id
     * @return
     */
    public ResponseResult delCms(String id){

        //先判读id能否查询到页面
        //先查询page页面是否存在
        CmsPage one = this.findById(id);
        if (one != null) {
            //能删除
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        //不存在返回null
        return new ResponseResult(CommonCode.FAIL);

    }

}
