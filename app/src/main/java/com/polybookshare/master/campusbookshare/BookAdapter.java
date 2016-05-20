package com.polybookshare.master.campusbookshare;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;

import java.util.List;

/**
 * Created by MASTER on 5/6/16.
 */
public class BookAdapter extends RecyclerView.Adapter<BookView>{
    private List<Book> m_books;

    public BookAdapter(List<Book> books) {
        this.m_books = books;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.book_view;
    }

    @Override
    public BookView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookView(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(BookView holder, int position) {
        holder.bind(this.m_books.get(position), holder.itemView);
    }


    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return this.m_books.size();
    }
}



