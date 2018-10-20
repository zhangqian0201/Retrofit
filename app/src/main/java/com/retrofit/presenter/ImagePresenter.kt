package com.retrofit.presenter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import com.hitomi.glideloader.GlideImageLoader
import com.hitomi.tilibrary.style.index.NumberIndexIndicator
import com.hitomi.tilibrary.style.progress.ProgressPieIndicator
import com.hitomi.tilibrary.transfer.TransferConfig
import com.hitomi.tilibrary.transfer.Transferee
import com.retrofit.api.Api
import com.retrofit.model.ImageBean
import com.retrofit.model.ImageResult
import com.retrofit.utils.ApiUtls
import com.retrofit.view.BaseView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by zq on 2018/6/11
 */
class ImagePresenter : BasePresenter<BaseView<ImageBean>>() {
    private val api by lazy { ApiUtls.getApi(Api.welfareUrl) }
    private val imageViews: MutableList<ImageView> = ArrayList()
    lateinit var transferConfig: TransferConfig
    lateinit var context: Context
    private val transferee by lazy {
        Transferee.getDefault(context)
    }

    fun openDetail(view: ImageView, mList: MutableList<ImageResult>, position: Int, context: Context) {
        this.context = context
        if (imageViews.size < mList.size) {
            for (index in 0..(mList.size - imageViews.size)) {
                imageViews.add(view)
            }
        }
        transferConfig = TransferConfig.build()
                .setSourceImageList(mList.map { it.url })//SourceImageList\OriginImageList size一致
                .setOriginImageList(imageViews)
                .setMissDrawable(ColorDrawable(Color.parseColor("#DCDDE1")))
                .setErrorDrawable(ColorDrawable(Color.parseColor("#DCDDE1")))
                .setProgressIndicator(ProgressPieIndicator())
                .setNowThumbnailIndex(position)//仅一张
                .setIndexIndicator(NumberIndexIndicator())
                .setImageLoader(GlideImageLoader.with(context.applicationContext))
                .create()
        transferee.apply(transferConfig).show()
    }

    override fun loadMore() {
        getData(false)
    }

    override fun refresh() {
        from = 1
        getData(true)
    }

    private fun getData(isFirstLoad: Boolean) {
        val data = api.getImageList(from)
        data.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    from++
                    mvpView?.loadSuccess(it, isFirstLoad)
                }, {
                    mvpView?.loadFail(it.message)
                })
    }
}