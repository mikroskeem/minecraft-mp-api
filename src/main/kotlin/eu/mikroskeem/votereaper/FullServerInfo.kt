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

import com.google.gson.annotations.SerializedName

/**
 * @author Mark Vainomaa
 */
data class FullServerInfo(
        val id: Int,
        val name: String,
        val address: String,
        val port: Int,
        @SerializedName("banner_url") val bannerUrl: String,
        val location: String,
        @SerializedName("is_online") val isOnline: Int,
        val theme: String,
        val players: Int,
        @SerializedName("maxplayers") val maxPlayers: Int,
        val version: String,
        val uptime: Int,
        val score: Int,
        val votes: Int,
        val favorited: Int,
        val comments: Int,
        val url: String,
        @SerializedName("last_check") val lastCheck: String,
        @SerializedName("last_online") val lastOnline: String,
        @SerializedName("registration_date") val registrationDate: String,
        @SerializedName("update_date") val updateDate: String
)