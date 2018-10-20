package com.retrofit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ljq.mvpframework.presenter.BaseMvpPresenter
import com.ljq.mvpframework.view.AbstractFragment
import com.ljq.mvpframework.view.BaseMvpView

/**
 * Created by zq on 2018/6/11
 */
abstract class BaseFragment<V : BaseMvpView, P : BaseMvpPresenter<V>> : AbstractFragment<V, P>() {
    private val STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN"
    protected lateinit var mView: View
    private var isViewInitiated: Boolean = false
    private var isVisibleToUser: Boolean = false
    private var isDataInitiated: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(getLayoutId(), container, false)
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isViewInitiated = true
        initObject()
        prepareFetchData()
        initListener()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        prepareFetchData()
    }

    private fun prepareFetchData() {
        if (isVisibleToUser && isViewInitiated && !isDataInitiated) {
            initData()
            isDataInitiated = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //防止Fragment重叠
        savedInstanceState?.let {
            val isSupportHidden = it.getBoolean(STATE_SAVE_IS_HIDDEN)
            val ft = fragmentManager.beginTransaction()
            if (isSupportHidden) {
                ft.hide(this)
            } else {
                ft.show(this)
            }
            ft.commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.apply {
            putBoolean(STATE_SAVE_IS_HIDDEN, isHidden)
        }
    }

    override fun onDestroyView() {
        isDataInitiated = false
        super.onDestroyView()
    }

    protected abstract fun getLayoutId(): Int

    protected abstract fun initObject()

    protected abstract fun initData()

    protected abstract fun initListener()

}