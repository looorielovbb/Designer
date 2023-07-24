package com.jojo.design.common_base.config.arouter

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/12/24 4:27 PM
 *    desc   : base-路由页面常量配置 注意：路径至少需要两级 {/xx/xx}
 */
class ARouterConfig {
    companion object {
        //const声明编译时常量
        //想去app
        const val ACT_WEB_VIEW = "/base/act_common_web"
        const val ACT_DESIGNER_LIST = "/designer/act_designer_list"
        const val ACT_SEARCH = "/mall/act_search"
        const val ACT_GOODS_FILTER= "/mall/act_goods_filter"
        const val ACT_GOODS_DETAIL= "/mall/act_goods_detail"
        //开眼视频app
        const val ACT_CATEGORY= "/found/act_category"
        const val ACT_CATEGORY_DETAIL= "/found/act_category_detail"
        const val ACT_PLAY_VIDEO= "/found/act_play_video"
    }

}