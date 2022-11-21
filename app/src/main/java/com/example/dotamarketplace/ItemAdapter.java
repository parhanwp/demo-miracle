package com.example.dotamarketplace;



import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.rudderstack.android.sdk.core.RudderClient;
import com.rudderstack.android.sdk.core.*;
import com.rudderstack.android.sdk.core.RudderConfig;
import com.rudderstack.android.sdk.core.RudderLogger;


public class ItemAdapter extends SimpleCursorAdapter {
    TextView name, stock, price;


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
        final RudderClient rudderClient = RudderstackClient.getRudderClient();
        super.bindView(view, context, cursor);
        final int row_id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper._ID));
        Button button = view.findViewById(R.id.buy_item);
        name = view.findViewById(R.id.item_name);
        stock = view.findViewById(R.id.item_stok);
        price = view.findViewById(R.id.item_price);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int prc = Integer.parseInt(price.getText().toString());
                int isk = Integer.parseInt(stock.getText().toString());
                Intent intent = new Intent(context, BuyItemActivity.class);
                intent.putExtra("id_item", row_id);
                Log.d("log_adam",stock.getText().toString());
                context.startActivity(intent);
                rudderClient.track(
                        "addToCarts",
                        new RudderProperty()
                                .putValue("name", name.getText().toString())
                                .putValue("stock", isk)
                                .putValue("prices", prc)

                );

            }
        });
    }
}
