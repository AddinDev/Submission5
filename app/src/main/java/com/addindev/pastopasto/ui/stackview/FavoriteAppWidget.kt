package com.addindev.pastopasto.ui.stackview

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.addindev.pastopasto.MainBotNavActivity
import com.addindev.pastopasto.R
import com.addindev.pastopasto.ui.stackview.adapter.StackWidgetService

class FavoriteAppWidget : AppWidgetProvider(){
    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        Log.d("IDN","${FavoriteAppWidget::class.java.simpleName} : onUpdate")
        for (appWidgetId in appWidgetIds!!) {
            updateAppWidget(context!!, appWidgetManager!!, appWidgetId)
        }
    }

    override fun onEnabled(context: Context?) {

    }

    override fun onDisabled(context: Context?) {

    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.d("IDN","${FavoriteAppWidget::class.java.simpleName} : onReceive")
        if (intent!!.action != null) {
            if (intent.action!!.equals(TOAST_ACTION)) {
                val viewIndex = intent.getIntExtra(EXTRA_ITEM, 0)
                Toast.makeText(context, "Touched view $viewIndex", Toast.LENGTH_SHORT).show()
//                val intentApp = Intent(context, MainBotNavActivity::class.java)
//                context!!.startActivity(intentApp)
            }
        }

//        val appWidgetManager = AppWidgetManager.getInstance(context)
//        val thisWidget = ComponentName(context!!, FavoriteAppWidget::class.java)
//        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
//        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view)
//        super.onReceive(context, intent)
    }

    companion object{
        const val TOAST_ACTION = "com.addindev.pastopasto.TOAST_ACTION"
        const val EXTRA_ITEM = "com.addindev.pastopasto.EXTRA_ITEM"

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            Log.d("IDN","${FavoriteAppWidget::class.java.simpleName} : updateAppWidget")

            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))

            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.favorite_app_widget)
            views.setRemoteAdapter(R.id.stack_view, intent)
            views.setEmptyView(R.id.stack_view, R.id.empty_view)

            val toastIntent = Intent(context, FavoriteAppWidget::class.java)
            toastIntent.action = TOAST_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))

            val toastPendingIntent =
                    PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}