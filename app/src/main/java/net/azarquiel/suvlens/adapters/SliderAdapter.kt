package net.azarquiel.suvlens.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import net.azarquiel.suvlens.R
import net.azarquiel.suvlens.model.Slide

class SliderAdapter : PagerAdapter {


    var mContext: Context? = null
    var mList: List<Slide>? = null

    constructor(context: Context, list: List<Slide>) {
        mContext = context
        mList = list
    }


    override fun instantiateItem(container: ViewGroup, position: Int): View {

        var inflater: LayoutInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        var slideLayout = inflater.inflate(R.layout.slide_item, null)

        var slideImage: ImageView = slideLayout.findViewById(R.id.slideImage)
        slideImage.setImageResource(mList!!.get(position).image)
        container.addView(slideLayout)
        return slideLayout

    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    /**
     * Return the number of views available.
     */
    override fun getCount(): Int {
        return mList!!.count()
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}


