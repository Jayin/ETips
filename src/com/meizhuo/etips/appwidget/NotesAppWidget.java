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

import com.meizhuo.etips.common.CalendarUtils;
import com.meizhuo.etips.common.ETipsContants;
import com.meizhuo.etips.common.L;
import com.meizhuo.etips.common.SP;
import com.meizhuo.etips.common.StringUtils;
import com.meizhuo.etips.model.MNotes;

/**
 * 便签桌面小插件
 * 
 * @author Jayin Ton
 * 
 */
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

		if (intent.getAction().equals(ETipsContants.Action_Notes)
				|| intent.getAction().equals(
						"android.appwidget.action.APPWIDGET_UPDATE")) {
			// 考虑起始位为0
			int item_no = intent.getIntExtra("item_no", -1);
			reflush(context, item_no);
		} else {
			super.onReceive(context, intent);
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		for (int i = 0; i < appWidgetIds.length; i++) {
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_notes);

			appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
		}
		context.sendBroadcast(new Intent(ETipsContants.Action_Notes)); // 发广播刷新
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	private String wrapData(MNotes res) {
		StringBuffer sb = new StringBuffer();
		sb.append(
				CalendarUtils.getTimeFromat(res.getTime(),
						CalendarUtils.TYPE_ONE)).append("\n\t")
				.append(res.getContent());
		return sb.toString();
	}

	// 刷新
	private void reflush(Context context, int item_no) {
		PendingIntent startNotes = PendingIntent.getActivity(context, 0,
				new Intent(context, Notes.class), 0);
		PendingIntent editNotes = PendingIntent.getActivity(context, 1,
				new Intent(context, NotesEdit.class), 0);
		SP sp = new SP(ETipsContants.SP_NAME_Notes, context);
		list = null;
		list = new ArrayList<MNotes>();
		String content = null;
		if (!sp.isEmpty()) {
			list = (List<MNotes>) sp.toEntityAll(ETipsContants.TYPE_SP_Notes);
			if (item_no != -1)
				content = wrapData(list.get(item_no));
			else {
				content = wrapData(list.get(0));// first time
				item_no = 0;
			}
		} else {
			content = "随时随地，记录生活点滴";
		}
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_notes);
		remoteViews.setOnClickPendingIntent(R.id.widget_notes_content,
				startNotes);
		remoteViews.setOnClickPendingIntent(R.id.widget_notes_btn_addNote,
				editNotes);

		if (list.size() > 1) {
		
			int pre = item_no - 1 == -1 ? list.size() - 1 : item_no - 1;
			int next = item_no + 1 == list.size() ? 0 : item_no + 1;
			PendingIntent pi_pre = PendingIntent.getBroadcast(context, 0,
					new Intent(ETipsContants.Action_Notes).putExtra("item_no", pre), PendingIntent.FLAG_UPDATE_CURRENT);
			PendingIntent pi_next = PendingIntent.getBroadcast(context, 0,
					new Intent(ETipsContants.Action_Notes).putExtra("item_no", next), PendingIntent.FLAG_UPDATE_CURRENT);
			
			remoteViews.setOnClickPendingIntent(R.id.iv_previous,
					pi_pre);
			remoteViews.setOnClickPendingIntent(R.id.iv_next,
					pi_next);
		}
		remoteViews.setTextViewText(R.id.widget_notes_tv_content, content);
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		ComponentName componentName = new ComponentName(context,
				NotesAppWidget.class);
		appWidgetManager.updateAppWidget(componentName, remoteViews);
	}
}
