package cr.ac.utn.examenI

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.examenI.domain.model.Identifier

class ListAdapter (private var items: List<Identifier>,
                   val itemClickListener: OnItemClickListener):
    RecyclerView.Adapter<CustomViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            CustomViewHolder {
        val itemView = LayoutInflater.from(parent.context).
        inflate(R.layout.activity_item_list,
            parent, false)
        return CustomViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, itemClickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class CustomViewHolder(view: View): RecyclerView.ViewHolder(view)
{
    val tvText1 = view.findViewById<TextView>(R.id.txtText1_Item)
    val tvText2 = view.findViewById<TextView>(R.id.txtText2_Item)
    val tvText3 = view.findViewById<TextView>(R.id.txtText3_Item)
    val tvText4 = view.findViewById<TextView>(R.id.txtText4_Item)
    val imgPicture = view.findViewById<ImageView>(R.id.imgPicture_Item)

    fun bind(obj: Identifier, itemClickListener: OnItemClickListener){
        tvText1.text = obj.id
        tvText2.text = obj.fullName

        imgPicture.setImageResource(R.drawable.ic_launcher_foreground)
        itemView.setOnClickListener{
            itemClickListener.onItemClick(obj)
        }
    }
}