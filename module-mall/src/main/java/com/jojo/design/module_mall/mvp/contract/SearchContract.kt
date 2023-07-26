package com.jojo.design.module_mall.mvp.contract

import com.jojo.design.common_base.dagger.mvp.BaseContract
import com.jojo.design.module_mall.bean.CategoryBean
import com.jojo.design.module_mall.bean.FilterBean
import com.jojo.design.module_mall.bean.RecordsEntity
import com.smart.novel.net.BaseHttpResponse
import io.reactivex.rxjava3.core.Observable

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/12/29 10:09 PM
 *    desc   : 搜索商品
 */
interface SearchContract {
    interface View : BaseContract.BaseView {
        fun getHotList(dataList: List<String>)
        fun getSearchGoods(dataBean: RecordsEntity)
        fun getCategoryList(dataList: List<CategoryBean>)
        fun getFilterData(dataBean: FilterBean)
    }

    interface Presenter : BaseContract.BasePresenter {
        fun getHotList()
        fun getSearchGoods(outCategoryId: String, keyword: String, page: Int, sort: Int, queryParams: Map<String, String>)
        fun getCategoryList(outCategoryId: String, keyword: String)
        fun getFilterData(outCategoryId: String)
    }

    interface Model : BaseContract.BaseModel {
        fun getHotList(): Observable<BaseHttpResponse<List<String>>>
        fun getSearchGoods(outCategoryId: String, keyword: String, page: Int, sort: Int, queryParams: Map<String, String>): Observable<BaseHttpResponse<RecordsEntity>>
        fun getCategoryList(outCategoryId: String, keyword: String): Observable<BaseHttpResponse<List<CategoryBean>>>
        fun getFilterData(outCategoryId: String): Observable<BaseHttpResponse<FilterBean>>
    }
}