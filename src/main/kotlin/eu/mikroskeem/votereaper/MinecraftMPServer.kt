/*
 * This file is part of project Votereaper, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 Mark Vainomaa <mikroskeem@mikroskeem.eu>
 * Copyright (c) Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package eu.mikroskeem.votereaper

import com.google.gson.Gson
import okhttp3.*

/**
 * Wraps minecraft-mp.com API
 *
 * @author Mark Vainomaa
 */
class MinecraftMPServer(private val serverKey: String,
                        private val httpClient: OkHttpClient = OkHttpClient(),
                        private val gson: Gson = Gson()) {
    private fun getBaseUrl(): HttpUrl.Builder = HttpUrl.Builder()
            .scheme("http")
            .host("minecraft-mp.com")
            .addPathSegments("api")
            .addPathSegment("")
            .addQueryParameter("key", serverKey)

    /**
     * Gets full server information
     */
    fun getFullServerInfo(): FullServerInfo? {
        val url = getBaseUrl()
                .addQueryParameter("object", "servers")
                .addQueryParameter("element", "detail")
                .build()

        return doGet(url) { gson.fromJson(it.body()!!.charStream(), FullServerInfo::class.java) }
    }

    /** Gets vote status for given username */
    fun getVoteStatus(username: String): VoteStatus {
        val url = getBaseUrl()
                .addQueryParameter("object", "votes")
                .addQueryParameter("element", "claim")
                .addQueryParameter("username", username)
                .build()

        return doGet(url) {
            val status = it.body()!!.string()
            when(status) {
                "0" -> VoteStatus.NOT_VOTED
                "1" -> VoteStatus.VOTED
                "2" -> VoteStatus.VOTED_AND_CLAIMED
                else -> throw RuntimeException("Unknown vote status: $status")
            }
        }
    }

    /** Sets vote claimed for given username. Returns whether vote got claimed or was alerady claimed */
    fun setVoteClaimed(username: String): Boolean {
        val url = getBaseUrl()
                .addQueryParameter("action", "post")
                .addQueryParameter("object", "votes")
                .addQueryParameter("element", "claim")
                .addQueryParameter("username", username)
                .build()

        return doPost(url) {
            val status = it.body()!!.string()
            when(status) {
                "0" -> false
                "1" -> true
                else -> throw RuntimeException("Unknown vote claim status: $status")
            }
        }
    }

    /** Gets list of voters and their vote count */
    fun getListOfVoters(period: VotePeriods, limit: Int = 100): Map<String, Int> {
        val url = getBaseUrl()
                .addQueryParameter("object", "servers")
                .addQueryParameter("element", "voters")
                .addQueryParameter("format", "json")
                .addQueryParameter("month", period.value)
                .addQueryParameter("limit", "$limit")
                .build()

        return doGet(url) {
            val votersList = gson.fromJson(it.body()!!.charStream(), VotersList::class.java)
            mapOf(*votersList.voters.map { Pair(it.nickname, it.votes) }.toTypedArray())
        }
    }

    /** Gets list of votes */
    fun getListOfVotes(limit: Int = 100): List<Vote> {
        val url = getBaseUrl()
                .addQueryParameter("object", "servers")
                .addQueryParameter("element", "votes")
                .addQueryParameter("format", "json")
                .addQueryParameter("limit", "$limit")
                .build()

        return doGet(url) { gson.fromJson(it.body()!!.charStream(), VoteList::class.java).votes }
    }


    private inline fun <T> doGet(url: HttpUrl, body: (Response) -> T): T =
        body.invoke(httpClient.newCall(Request.Builder().url(url).get().build()).execute())

    private inline fun <T> doPost(url: HttpUrl, body: (Response) -> T): T =
            body.invoke(httpClient.newCall(Request.Builder().url(url).post(RequestBody.create(MediaType
                    .parse("text/plain"), "")).build()).execute())

    /** Vote statuses */
    enum class VoteStatus {
        NOT_VOTED,
        VOTED,
        VOTED_AND_CLAIMED
    }

    /** Vote periods */
    enum class VotePeriods(val value: String) {
        CURRENT_MONTH("current"),
        PREVIOUS_MONTH("previous")
    }
}