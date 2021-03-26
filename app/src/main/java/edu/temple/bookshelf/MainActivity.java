package edu.temple.bookshelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListFragmentInterface {

    BookList bookList;
    boolean container2present;
    BookDetailsFragment bookDetailsFragment;
    int placeholder=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container2present = findViewById(R.id.container_2) != null;

         bookList = new BookList();
        ArrayList<Book> test = new ArrayList();


        String[] books = getResources().getStringArray(R.array.books);
        String[] authors = getResources().getStringArray(R.array.authors);
        for (int i = 0; i < books.length; i++ ){
            bookList.add(new Book(books[i], authors[i]));
         }


        getSupportFragmentManager().beginTransaction().replace(R.id.container_1, BookListFragment.newInstance(bookList)).addToBackStack(null).commit();
        if(container2present){
            bookDetailsFragment = new BookDetailsFragment();


            getSupportFragmentManager().beginTransaction().replace(R.id.container_2, bookDetailsFragment).commit();
        }

    }

    @Override
    public void itemClicked(int position) {
        placeholder = position;
        if(!container2present) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container_1, BookDetailsFragment.newInstance(bookList.get(position))).addToBackStack(null).commit();
        }else{
            bookDetailsFragment.displayBook(bookList.get(position));

        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("place", placeholder);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int placing = savedInstanceState.getInt("place");

        if(!container2present) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container_1, BookDetailsFragment.newInstance(bookList.get(placing))).addToBackStack(null).commit();
        }else{
            bookDetailsFragment.displayBook(bookList.get(placing));

        }
    }
}