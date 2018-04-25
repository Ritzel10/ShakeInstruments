package pl.edu.pwr.nr238367.shakeinstruments

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.instrument_entry.view.*

class ImagePagerAdapter(private val context:Context, private val instruments:List<Instrument>) : PagerAdapter(){

    //inflate the view and change its values
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.instrument_entry, container, false)
        val instrument = instruments[position]
        val imageId = getResourceId(instrument.imageName, "drawable")
        //we give view a tag to distinguish it from the others
        view.tag = position
        //initialize the view
        view.instrumentImage.setImageResource(imageId)
        view.instrumentName.text = instrument.name
        container.addView(view)
        return view
    }

    //destory view
    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
    /**
     * Determines whether a page View is associated with a specific key object
     * as returned by [.instantiateItem]. This method is
     * required for a PagerAdapter to function properly.
     *
     * @param view Page View to check for association with `object`
     * @param obj Object to check for association with `view`
     * @return true if `view` is associated with the key object `object`
     */
    override fun isViewFromObject(view: View?, obj: Any?): Boolean {
        return view == obj
    }

    /**
     * Return the number of views available.
     */
    override fun getCount(): Int {
        return instruments.size
    }


    private fun getResourceId(resourceName:String, folder:String):Int{
        return context.resources.getIdentifier(resourceName, folder, context.packageName)
    }
}