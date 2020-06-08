package task.who.coviddashboard.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.islamkhsh.CardSliderAdapter
import kotlinx.android.synthetic.main.list_item_card_2.view.*
import task.who.coviddashboard.R
import task.who.coviddashboard.data.model.Attribute

class CardsAdapter(private val cardData: ArrayList<Attribute>)  : CardSliderAdapter<RecyclerView.ViewHolder>() {
    
    override fun getItemCount() = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == 2) {
            Card2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_card_2, parent, false))
        }else {
            Card1ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_card_1, parent, false))
        }

    override fun bindVH(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 2) {
            (holder as Card2ViewHolder).bind(cardData[position-1])
        }
    }

    override fun getItemViewType(position: Int): Int = if (position == 0) 1 else 2

    inner class Card1ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class Card2ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        @SuppressLint("SetTextI18n")
        fun bind(attribute: Attribute) {
            itemView.header_text.text = attribute.getHeaderText()
            itemView.total_death.text = attribute.getFormattedTotalDeaths()
            itemView.cases_num.text = attribute.getFormattedTotalCases()
        }
    }
}