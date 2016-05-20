package com.polybookshare.master.campusbookshare;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by MASTER on 5/6/16.
 */
public class BookView extends  RecyclerView.ViewHolder {
    private Book m_book;
    private ImageView book_cover;
    private TextView author;
    private TextView title;
    private TextView price;
    private TextView description;
    private TextView condition;

    public BookView(View view) {
        super(view);
        book_cover = (ImageView) view.findViewById(R.id.book_picture);
        author = (TextView) view.findViewById(R.id.author);
        title = (TextView) view.findViewById(R.id.book_title);
        price = (TextView) view.findViewById(R.id.price);
        description = (TextView) view.findViewById(R.id.comments);
        condition = (TextView) view.findViewById(R.id.condition);
    }

    public void bind(Book book, View view) {
        this.m_book = book;

        Picasso.with(view.getContext())
                .load(this.m_book.getCover_url())
                .resize(250, 350)
                .centerCrop()
                .into(book_cover);
        author.setText(this.m_book.getAuthor());
        title.setText(this.m_book.getTitle());
        price.setText("$" + this.m_book.getPrice());
        condition.setText(this.m_book.getCondition());
        description.setText(this.m_book.getDescription());
    }

    public Book getBook() {
        return this.m_book;
    }

}
