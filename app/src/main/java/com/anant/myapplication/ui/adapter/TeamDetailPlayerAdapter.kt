package com.anant.myapplication.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anant.myapplication.R
import com.anant.myapplication.data.Players
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.players_list_item.view.*

class TeamDetailPlayerAdpater(private val context: Context,
    playersListData: OrderedRealmCollection<Players>
) : RealmRecyclerViewAdapter<Players,
        TeamDetailPlayerAdpater.TeamDetailViewHolder>(playersListData, true) {

    inner class TeamDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parentViewGroup: ViewGroup, viewType: Int): TeamDetailViewHolder {
        val inflatedView = LayoutInflater.from(parentViewGroup.context).inflate(
            R.layout.players_list_item,
            parentViewGroup, false)
        return TeamDetailViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: TeamDetailViewHolder, position: Int) {
        getItem(position)?.let { teamPlayerItemData ->
            holder.itemView.playersFirstName.text = context.getString(R.string.playerFirstName, teamPlayerItemData.fullName)
            holder.itemView.playersLastName.text = context.getString(R.string.playerLastName,teamPlayerItemData.lastName)
            holder.itemView.playersNumber.text = context.getString(R.string.playerNumber, teamPlayerItemData.number)
            holder.itemView.playersPosition.text = context.getString(R.string.playerPosition, teamPlayerItemData.playerPosition)
        }
    }
}