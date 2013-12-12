package com.meizhuo.etips.appwidget;

import java.util.ArrayList;
import java.util.List;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.meizhuo.etips.activities.Notes;
import com.meizhuo.etips.activities.NotesEdit;
import com.meizhuo.etips.activities.R;
 
import com.meizhuo.etips.common.utils.ETipsContants;
import com.meizhuo.etips.common.utils.SP;
import com.meizhuo.etips.common.utils.StringUtils;
import com.meizhuo.etips.model.MNotes;

public class NotesAppWidget extends AppWidgetProvider {
	private List<MNotes> list = null;

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (intent.getAction() == ETipsContants.Action_Notes) {
			reflush(context);
		}else{
			super.onReceive(context, intent);
		}
	}
    
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		PendingIntent startNotes = PendingIntent.getActivity(context, 0,
				new Intent(context, Notes.class), 0);
		PendingIntent editNotes = PendingIntent.getActivity(context, 1,
				new Intent(context, NotesEdit.class), 0);
		reflush(context);
		for (int i = 0; i < appWidgetIds.length; i++) {
			// Log.i("debug", "onUpdata --");

			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_notes);
			remoteViews.setOnClickPendingIntent(R.id.widget_notes_content,
					startNotes);
			remoteViews.setOnClickPendingIntent(R.id.widget_notes_btn_addNote,
					editNotes);
			appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
		}
		context.sendBroadcast(new Intent(ETipsContants.Action_Notes)); //发广播刷新
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

   private String wrapData(List<MNotes> res){
	   StringBuffer sb =new StringBuffer();
	   for( MNotes m : res){
	 
		   sb.append(StringUtils.getDateFormat(m.getTime(), "mm-dd")).append("\n\t").append(m.getContent()).append("\n");
	   }
	   return sb.toString();
   }
   
   private void reflush(Context context){
		SP sp = new SP(ETipsContants.SP_NAME_Notes, context);
		list = null;
		list = new ArrayList<MNotes>();
		if (!sp.isEmpty()) {
			list = (List<MNotes>) sp
					.toEntityAll(ETipsContants.TYPE_SP_Notes);
		 
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_notes);
		
			remoteViews.setTextViewText(R.id.widget_notes_tv_content, wrapData(list));
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			ComponentName componentName = new ComponentName(context, NotesAppWidget.class);
			appWidgetManager.updateAppWidget(componentName, remoteViews);
		}
   }

}
