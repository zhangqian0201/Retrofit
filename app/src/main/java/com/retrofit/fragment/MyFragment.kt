package com.retrofit.fragment

import com.ljq.mvpframework.factory.CreatePresenter
import com.retrofit.R
import com.retrofit.model.GirlsBean
import com.retrofit.presenter.GirlsPresenter
import com.retrofit.view.BaseView

/**
 * Created by zq on 2018/8/4
 */
@CreatePresenter(GirlsPresenter::class)
class MyFragment : BaseFragment<BaseView<GirlsBean>, GirlsPresenter>(), BaseView<GirlsBean> {

    override fun getLayoutId(): Int = R.layout.fragment_my

    override fun initObject() {

    }

    override fun initData() {

    }

    override fun initListener() {

    }

    override fun loadSuccess(model: GirlsBean, isFirstLoad: Boolean) {

    }

    override fun loadFail(msg: String?) {

    }

    companion object {
        val instance: MyFragment by lazy {
            MyFragment()
        }
    }
}