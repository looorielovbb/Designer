package com.jojo.design.module_mall.mvp.model

import com.jojo.design.module_mall.bean.CategoryBean
import com.jojo.design.module_mall.bean.FilterBean
import com.jojo.design.module_mall.bean.RecordsEntity
import com.jojo.design.module_mall.mvp.contract.SearchContract
import com.jojo.design.module_mall.net.NetMallProvider
import com.smart.novel.net.BaseHttpResponse
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/12/29 5:09 PM
 *    desc   : 搜索商品
 */
class SearchModel @Inject constructor() : SearchContract.Model {
    override fun getHotList(): Observable<BaseHttpResponse<List<String>>> = NetMallProvider.requestService.getHotList()

    override fun getSearchGoods(outCategoryId: String, keyword: String, page: Int, sort: Int, queryParams: Map<String, String>): Observable<BaseHttpResponse<RecordsEntity>> = NetMallProvider.requestService.getSearchGoods(outCategoryId, keyword, page, sort, queryParams)

    override fun getCategoryList(outCategoryId: String, keyword: String): Observable<BaseHttpResponse<List<CategoryBean>>> = NetMallProvider.requestService.getCategoryList(outCategoryId, keyword)

    override fun getFilterData(outCategoryId: String): Observable<BaseHttpResponse<FilterBean>> = NetMallProvider.requestService.getFilterData(outCategoryId)

}