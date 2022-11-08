package com.example.dotamarketplace;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.rudderstack.android.sdk.core.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rudderstack.android.sdk.core.RudderClient;
import com.rudderstack.android.sdk.core.RudderConfig;
import com.rudderstack.android.sdk.core.RudderLogger;

import java.util.ArrayList;

public class HistoryAdapter extends SimpleCursorAdapter {

    private Context context;
    private int layout;
    private Cursor cursor;
    private final LayoutInflater inflater;

    public HistoryAdapter(Context context, int layout, Cursor c, String[] from, int[] to){
        super(context, layout, c, from, to);
        this.layout = layout;
        this.context = context;
        this.cursor = c;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layout, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        int id_user = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.USER_ID));
        int id_item = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ITEM_ID));
        TextView name = view.findViewById(R.id.item_name);
        TextView qty = view.findViewById(R.id.qty);
        DatabaseManager manager = new DatabaseManager(view.getContext());
        manager.open();
        Cursor item = manager.getDetailItem(id_item);
        String itemName = item.getString(item.getColumnIndex(DatabaseHelper.NAME));
        name.setText(itemName);
        qty.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUANTITY)));
    }
}
