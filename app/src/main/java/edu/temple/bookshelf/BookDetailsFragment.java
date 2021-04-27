package edu.temple.bookshelf;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_BOOK = "param1";
    private TextView textView2;
    private TextView textView;
    private ImageView imageView;

    // TODO: Rename and change types of parameters
    private Book book;

    public BookDetailsFragment() {

    }

    public static BookDetailsFragment newInstance(Book book) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_BOOK, book);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            book = getArguments().getParcelable(ARG_BOOK);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View l =  inflater.inflate(R.layout.fragment_book_details, container, false);

        textView = l.findViewById(R.id.textView);
        textView2 = l.findViewById(R.id.textView2);
        imageView = l.findViewById(R.id.imageView2);
        if(book!= null){
            displayBook(book);
        }


        return l;
    }

    public void displayBook(Book book){
        textView.setText(book.getTitle());
        textView2.setText(book.getAuthor());
        Picasso.get().load(Uri.parse(book.getCoverURL())).into(imageView);

    }
}