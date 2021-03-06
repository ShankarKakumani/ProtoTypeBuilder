/**
 * Author : Mani Shankar Kakumani,
 * Created on : 13 May, 2022.
 */

package com.prototypebuilder.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tableAppsList")
data class RMAppEntity(
    @PrimaryKey(autoGenerate = true)
    val appId: Long = 0,
    val appName: String?,
    val background: Int?,
    val statusBarColor: Int?,
    val statusBarDarkIcons: Boolean?,
    val navigationBarColor: Int?,
    val navigationBarDarkIcons: Boolean?,
    val themeType: String?,
    val initialScreenId: Int?,
)