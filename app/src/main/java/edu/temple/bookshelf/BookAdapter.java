package edu.temple.bookshelf;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class BookAdapter extends ArrayAdapter {

    Context context;
    public BookAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LinearLayout linearLayout= new LinearLayout(context);
        TextView textView = new TextView(context);
        TextView textView2  = new TextView(context);


            linearLayout.addView(textView);
            linearLayout.addView(textView2);
            textView.setTextSize(22);
            textView2.setTextSize(15);
            textView.setPadding(15,20,0,20);
        textView2.setPadding(15,0,0,20);
            linearLayout.setOrientation(LinearLayout.VERTICAL);


        textView.setText(((Book) (getItem(position))).getTitle());
        textView2.setText(((Book) (getItem(position))).getAuthor());
        //linearLayout.notifyAll();
        return linearLayout;
    }
}
