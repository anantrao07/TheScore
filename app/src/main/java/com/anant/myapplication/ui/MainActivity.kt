package com.anant.myapplication.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.anant.myapplication.R
import com.anant.myapplication.data.TeamItems
import com.anant.myapplication.networking.NetworkManager
import com.anant.myapplication.persistance.fetchTeamItems
import com.anant.myapplication.persistance.fetchSortedTeamItems
import com.anant.myapplication.ui.adapter.TeamListAdapter
import com.anant.myapplication.utils.SortOptions
import com.anant.myapplication.utils.isNetworkAvailable
import io.realm.Realm
import io.realm.RealmResults

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class MainActivity : AppCompatActivity() {

    private val realmInstance = Realm.getDefaultInstance()
    private var teamListItemsData: RealmResults<TeamItems>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        teamListItemsData = fetchTeamItems(realmInstance)
        getTeamData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.Sort_by_name -> {
                getSortedTeamData(SortOptions.Alphabetical)
                true
            }

            R.id.sort_by_wins -> {
                getSortedTeamData(SortOptions.Wins)
                true
            }
            R.id.sort_by_loss -> {
                getSortedTeamData(SortOptions.Loss)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realmInstance.close()
    }

    private fun setUpRecyclerView(teamItems: RealmResults<TeamItems>) {
        teamListRecyclerView.layoutManager = LinearLayoutManager(this)
        teamListRecyclerView.adapter = TeamListAdapter(this, teamItems)
    }

    private fun getTeamData() {
        if (teamListItemsData?.isNotEmpty() == true) {
            setUpRecyclerView(teamListItemsData!!)
        } else {
            if (isNetworkAvailable(this)) {
                try {
                    launch(UI) {

                        async {
                            NetworkManager.suspendRequestTeamItems()
                            setUpRecyclerView(teamListItemsData!!)
                        }
                    }
                } catch (e: Exception) {
                    Log.d("item detail", e.localizedMessage)
                }
            } else {
                Toast.makeText(this, getString(R.string.networkError), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getSortedTeamData(sortType: SortOptions) {
        teamListItemsData = fetchSortedTeamItems(realmInstance, sortType)
        if (teamListItemsData?.isNotEmpty() == true) {
            setUpRecyclerView(this.teamListItemsData!!)
        } else {
            getTeamData()
        }
    }
}
