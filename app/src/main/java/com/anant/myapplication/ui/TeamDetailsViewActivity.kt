package com.anant.myapplication.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.anant.myapplication.R
import com.anant.myapplication.data.Players
import com.anant.myapplication.data.TeamItems
import com.anant.myapplication.persistance.fetchTeam
import com.anant.myapplication.ui.adapter.TeamDetailPlayerAdpater
import com.anant.myapplication.ui.adapter.TeamListAdapter
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmResults
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_team_details.*
import kotlinx.android.synthetic.main.team_list_child_layout.*

class TeamDetailsViewActivity : AppCompatActivity() {

    private val realmInstance = Realm.getDefaultInstance()
    private var teamId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_details_view)
        teamId = intent.extras.getInt("itemID")

    }

    override fun onResume() {
        super.onResume()
        val realmObject = fetchTeam(realmInstance, teamId)
        if (realmObject != null) {
            teamDetailNameTextView.text = realmObject.fullName
            teamDetailWinsTextView.text = getString(R.string.wins, realmObject.wins)
            teamDetailLossTextView.text = getString(R.string.loss, realmObject.losses)
            setUpRecyclerView(realmObject.players)
        }
    }

    private fun setUpRecyclerView(teamItems: RealmList<Players>) {
        teamDetailRecyclerView.layoutManager = LinearLayoutManager(this)
        teamDetailRecyclerView.adapter = TeamDetailPlayerAdpater( this, teamItems)
    }

    override fun onDestroy() {
        super.onDestroy()
        realmInstance.close()
    }
}
