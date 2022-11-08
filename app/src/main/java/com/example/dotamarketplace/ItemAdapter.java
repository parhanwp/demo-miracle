package com.example.dotamarketplace;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class ItemAdapter extends SimpleCursorAdapter {
    private Context context;
    private int layout;
    private Cursor cursor;
    private final LayoutInflater inflater;

    public ItemAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
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
    public void bindView(View view, final Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        final int row_id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper._ID));
        Button button = view.findViewById(R.id.buy_item);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BuyItemActivity.class);
                intent.putExtra("id_item", row_id);
                context.startActivity(intent);
            }
        });
    }
}
