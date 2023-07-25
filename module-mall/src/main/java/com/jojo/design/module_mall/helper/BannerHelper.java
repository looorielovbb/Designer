package com.jojo.design.module_mall.helper;

import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.jojo.design.common_base.utils.glide.GlideUtils;
import com.jojo.design.module_mall.R;

import java.util.List;

/**
 * author : JOJO
 * e-mail : 18510829974@163.com
 * date   : 2019/1/16 5:40 PM
 * desc   :
 */
public class BannerHelper {
    public static void setBanner(ConvenientBanner<String> banner, List<String> imgUrls) {
        banner.setPages(new CBViewHolderCreator() {
            @Override
            public Holder<String> createHolder(View itemView) {
                return new Holder<String>(itemView) {
                    ImageView iv_banner;

                    @Override
                    protected void initView(View itemView) {
                        iv_banner = itemView.findViewById(R.id.iv_banner);
                    }

                    @Override
                    public void updateUI(String data) {
                        GlideUtils.INSTANCE.loadImage(data, iv_banner, 0);
                    }
                };
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_banner;
            }
        }, imgUrls);
    }
}
