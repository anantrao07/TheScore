package com.anant.myapplication.ui.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anant.myapplication.R
import com.anant.myapplication.data.TeamItems
import com.anant.myapplication.ui.TeamDetailsViewActivity
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.team_list_child_layout.view.*

class TeamListAdapter(
    private val context: Context,
    teamListData: OrderedRealmCollection<TeamItems>
) : RealmRecyclerViewAdapter<TeamItems,
        TeamListAdapter.TeamListViewHolder>(teamListData, true) {

    inner class TeamListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parentViewGroup: ViewGroup, viewType: Int): TeamListViewHolder {
        val inflatedView = LayoutInflater.from(parentViewGroup.context).inflate(
            R.layout.team_list_child_layout,
            parentViewGroup, false
        )
        return TeamListViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: TeamListViewHolder, position: Int) {
        getItem(position)?.let { teamListItemData ->
            holder.itemView.teamNameTextView.text = teamListItemData.fullName
            holder.itemView.teamWinsTextView.text = context.resources.getString(R.string.wins, teamListItemData.wins)
            holder.itemView.teamLoseTextView.text = context.resources.getString(R.string.loss, teamListItemData.losses)

            holder.itemView.setOnClickListener {
                val itemDetailIntent = Intent(context, TeamDetailsViewActivity::class.java)
                itemDetailIntent.putExtra("itemID", teamListItemData.TeamId)
                Log.d("itemID", teamListItemData.TeamId.toString())
                context.startActivity(itemDetailIntent)
            }
        }
    }
}