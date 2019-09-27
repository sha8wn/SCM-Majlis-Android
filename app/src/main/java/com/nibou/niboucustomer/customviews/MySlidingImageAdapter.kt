//package com.ongraph.muslimhub.ui.activities
//
//import android.content.Context
//import android.graphics.drawable.Drawable
//import android.net.Uri
//import android.os.Parcelable
//import android.support.v4.view.PagerAdapter
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.ProgressBar
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.DataSource
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.load.engine.GlideException
//import com.bumptech.glide.request.RequestListener
//import com.bumptech.glide.request.RequestOptions
//import com.bumptech.glide.request.target.Target
//import com.ongraph.muslimhub.R
//
//class MySlidingImageAdapter(private val context: Context, internal var mImageList: List<String>) : PagerAdapter() {
//    private val inflater: LayoutInflater = LayoutInflater.from(context)
//
//    override fun getCount(): Int {
//        return mImageList.size
//    }
//
//    override fun instantiateItem(view: ViewGroup, position: Int): Any {
//        val imageLayout = inflater.inflate(R.layout.layout_swipe_image, view, false)!!
//        val imageView = imageLayout.findViewById(R.id.zoomableImageView) as ImageView
//
//        val progressBar = imageLayout.findViewById(R.id.progressBar) as ProgressBar
//
//        progressBar.visibility = View.VISIBLE
//        if (mImageList[position] != null) {
//            val imageUri = Uri.parse(mImageList[position])
//
//            val requestOptions = RequestOptions()
//                    .placeholder(R.mipmap.default_header).error(R.mipmap.default_header)
//                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//
//            Glide.with(context)
//                    .load(imageUri)
//                    .listener(object : RequestListener<Drawable> {
//                        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
//                            progressBar.visibility = View.GONE
//                            return false
//                        }
//
//                        override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
//                            progressBar.visibility = View.GONE
//                            return false
//                        }
//                    })
//                    .apply(requestOptions)
//                    .into(imageView)
//                    .clearOnDetach()
//        }
//        view.addView(imageLayout, 0)
//        return imageLayout
//    }
//
//    override fun isViewFromObject(view: View, `object`: Any): Boolean {
//        return view == `object`
//    }
//
//    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}
//
//    override fun saveState(): Parcelable? {
//        return null
//    }
//
//    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        container.removeView(`object` as View)
//    }
//}