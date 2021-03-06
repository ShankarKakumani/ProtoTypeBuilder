/**
 * Author : Mani Shankar Kakumani,
 * Created on : 13 May, 2022.
 */

package com.prototypebuilder.app.views.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.prototypebuilder.app.R
import com.prototypebuilder.app.compose.layouts.ShowAlertDialog
import com.prototypebuilder.app.utils.ComposeUtils
import com.prototypebuilder.domain.core.base.ActivityModel
import com.prototypebuilder.domain.core.base.AppModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow

class MainCompose {

    @Composable
    fun SetUpUI() {
        ComposeUtils.SetUpSystemUiColors()
        MainUI()
    }


    @Composable
    fun MainUI() {
        val viewModel: MainViewModel = viewModel()
        LazyColumn {

            viewModel.appsListState.value.forEachIndexed { index, appModel ->
                item {
                    AppItem(appModel, viewModel, index + 1)
                }
            }

            item {
                AddNewAppLayout(viewModel)
            }
        }

    }

    @Composable
    private fun AddNewAppLayout(viewModel: MainViewModel) {
        val isDialogOpen = remember { mutableStateOf(false) }


        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .border(2.dp, color = Color.Black, shape = RoundedCornerShape(15.dp))
                .clickable {
                    isDialogOpen.value = true
                }
        ) {
            val (appName) = createRefs()

            Text(
                modifier = Modifier
                    .padding(15.dp)
                    .constrainAs(appName) {
                        centerHorizontallyTo(parent)
                    },
                text = "Add New App"
            )

            ShowAlertDialog(isDialogOpen, onBtnClick = {
                viewModel.insertApp(it)
            })
        }
    }

    @Composable
    fun AppItem(appModel: AppModel, viewModel: MainViewModel, index: Int) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .clickable {
                    viewModel.navigator?.gotoAppInfo(appModel.appId)
                }
        ) {

            val (number, logo, appName, activitiesCount, delete) = createRefs()
            val activityList: MutableState<List<ActivityModel>> = remember {
                mutableStateOf(emptyList())
            }
            LaunchedEffect(Unit) {
                viewModel.getActivityList(appModel.appId).collect {
                    activityList.value = it
                }
            }

            Text(
                modifier = Modifier
                    .constrainAs(number) {
                        start.linkTo(parent.start, 5.dp)
                        centerVerticallyTo(parent)
                    },
                fontSize = 18.sp,
                text = index.toString()
            )

            Image(
                painter = painterResource(id = R.drawable.ic_app_icon),
                contentDescription = "${appModel.appName} icon",
                modifier = Modifier.constrainAs(logo) {
                    start.linkTo(number.end, 15.dp)
                    centerVerticallyTo(parent)
                }
            )

            Text(
                modifier = Modifier
                    .padding(start = 15.dp)
                    .constrainAs(appName) {
                        top.linkTo(parent.top)
                        bottom.linkTo(activitiesCount.top, 1.dp)
                        start.linkTo(logo.end, 10.dp)
                    },
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                text = appModel.appName ?: ""
            )

            Text(
                modifier = Modifier
                    .padding(start = 15.dp)
                    .constrainAs(activitiesCount) {
                        top.linkTo(appName.bottom, 1.dp)
                        start.linkTo(logo.end, 10.dp)
                        bottom.linkTo(parent.bottom)
                    },
                fontSize = 16.sp,
                text = "Activity Count - " + activityList.value.size.toString()
            )

            Image(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete ${appModel.appName}",
                modifier = Modifier
                    .constrainAs(delete) {
                        end.linkTo(parent.end, 10.dp)
                        centerVerticallyTo(parent)
                    }
                    .clickable {
                        viewModel.deleteApp(appModel.appId)
                    }
            )

        }
    }

}