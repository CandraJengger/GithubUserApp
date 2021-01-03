package com.jengger.githubuserapp.ViewModel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.jengger.githubuserapp.Model.UserItems
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class FollowersViewModel : ViewModel() {
    private val listFollowers = MutableLiveData<ArrayList<UserItems>>()

    fun setUsername(view: View, username: String) {
        val listItems = ArrayList<UserItems>()

        val url = "https://api.github.com/users/$username/followers"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 84ea40cfa9a2af90882e2271e7f7d7c3606e37cd")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val list = JSONArray(result)

                    Log.d("Length", list.length().toString())
                    if (list.length() == 0) {
                        listFollowers.postValue(null)
                        return
                    }

                    for (i in 0 until list.length()) {
                        val user = list.getJSONObject(i)
                        val userItems = UserItems(
                                photo = user.getString("avatar_url"),
                                username = user.getString("login"),
                        )

                        listItems.add(userItems)
                    }

                    setDetailUser(listItems)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Snackbar.make(view, error?.message.toString(), Snackbar.LENGTH_LONG).show()
                Log.d("onFailure", error?.message.toString())
            }

        })

    }

    private fun setDetailUser(list: ArrayList<UserItems>) {

        for (i in 0 until list.size) {
            val username = list[i].username

            val url = "https://api.github.com/users/$username"

            val client = AsyncHttpClient()
            client.addHeader("Authorization", "token 84ea40cfa9a2af90882e2271e7f7d7c3606e37cd")
            client.addHeader("User-Agent", "request")
            client.get(url, object: AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)

                    list[i].photo = responseObject.getString("avatar_url")
                    list[i].username = responseObject.getString("login")
                    list[i].name = responseObject.getString("name")
                    list[i].location = responseObject.getString("location")
                    list[i].company = responseObject.getString("company")
                    list[i].followers = responseObject.getString("followers")
                    list[i].following = responseObject.getString("following")
                    list[i].repository = responseObject.getString("public_repos")
                    list[i].urlFollowers = responseObject.getString("followers_url")
                    list[i].urlFollowing = responseObject.getString("following_url")

                    listFollowers.postValue(list)
                }

                override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                    Log.d("onFailure", error?.message.toString())
                }

            })

        }
    }

    fun getFollowers() : LiveData<ArrayList<UserItems>> {
        return listFollowers
    }
}