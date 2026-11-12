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
        val item = if (position > 0) items[position - 1] else items[position]
        holder.bind(item, itemClickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class CustomViewHolder(view: View): RecyclerView.ViewHolder(view)
{
    val tvTitle = view.findViewById<TextView>(R.id.txtTitle_Item)
    val tvStatus = view.findViewById<TextView>(R.id.txtStatus_Item)
    val tvPriority = view.findViewById<TextView>(R.id.txtPriority_Item)
    val tvAssingTo = view.findViewById<TextView>(R.id.txtAssignTo_Item)
    val imgPicture = view.findViewById<ImageView>(R.id.imgPicture_Item)

    fun bind(obj: Identifier, itemClickListener: OnItemClickListener){
        tvTitle.text = obj.id
        tvStatus.text = obj.fullName

        imgPicture.setImageResource(R.drawable.ic_launcher_foreground)
        itemView.setOnClickListener{
            itemClickListener.onItemClick(obj)
        }
    }
}