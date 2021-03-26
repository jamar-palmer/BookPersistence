package edu.temple.bookshelf;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_BOOKLIST = "param1";

    // TODO: Rename and change types of parameters
    private BookList bookList;


    public BookListFragment() {

    }


    public static BookListFragment newInstance(BookList bookList) {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_BOOKLIST, bookList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookList = getArguments().getParcelable(ARG_BOOKLIST);
        }else{
            bookList = new BookList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ListView listView = (ListView) inflater.inflate(R.layout.fragment_book_list, container, false);
        ArrayList<Book> arrayList = new ArrayList();
        for(int i = 0; i < bookList.size(); i++){
            arrayList.add(bookList.get(i));
        }
        listView.setAdapter(new BookAdapter(getActivity(), android.R.layout.simple_list_item_1, arrayList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((BookListFragmentInterface) getActivity()).itemClicked(position);
            }
        });
        return listView;
    }



    interface BookListFragmentInterface {
        public void itemClicked(int position);
    }
}